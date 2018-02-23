package io.tools.trellobacklogsaggregator.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;

import io.tools.trellobacklogsaggregator.model.CardWithMembers;
import io.tools.trellobacklogsaggregator.model.Column;
import io.tools.trellobacklogsaggregator.model.ColumnLabel;
import io.tools.trellobacklogsaggregator.model.Sprint;

@Service
public class SprintService {

    @Autowired
    private CardService cardService;

    public Sprint addCard(Sprint sprint, TList list, Card card, Map<String, Member> possibleMembers, String backlogName) {
        Column column = getColumnByListName(sprint, list);

        CardWithMembers cardWithMembers = cardService.createCardWithMembers(card, possibleMembers, backlogName);
        column.addCard(cardWithMembers);

        Double cardBusinessComplexity = cardService.getBusinessComplexity(card);
        Double cardConsumedComplexity = cardService.getConsumedComplexity(card);
        Double cardTotalComplexity = cardService.getTotalComplexity(card);

        List<Label> cardLabels = card.getLabels();

        for (int i = 0; i < cardLabels.size(); i++) {
            ColumnLabel columnLabel = getColumnLabelFromCardLabel(column, cardLabels.get(i));

            columnLabel.setBusinessComplexity(columnLabel.getBusinessComplexity() + cardBusinessComplexity);
            columnLabel.setConsumedComplexity(columnLabel.getConsumedComplexity() + cardConsumedComplexity);
            columnLabel.setTotalComplexity(columnLabel.getTotalComplexity() + cardTotalComplexity);
            columnLabel.setRemainedComplexity(columnLabel.getTotalComplexity() - columnLabel.getConsumedComplexity());
            columnLabel.addCard(cardWithMembers);
        }

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

    private ColumnLabel getColumnLabelFromCardLabel(Column column, Label cardLabel) {
        ColumnLabel columnLabel = null;
        for (int j = 0; j < column.getColumnLabels().size(); j++) {
            String columnLabelName = column.getColumnLabels().get(j).getLabel().getName();
            if (columnLabelName.equals(cardLabel.getName())) {
                columnLabel = column.getColumnLabels().get(j);
                break;
            }
        }
        return columnLabel;
    }

    private Column getColumnByListName(Sprint sprint, TList list) {
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
        return column;
    }

    public Sprint addColumn(Sprint sprint, TList list, List<Label> possibleLabels) {
        String listName = list.getName();
        Column column = new Column();
        column.setName(listName);
        for (Label label : possibleLabels) {
            column.addColumnLabel(new ColumnLabel(label));
        }
        sprint.addColumn(column);
        return sprint;
    }

}
