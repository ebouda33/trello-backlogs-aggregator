package io.tools.trellobacklogsaggregator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;

import io.tools.trellobacklogsaggregator.model.CardWithMembers;
import io.tools.trellobacklogsaggregator.model.Column;
import io.tools.trellobacklogsaggregator.model.Sprint;

@Service
public class SprintService {

    @Autowired
    private CardService cardService;

    public Sprint addCard(Sprint sprint, TList list, Card card, Map<String, Member> possibleMembers) {
        Column column = null;
        String listName = list.getName();
        for (int i = 0; i < sprint.getColumns().size(); i++) {
            Column columnInSprint = sprint.getColumns().get(i);
            String columnInSprintName = columnInSprint.getName();
            if (listName.equalsIgnoreCase(columnInSprintName)) {
                column = columnInSprint;
                break;
            }
        }
        if (column == null) {
            column = new Column();
            column.setName(listName);
            sprint.addColumn(column);
        }

        column.addCard(createCardWithMembers(card, possibleMembers));

        Double cardBusinessComplexity = cardService.getBusinessComplexity(card);
        Double cardConsumedComplexity = cardService.getConsumedComplexity(card);
        Double cardTotalComplexity = cardService.getTotalComplexity(card);

        column.setBusinessComplexity(column.getBusinessComplexity() + cardBusinessComplexity);
        column.setConsumedComplexity(column.getConsumedComplexity() + cardConsumedComplexity);
        column.setTotalComplexity(column.getTotalComplexity() + cardTotalComplexity);
        column.setRemainedComplexity(column.getTotalComplexity() - column.getConsumedComplexity());

        sprint.setBusinessComplexity(sprint.getBusinessComplexity() + cardBusinessComplexity);
        sprint.setConsumedComplexity(sprint.getConsumedComplexity() + cardConsumedComplexity);
        sprint.setTotalComplexity(sprint.getTotalComplexity() + cardTotalComplexity);
        sprint.setRemainedComplexity(sprint.getTotalComplexity() - sprint.getConsumedComplexity());
        return sprint;
    }

    private CardWithMembers createCardWithMembers(Card card, Map<String, Member> possibleMembers) {
        List<Member> cardMembers = new ArrayList<>();
        card.getIdMembers().forEach(idMember -> {
            cardMembers.add(possibleMembers.get(idMember));
        });
        return new CardWithMembers(card, cardMembers);
    }
}
