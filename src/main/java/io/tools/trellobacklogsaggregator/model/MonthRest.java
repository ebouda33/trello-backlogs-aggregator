package io.tools.trellobacklogsaggregator.model;

import java.util.ArrayList;
import java.util.List;

public class MonthRest {

    private String label;
    private int length;
    private int firstDay;
    private int year;
    private int index;
    private List<WeekRest> weekRestList;

    public MonthRest(String label, int index, int length, int firstDay, int year) {
        this.label = label;
        this.length = length;
        this.firstDay = firstDay;
        this.year = year;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }

    public void addWeek(int firstDay, int lastDay, int index, int indexFirstDay, int indexLastDay) {
        if (weekRestList == null) {
            weekRestList = new ArrayList<>();
        }
        weekRestList.add(new WeekRest(firstDay, lastDay, index, indexFirstDay, indexLastDay));
    }

    public List<WeekRest> getWeekRestList() {
        return weekRestList;
    }
}
