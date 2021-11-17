package com.valteris.database.cotroller;

import com.valteris.database.convector.TableConvertor;
import com.valteris.database.domain.Database;
import com.valteris.database.domain.Table;
import com.valteris.database.dto.CreateTableJson;
import com.valteris.database.dto.InnerJoinJson;
import com.valteris.database.dto.TableErrorValidation;
import com.valteris.database.dto.TableJson;
import com.valteris.database.service.CellValidationService;
import com.valteris.database.service.DatabaseService;
import com.valteris.database.service.impl.CellValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"tableResponse"})
@RequiredArgsConstructor
public class TableController {

    public static final String TABLE_RESPONSE = "tableResponse";
    private final DatabaseService databaseService;
    private final CellValidationService cellValidationService;
    private final TableConvertor tableConvertor;

    @ModelAttribute(name = TABLE_RESPONSE)
    public TableJson tableResponse() {
        return new TableJson();
    }

    @GetMapping("/table")
    public String getNewTablePage(@RequestParam String dbName, Model model) {
        model.addAttribute("dbName", dbName);
        return "new_table";
    }

    @PostMapping(value = "/table", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateTableJson createNewTable(@RequestBody CreateTableJson createTableJson) {
        databaseService.createTable(tableConvertor.convertCreateTableToDomain(createTableJson));

        return createTableJson;
    }

    @GetMapping("/table/{name}")
    public String getTablePage(@PathVariable(name = "name") String tableName, @RequestParam String dbName, Model model) {
        final Table table = databaseService.findTableByName(dbName, tableName);
        model.addAttribute("table", table);
        return "table";
    }

    @GetMapping("/table/meta/{name}")
    @ResponseBody
    public TableJson getTable(@PathVariable(name = "name") String tableName, @RequestParam String dbName) {
        final Table table = databaseService.findTableByName(dbName, tableName);
        return tableConvertor.convertToJsonDTO(table);
    }

    @GetMapping("/table/join/inner")
    public String getInnerJoinTablePage(@RequestParam String dbName, Model model) {
        Database database = databaseService.findDatabaseByName(dbName);
        model.addAttribute("database", database);
        return "inner_join";
    }

    @PostMapping("/table/join/inner")
    @ResponseStatus(HttpStatus.CREATED)
    public void innerJoinTables(@RequestBody InnerJoinJson innerJoinJson, Model model) {
        boolean isColumnTypesNotEqual = !innerJoinJson.getTable1().column.type.equals(innerJoinJson.getTable2().column.type);
        if (isColumnTypesNotEqual) {
            model.addAttribute(TABLE_RESPONSE, new TableJson());
            return;
        }

        boolean isSelfJoin = innerJoinJson.getTable1().tableName.equals(innerJoinJson.getTable2().tableName);
        if (isSelfJoin) {
            model.addAttribute(TABLE_RESPONSE, new TableJson());
            return;
        }

        Table table = databaseService.innerJoin(innerJoinJson.getDbName(),
                tableConvertor.convertJoinTableToDomain(innerJoinJson.getTable1()),
                tableConvertor.convertJoinTableToDomain(innerJoinJson.getTable2()));

        model.addAttribute(TABLE_RESPONSE, tableConvertor.convertToJsonDTO(table));
    }

    @GetMapping("/result")
    public String getResultPage(@ModelAttribute(TABLE_RESPONSE) TableJson tableResponse, Model model) {
        model.addAttribute("table", tableResponse);
        return "result";
    }

    @PostMapping("/table/save")
    @ResponseBody
    public TableJson saveTable(@RequestBody TableJson tableJson,
                               @ModelAttribute(TABLE_RESPONSE) TableJson tableResponse, Model model) {
        TableErrorValidation tableErrorValidation =
                cellValidationService.validateIfTableHasErrors(tableConvertor.convertToDomain(tableJson));

        if (tableErrorValidation.isHasError()) {
            model.addAttribute(TABLE_RESPONSE, tableConvertor.convertToJsonDTO(tableErrorValidation.getTable()));
            return tableConvertor.convertToJsonDTO(tableErrorValidation.getTable());
        } else {
            databaseService.saveTable(tableConvertor.convertToDomain(tableJson));
            return tableJson;
        }
    }

    @GetMapping("/table/validation/session")
    public String getTablePageFromSession(@ModelAttribute(TABLE_RESPONSE) TableJson tableResponse, Model model) {
        model.addAttribute("table", tableResponse);
        return "table";
    }
}
