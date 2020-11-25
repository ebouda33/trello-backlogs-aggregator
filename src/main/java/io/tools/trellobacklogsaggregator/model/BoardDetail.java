package io.tools.trellobacklogsaggregator.model;

import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Label;
import io.tools.trellobacklogsaggregator.execptions.ListException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardDetail {
    private String name;
    private Double businessComplexity = 0D;
    private Double consumedComplexity = 0D;
    private Double remainedComplexity = 0D;
    private Double totalComplexity = 0D;
    private Board source;
    private List<ListByLabel> listsByLabel;
    private List<Label> listLabels;

    public BoardDetail(Board board) {
        this.source = board;
        this.setName(board.getName());
        listsByLabel = new ArrayList<>();
    }

    public void createList(String label){
        if(!getListByLabel(label).isPresent()){
            listsByLabel.add(new ListByLabel(label));
        }
    }

    public List<ListByLabel> getListsByLabel() {
        return listsByLabel;
    }

    public void addCard(Card card, String label){
        final ListByLabel listByLabel1 = getListByLabel(label).orElseThrow(ListException::new);
        listByLabel1.addCard(card);
    }

    public Optional<ListByLabel> getListByLabel(String label) {
        return listsByLabel.stream().filter(listByLabel -> listByLabel.getName().equalsIgnoreCase(label)).findFirst();
    }

    public void setListLabels(List<Label> listLabels) {
        this.listLabels = listLabels;
    }

    public List<Label> getListLabels() {
        return listLabels;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRemainedComplexity() {
        return remainedComplexity;
    }

    public void setRemainedComplexity(Double remainedComplexity) {
        this.remainedComplexity = remainedComplexity;
    }

    public Board getSource() {
        return source;
    }
}
