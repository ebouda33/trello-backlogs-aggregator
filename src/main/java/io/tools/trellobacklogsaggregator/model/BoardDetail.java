package io.tools.trellobacklogsaggregator.model;

import java.util.Map;

import com.julienvey.trello.domain.Board;

public class BoardDetail {
    private String name;
    private Double businessComplexity = 0D;
    private Double consumedComplexity = 0D;
    private Double remainedComplexity = 0D;
    private Double totalComplexity = 0D;

    private Map<String, Double> releaseBusinessComplexity;
    private Map<String, Double> releaseConsumedComplexity;
    private Map<String, Double> releaseRemainedComplexity;
    private Map<String, Double> releaseTotalComplexity;

    public BoardDetail(Board board) {
        this.setName(board.getName());
    }

    public Double getBusinessComplexity() {
        return businessComplexity;
    }

    public Double getConsumedComplexity() {
        return consumedComplexity;
    }

    public String getName() {
        return name;
    }

    public Double getRemainedComplexity() {
        return remainedComplexity;
    }

    public Double getTotalComplexity() {
        return totalComplexity;
    }

    public void setBusinessComplexity(Double businessComplexity) {
        this.businessComplexity = businessComplexity;
    }

    public void setConsumedComplexity(Double consumedComplexity) {
        this.consumedComplexity = consumedComplexity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemainedComplexity(Double remainedComplexity) {
        this.remainedComplexity = remainedComplexity;
    }

    public void setTotalComplexity(Double totalComplexity) {
        this.totalComplexity = totalComplexity;
    }

    public Map<String, Double> getReleaseBusinessComplexity() {
        return releaseBusinessComplexity;
    }

    public void setReleaseBusinessComplexity(Map<String, Double> releaseBusinessComplexity) {
        this.releaseBusinessComplexity = releaseBusinessComplexity;
    }

    public Map<String, Double> getReleaseConsumedComplexity() {
        return releaseConsumedComplexity;
    }

    public void setReleaseConsumedComplexity(Map<String, Double> releaseConsumedComplexity) {
        this.releaseConsumedComplexity = releaseConsumedComplexity;
    }

    public Map<String, Double> getReleaseRemainedComplexity() {
        return releaseRemainedComplexity;
    }

    public void setReleaseRemainedComplexity(Map<String, Double> releaseRemainedComplexity) {
        this.releaseRemainedComplexity = releaseRemainedComplexity;
    }

    public Map<String, Double> getReleaseTotalComplexity() {
        return releaseTotalComplexity;
    }

    public void setReleaseTotalComplexity(Map<String, Double> releaseTotalComplexity) {
        this.releaseTotalComplexity = releaseTotalComplexity;
    }

}
