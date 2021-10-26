package com.valteris.database.service.impl;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.valteris.database.domain.Cell;
import com.valteris.database.domain.Column;
import com.valteris.database.domain.Database;
import com.valteris.database.domain.Line;
import com.valteris.database.domain.Table;
import com.valteris.database.entity.DatabaseEntity;
import com.valteris.database.exception.DatabaseNotFoundException;
import com.valteris.database.repository.DatabaseRepository;
import com.valteris.database.service.DatabaseService;
import com.valteris.database.service.mapper.Mapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DatabaseServiceImpl implements DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final Mapper<Database, DatabaseEntity> databaseMapper;
    private final Gson gson = new Gson();

    @Override
    public List<String> findAllDatabases() {
        return findAllInStorage();
//        return databaseRepository.findAll().stream().map(DatabaseEntity::getName).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public Optional<Database> findDatabaseByName(String name) {
        JsonReader reader = new JsonReader(new FileReader(name + ".json"));
        return Optional.of(gson.fromJson(reader, Database.class));
//        return databaseRepository.findDatabaseEntityByName(name).map(databaseMapper::mapEntityToDomain);
    }

    @Override
    public void createDatabase(String name) {
        String dbName = name + "db";
        final Database database = new Database();
        database.setName(dbName);

        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    @Override
    public void createTable(Table table) {
        final String dbName = table.getDbName();
        final Database database = findDatabaseByName(dbName).get();
        database.getTables().add(table);
        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    @Override
    public Optional<Table> findTableByName(String dbName, String tableName) {
        final Database database = findDatabaseByName(dbName).get();
        return Optional.of(getTable(tableName, database));
    }

    @Override
    public void addNewLine(String dbName, String tableName) {
        final Database database = findDatabaseByName(dbName).get();
        final Table table = getTable(tableName, database);
        table.setIncrementor(table.getIncrementor() + 1);
        final Line line = new Line();
        line.setId(table.getIncrementor());
        for (int i = 0; i < table.getColumns().size(); i++) {
            line.getCells().add(new Cell());
        }
        table.getLines().add(line);

        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    @Override
    public void addNewColumn(Table table) {
        final Database database = findDatabaseByName(table.getDbName()).get();
        final Table subTable = getTable(table.getTableName(), database);
        subTable.getColumns().add(table.getColumns().get(0));
        for (Line line: subTable.getLines()) {
            line.getCells().add(new Cell());
        }

        writeToFile(table.getDbName(), database);
    }

    @Override
    public void deleteColumn(Table table) {
        final Database database = findDatabaseByName(table.getDbName()).get();
        final Table subTable = getTable(table.getTableName(), database);
        final Column column = table.getColumns().get(0);
        final Column delColumn = subTable.getColumns().stream()
                .filter(c -> c.getName().equals(column.getName())).findFirst().get();
        final int i = subTable.getColumns().indexOf(delColumn);
        subTable.getColumns().remove(i);
        for (Line line: subTable.getLines()) {
            line.getCells().remove(i);
        }

        writeToFile(table.getDbName(), database);
    }

    @Override
    public void deleteLine(Long id, String dbName, String tableName) {
        final Database database = findDatabaseByName(dbName).get();
        final Table table = getTable(tableName, database);
        final Line delLine = table.getLines().stream()
                .filter(l -> l.getId().equals(id)).findFirst().get();
        table.getLines().remove(delLine);

        writeToFile(dbName, database);
        writeToSubFile(dbName, database);
    }

    private Table getTable(String tableName, Database database) {
        return database.getTables().stream().filter(t -> t.getTableName().equals(tableName)).findFirst().get();
    }

    private List<String> findAllInStorage() {
        try (Stream<Path> paths = Files.walk(Paths.get(""))) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().contains("db.json"))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DatabaseNotFoundException(e.toString());
        }
    }

    private void writeToSubFile(String dbName, Database database) {
        try {
            final Writer subFileWriter = new FileWriter(dbName + "sub.json");
            gson.toJson(getNumOfLines(database), subFileWriter);
            subFileWriter.flush();
            subFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> getNumOfLines(Database database) {
        Map<String, Integer> tableNameToNumOfLines = new HashMap<>();
        for (Table table : database.getTables()) {
            tableNameToNumOfLines.put(table.getTableName(), table.getLines().size());
        }
        return tableNameToNumOfLines;
    }

    private void writeToFile(String name, Database database) {
        try {
            final Writer fileWriter = new FileWriter(name + ".json");
            gson.toJson(database, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
