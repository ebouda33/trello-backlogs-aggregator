package io.tools.trellobacklogsaggregator.model;

import java.util.ArrayList;
import java.util.List;

public class Sprint {
    private Double businessComplexity = 0D;
    private Double consumedComplexity = 0D;
    private Double totalComplexity = 0D;
    private Double remainedComplexity = 0D;
    private List<Column> columns = new ArrayList<>();

    public List<Column> getColumns() {
        return columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
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
}
