package io.tools.trellobacklogsaggregator.model;

import java.util.ArrayList;
import java.util.List;

import com.julienvey.trello.domain.Label;

public class ColumnLabel {
    private Label label;
    private Double businessComplexity = 0D;
    private Double consumedComplexity = 0D;
    private Double totalComplexity = 0D;
    private Double remainedComplexity = 0D;
    private List<CardWithMembers> cardsWithMembers = new ArrayList<>();

    public ColumnLabel(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Double getBusinessComplexity() {
        return businessComplexity;
    }

    public void setBusinessComplexity(Double businessComplexity) {
        this.businessComplexity = businessComplexity;
    }

    public Double getConsumedComplexity() {
        return consumedComplexity;
    }

    public void setConsumedComplexity(Double consumedComplexity) {
        this.consumedComplexity = consumedComplexity;
    }

    public Double getTotalComplexity() {
        return totalComplexity;
    }

    public void setTotalComplexity(Double totalComplexity) {
        this.totalComplexity = totalComplexity;
    }

    public Double getRemainedComplexity() {
        return remainedComplexity;
    }

    public void setRemainedComplexity(Double remainedComplexity) {
        this.remainedComplexity = remainedComplexity;
    }

    public List<CardWithMembers> getCardsWithMembers() {
        return cardsWithMembers;
    }

    public void addCard(CardWithMembers cardWithMembers) {
        this.cardsWithMembers.add(cardWithMembers);
    }
}
