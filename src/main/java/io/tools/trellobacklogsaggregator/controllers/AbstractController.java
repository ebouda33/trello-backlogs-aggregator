package io.tools.trellobacklogsaggregator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;

public abstract class AbstractController {

    @Autowired
    private BacklogsRepository backlogsRepository;

    protected void errorManagement(Model model) {
        model.addAttribute("errors", backlogsRepository.read().getErrors());
    }
}
