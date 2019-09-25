package io.tools.trellobacklogsaggregator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration")
public class AdministrationController {


    @RequestMapping("")
    public String index(Model model){
        return "administration";
    }

}
