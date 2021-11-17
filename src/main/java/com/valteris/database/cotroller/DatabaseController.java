package com.valteris.database.cotroller;

import com.google.gson.Gson;
import com.valteris.database.convector.TableConvertor;
import com.valteris.database.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DatabaseController {

    private final DatabaseService databaseService;

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/database/all")
    @ResponseBody
    public String getListOfDatabases() {
        final List<String> databaseList = databaseService.findAllDatabaseNames();
        return new Gson().toJson(databaseList);
    }

    @GetMapping("/database/{name}")
    public String getDatabasePage(@PathVariable String name, Model model) {
        model.addAttribute("database", databaseService.findDatabaseByName(name));
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
}
