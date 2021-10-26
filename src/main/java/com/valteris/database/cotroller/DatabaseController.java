package com.valteris.database.cotroller;

import com.google.gson.Gson;
import com.valteris.database.domain.Database;
import com.valteris.database.domain.Line;
import com.valteris.database.domain.Table;
import com.valteris.database.dto.CreateTableJson;
import com.valteris.database.service.DatabaseService;
import com.valteris.database.util.TableConvertor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DatabaseController {

    private final DatabaseService databaseService;
    private final TableConvertor tableConvertor;

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/database/all")
    @ResponseBody
    public String getListOfDatabases() {
        final List<String> databaseList = databaseService.findAllDatabases();
        return new Gson().toJson(databaseList);
    }

    @GetMapping("/database/{name}")
    public String getDatabasePage(@PathVariable String name, Model model) {
        model.addAttribute("database", databaseService.findDatabaseByName(name).orElse(null));
        return "db";
    }

    @GetMapping("/database")
    public String getNewDatabasePage() {
        return "new_db";
    }

    @PostMapping("/database")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewDatabase(@RequestBody String name) {
        databaseService.createDatabase(name);
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
        databaseService.createTable(tableConvertor.convertToDomain(createTableJson));

        return createTableJson;
    }

    @GetMapping("/table/{name}")
    public String getTablePage(@PathVariable(name = "name") String tableName, @RequestParam String dbName, Model model) {
        final Table table = databaseService.findTableByName(dbName, tableName).orElse(null);
        model.addAttribute("table", table);
        return "table";
    }

    @GetMapping("/line")
    public String addNewLine(@RequestParam String dbName, @RequestParam String tableName, RedirectAttributes attributes) {
        databaseService.addNewLine(dbName, tableName);
        attributes.addAttribute("dbName", dbName);
        return "redirect:/table/" + tableName;
    }

    @PostMapping(value = "/column", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateTableJson addNewColumn(@RequestBody CreateTableJson createTableJson) {
        final Table table = tableConvertor.convertToDomain(createTableJson);
        databaseService.addNewColumn(table);

        return createTableJson;
    }

    @PostMapping(value = "/column/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreateTableJson deleteColumn(@RequestBody CreateTableJson createTableJson) {
        final Table table = tableConvertor.convertToDomain(createTableJson);
        databaseService.deleteColumn(table);

        return createTableJson;
    }

    @GetMapping(value = "/line/delete/{id}")
    public String deleteLine(@PathVariable Long id, @RequestParam String dbName,
                                      @RequestParam String tableName, RedirectAttributes attributes) {
        databaseService.deleteLine(id, dbName, tableName);
        attributes.addAttribute("dbName", dbName);
        return "redirect:/table/" + tableName;
    }
}
