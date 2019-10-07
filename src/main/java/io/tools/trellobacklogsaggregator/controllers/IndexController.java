package io.tools.trellobacklogsaggregator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;

@Controller
public class IndexController extends AbstractController {

    @Autowired
    private BacklogsRepository backlogsRepository;

    @RequestMapping("/")
    public String index(Model model) {
        if (backlogsRepository.read() != null) {
            model.addAttribute("boards", backlogsRepository.read().getBoards());
            errorManagement(model);
        }
        getContext(model);
        return "index";
    }
}
