package com.valteris.database.cotroller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DatabaseController {


    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

}
