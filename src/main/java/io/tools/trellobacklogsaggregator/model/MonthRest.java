package io.tools.trellobacklogsaggregator.model;

import io.tools.trellobacklogsaggregator.service.UtilService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MonthRest {

    private String label;
    private int length;
    private int firstDay;
    private int year;
    private String value;
    private int index;
    private List<WeekRest> weekRestList;

    public MonthRest(String label, int index, int length, int firstDay, int year) {
        this.label = label;
        this.length = length;
        this.firstDay = firstDay;
        this.year = year;
        this.index = index;
        this.value = String.format("%02d",index);
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
        weekRestList.add(new WeekRest(firstDay, lastDay, index, indexFirstDay, indexLastDay,this.index, year));
    }

    public List<WeekRest> getWeekRestList() {
        return weekRestList;
    }

    public String getValue() {
        return value;
    }

    public LocalDate getLastDay(){
        final LocalDate parse = LocalDate.parse(year + "-" + String.format("%02d", index) + "-01");
        int length = parse.lengthOfMonth();
        return LocalDate.parse(year + "-" + String.format("%02d", index) + "-"+length);
    }

    public WeekRest getWeekFor(LocalDate day){
        return this.weekRestList.stream().filter(weekRest -> weekRest.isIn(day)).findFirst().orElse(this.weekRestList.get(0));
    }
}
