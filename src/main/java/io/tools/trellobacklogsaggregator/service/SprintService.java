package io.tools.trellobacklogsaggregator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.TList;

import io.tools.trellobacklogsaggregator.model.Column;
import io.tools.trellobacklogsaggregator.model.Sprint;

@Service
public class SprintService {

    @Autowired
    private CardService cardService;

    public Sprint addCard(Sprint sprint, TList list, Card card) {
        Column column = null;
        String listName = list.getName();
        for (int i = 0; i < sprint.getColumns().size(); i++) {
            Column columnInSprint = sprint.getColumns().get(i);
            String columnInSprintName = columnInSprint.getName();
            if (listName.equals(columnInSprintName)) {
                column = columnInSprint;
                break;
            }
        }
        if (column == null) {
            column = new Column();
            column.setName(listName);
            column.addCard(card);
        } else {
            column.addCard(card);
        }

        sprint.setBusinessComplexity(sprint.getBusinessComplexity() + cardService.getCardValue(card));
        sprint.setConsumedComplexity(sprint.getConsumedComplexity() + cardService.getComplexiteRealisee(card));
        sprint.setTotalComplexity(sprint.getTotalComplexity() + cardService.getComplexiteTotale(card));
        sprint.setRemainedComplexity(sprint.getTotalComplexity() - sprint.getConsumedComplexity());
        return sprint;
    }
}
