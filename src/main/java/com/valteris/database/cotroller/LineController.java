package com.valteris.database.cotroller;

import com.valteris.database.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LineController {

    private final DatabaseService databaseService;

    @GetMapping("/line")
    public String addNewLine(@RequestParam String dbName, @RequestParam String tableName, RedirectAttributes attributes) {
        databaseService.addNewLine(dbName, tableName);
        attributes.addAttribute("dbName", dbName);
        return "redirect:/table/" + tableName;
    }

    @GetMapping(value = "/line/delete/{id}")
    public String deleteLine(@PathVariable Long id, @RequestParam String dbName,
                             @RequestParam String tableName, RedirectAttributes attributes) {
        databaseService.deleteLine(id, dbName, tableName);
        attributes.addAttribute("dbName", dbName);
        return "redirect:/table/" + tableName;
    }
}
