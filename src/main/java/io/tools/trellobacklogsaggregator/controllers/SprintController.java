package io.tools.trellobacklogsaggregator.controllers;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.model.Column;
import io.tools.trellobacklogsaggregator.model.ColumnLabel;
import io.tools.trellobacklogsaggregator.model.Sprint;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;

@Controller
public class SprintController extends AbstractController {

    @Autowired
    private BacklogsRepository backlogsRepository;

    @Autowired
    private CustomConfiguration customConfiguration;

    @RequestMapping("/sprint")
    public String index(Model model) {
        if (backlogsRepository.read() != null) {
            Sprint sprint = backlogsRepository.read().getSprint();
            if (sprint != null) {

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
                Comparator<ColumnLabel> columnLabelComparator = new Comparator<ColumnLabel>() {
                    @Override
                    public int compare(ColumnLabel columnLabel1, ColumnLabel columnLabel2) {
                        return columnLabel1.getLabel().getName().compareTo(columnLabel2.getLabel().getName());
                    }
                };

                model.addAttribute("sprint", sprint);
                model.addAttribute("columnLabelComparator", columnLabelComparator);
            }
            int totalNbItemsInSprint = 0;
            for (Column column : sprint.getColumns()) {
                totalNbItemsInSprint += column.getCardsWithMembers().size();
            }
            model.addAttribute("totalNbItemsInSprint", totalNbItemsInSprint);
            errorManagement(model);
        }
        return "sprint";
    }
}
