package io.tools.trellobacklogsaggregator.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import io.tools.trellobacklogsaggregator.model.BacklogError;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;

public abstract class AbstractController {

    @Autowired
    private BacklogsRepository backlogsRepository;

    protected void errorManagement(Model model) {
        List<BacklogError> errors = backlogsRepository.read().getErrors();
        if (errors != null && errors.size() > 0) {
            model.addAttribute("errors", errors);
        }
    }
}
