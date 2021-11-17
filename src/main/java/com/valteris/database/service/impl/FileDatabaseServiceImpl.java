package com.valteris.database.service.impl;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.valteris.database.domain.Cell;
import com.valteris.database.domain.Column;
import com.valteris.database.domain.Database;
import com.valteris.database.domain.JoinResult;
import com.valteris.database.domain.Line;
import com.valteris.database.domain.Table;
import com.valteris.database.exception.DatabaseNotFoundException;
import com.valteris.database.exception.TableNotFoundException;
import com.valteris.database.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Profile("file")
public class FileDatabaseServiceImpl implements DatabaseService {

    private final Gson gson;

    @Value("${database.storage.path:}")
    private String databaseStoragePath;

    @Override
    public List<String> findAllDatabaseNames() {
        try (Stream<Path> paths = Files.walk(Paths.get(databaseStoragePath))) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().contains("db.json"))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DatabaseNotFoundException(e.toString());
        }
    }

    @SneakyThrows
    @Override
    public Database findDatabaseByName(String name) {
        JsonReader reader = new JsonReader(new FileReader(name + ".json"));
        return gson.fromJson(reader, Database.class);
    }

    @Override
    public void createDatabase(String name) {
        String dbName = name + "db";
        Database database = Database.builder()
                .name(dbName)
                .build();

        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    @Override
    public void createTable(Table table) {
        String dbName = table.getDbName();
        table.setIncrementor(1L);
        Database database = findDatabaseByName(dbName);
        database.registerTable(table);

        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    @Override
    public Table findTableByName(String dbName, String tableName) {
        Database database = findDatabaseByName(dbName);
        return getTableFromDatabase(tableName, database);
    }

    @Override
    //todo unify api
    public void addNewLine(String dbName, String tableName) {
        Database database = findDatabaseByName(dbName);
        Table table = getTableFromDatabase(tableName, database);

        Line.LineBuilder builder = Line.builder()
                .id(table.getIncrementor());

        table.getColumns().stream()
                .map(column -> Cell.builder()
                        .type(column.getType())
                        .columnName(column.getName())
                        .lineId(table.getIncrementor())
                        .build())
                .forEach(builder::cell);

        table.getLines().add(builder.build());
        table.setIncrementor(table.getIncrementor() + 1);

        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    @Override
    public void addNewColumn(Table updatedTable) {
        Database database = findDatabaseByName(updatedTable.getDbName());
        Table originalTable = getTableFromDatabase(updatedTable.getTableName(), database);

        Column column = updatedTable.getFirstColumn();
        originalTable.addColumnToList(column);

        for (Line line : originalTable.getLines()) {
            Cell cell = Cell.builder()
                    .type(column.getType())
                    .columnName(column.getName())
                    .lineId(line.getId())
                    .build();
            line.addCell(cell);
        }

        writeToFile(updatedTable.getDbName(), database);
    }

    @Override
    public void deleteColumn(Table updatedTable) {
        Database database = findDatabaseByName(updatedTable.getDbName());
        Table originalTable = getTableFromDatabase(updatedTable.getTableName(), database);

        Column column = updatedTable.getFirstColumn();
        Column delColumn = originalTable.getColumnByName(column.getName());

        int i = originalTable.getColumns().indexOf(delColumn);
        originalTable.getColumns().remove(i);

        for (Line line : originalTable.getLines()) {
            line.getCells().remove(i);
        }

        writeToFile(updatedTable.getDbName(), database);
    }

    @Override
    public void deleteLine(Long id, String dbName, String tableName) {
        Database database = findDatabaseByName(dbName);
        Table originalTable = getTableFromDatabase(tableName, database);

        Line delLine = originalTable.getLineById(id);
        originalTable.getLines().remove(delLine);

        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    @Override
    public void saveTable(Table newTable) {
        Database database = findDatabaseByName(newTable.getDbName());
        Table originalTable = getTableFromDatabase(newTable.getTableName(), database);
        int index = database.getTables().indexOf(originalTable);

        newTable.setIncrementor(originalTable.getIncrementor());
        database.getTables().set(index, newTable);

        writeToFile(newTable.getDbName(), database);
        writeToSubFile(newTable.getDbName(), database);
    }

    @Override
    public Table innerJoin(String dbName, Table table1, Table table2) {
        Database database = findDatabaseByName(dbName);

        Table originalTableLeft = getTableFromDatabase(table1.getTableName(), database);
        Table originalTableRight = getTableFromDatabase(table2.getTableName(), database);
        List<Cell> columnCellsLeft = originalTableLeft.getColumnCells(table1.getFirstColumn().getName());
        List<Cell> columnCellsRight = originalTableRight.getColumnCells(table2.getFirstColumn().getName());

        List<JoinResult> joinResults = new ArrayList<>();
        findJoiningCells(originalTableLeft, originalTableRight, columnCellsLeft, columnCellsRight, joinResults);

        if (joinResults.isEmpty()) {
            return Table.builder()
                    .tableName("result")
                    .build();
        }

        List<Column> columns = new ArrayList<>();
        List<Line> lines = new ArrayList<>();
        Column.ColumnBuilder columnBuilder = Column.builder();
        columns.add(columnBuilder
                .name("joining")
                .type(table1.getFirstColumn().getType())
                .build());

        mergeCells(table1, table2, joinResults, columns, columnBuilder);
        mergeLines(table1, table2, joinResults, lines);

        return Table.builder()
                .tableName("result")
                .columns(columns)
                .lines(lines)
                .build();

    }

    private void findJoiningCells(Table originalTableLeft, Table originalTableRight, List<Cell> columnCellsLeft,
                                  List<Cell> columnCellsRight, List<JoinResult> joinResults) {
        for (Cell leftCell : columnCellsLeft) {
            for (Cell rightCell : columnCellsRight) {
                if (leftCell.getValue().equals(rightCell.getValue())) {
                    JoinResult joinResult = JoinResult.builder()
                            .leftLine(originalTableLeft.getLineById(leftCell.getLineId()))
                            .rightLine(originalTableRight.getLineById(rightCell.getLineId()))
                            .joiningCell(Cell.builder()
                                    .value(leftCell.getValue())
                                    .type(leftCell.getType())
                                    .build())
                            .build();
                    joinResults.add(joinResult);
                }
            }
        }
    }

    private void mergeCells(Table table1, Table table2, List<JoinResult> joinResults,
                            List<Column> columns, Column.ColumnBuilder columnBuilder) {
        for (Cell cell : joinResults.get(0).getLeftLine().getCells()) {
            if (!cell.getColumnName().equals(table1.getFirstColumn().getName())) {
                columnBuilder
                        .name(cell.getColumnName())
                        .type(cell.getType());
                columns.add(columnBuilder.build());
            }
        }
        for (Cell cell : joinResults.get(0).getRightLine().getCells()) {
            if (!cell.getColumnName().equals(table2.getFirstColumn().getName())) {
                columnBuilder
                        .name(cell.getColumnName())
                        .type(cell.getType());
                columns.add(columnBuilder.build());
            }
        }
    }

    private void mergeLines(Table table1, Table table2, List<JoinResult> joinResults, List<Line> lines) {
        for (JoinResult joinResult : joinResults) {
            Line.LineBuilder lineBuilder = Line.builder();
            lineBuilder.cell(joinResult.getJoiningCell());
            for (Cell cell : joinResult.getLeftLine().getCells()) {
                if (!cell.getColumnName().equals(table1.getFirstColumn().getName())) {
                    lineBuilder.cell(cell);
                }
            }
            for (Cell cell : joinResult.getRightLine().getCells()) {
                if (!cell.getColumnName().equals(table2.getFirstColumn().getName())) {
                    lineBuilder.cell(cell);
                }
            }
            lines.add(lineBuilder.build());
        }
    }

    private Table getTableFromDatabase(String tableName, Database database) {
        return database.getTables().stream()
                .filter(t -> t.getTableName().equals(tableName))
                .findFirst()
                .orElseThrow(TableNotFoundException::new);
    }

    private Map<String, Integer> getNumOfLines(Database database) {
        Map<String, Integer> tableNameToNumOfLines = new HashMap<>();
        for (Table table : database.getTables()) {
            tableNameToNumOfLines.put(table.getTableName(), table.getLines().size());
        }
        return tableNameToNumOfLines;
    }

    @SneakyThrows
    private void writeToFile(String name, Database database) {
        Writer fileWriter = new FileWriter(name + ".json");
        gson.toJson(database, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    @SneakyThrows
    //todo remove sub file
    private void writeToSubFile(String dbName, Database database) {
        Writer subFileWriter = new FileWriter(dbName + "sub.json");
        gson.toJson(getNumOfLines(database), subFileWriter);
        subFileWriter.flush();
        subFileWriter.close();
    }
}
