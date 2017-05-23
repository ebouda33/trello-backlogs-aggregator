package io.tools.trellobacklogsaggregator.model;

import com.julienvey.trello.domain.Board;

public class BoardDetail {
    private String name;
    private Double businessComplexity = 0D;
    private Double consumedComplexity = 0D;
    private Double totalComplexity = 0D;

    public BoardDetail(Board board) {
        this.setName(board.getName());
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

}
