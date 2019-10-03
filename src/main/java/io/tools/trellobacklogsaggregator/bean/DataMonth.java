package io.tools.trellobacklogsaggregator.bean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class DataMonth {

    @Max(12)
    @Min(1)
    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
