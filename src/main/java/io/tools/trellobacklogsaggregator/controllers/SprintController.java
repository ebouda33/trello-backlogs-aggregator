package io.tools.trellobacklogsaggregator.controllers;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.model.Column;
import io.tools.trellobacklogsaggregator.model.Sprint;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;

@Controller
public class SprintController {

    @Autowired
    private BacklogsRepository backlogsRepository;

    @Autowired
    private CustomConfiguration customConfiguration;

    @RequestMapping("/sprint")
    public String index(Model model) {
        if (backlogsRepository.read() != null) {
            Sprint sprint = backlogsRepository.read().getSprint();
            sprint.getColumns().sort(new Comparator<Column>() {

                @Override
                public int compare(Column col1, Column col2) {
                    int indexCol1 = customConfiguration.getColumnInSprintAllowed()
                            .indexOf(col1.getName().toLowerCase());
                    int indexCol2 = customConfiguration.getColumnInSprintAllowed()
                            .indexOf(col2.getName().toLowerCase());
                    return indexCol1 - indexCol2;
                }

            });
            model.addAttribute("sprint", sprint);
        }
        return "sprint";
    }
}
