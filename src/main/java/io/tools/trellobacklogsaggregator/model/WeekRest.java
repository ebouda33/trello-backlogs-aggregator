package io.tools.trellobacklogsaggregator.model;

import java.time.LocalDate;

public class WeekRest {

    private int firstDay;
    private String firsDayValue;
    private int indexFirstDay;
    private int lastDay;
    private String lastDayValue;
    private int indexLastDay;
    private int index;
    private int month;
    private int year;

    public WeekRest(int firstDay, int lastDay, int index, int indexFirstDay, int indexLastDay, int month, int year) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.index = index;
        this.indexFirstDay = indexFirstDay;
        this.indexLastDay = indexLastDay;
        this.firsDayValue = String.format("%02d", firstDay);
        this.lastDayValue = String.format("%02d", lastDay);
        this.month = month;
        this.year = year;
    }

    public int getIndex() {
        return index;
    }

    public int getIndexFirstDay() {
        return indexFirstDay;
    }

    public int getIndexLastDay() {
        return indexLastDay;
    }

    public int getFirstDay() {
        return firstDay;
    }

    public String getFirstDayTxt() {
        return String.format("%02d", firstDay);
    }

    public String getLastDayTxt() {
        return String.format("%02d", lastDay);
    }

    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    public String getFirsDayValue() {
        return firsDayValue;
    }

    public String getLastDayValue() {
        return lastDayValue;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public LocalDate getLocalFirstDay() {
        return LocalDate.parse(this.year + "-" + String.format("%02d",this.month) + "-" + this.getFirstDayTxt());
    }
    public LocalDate getLocalLastDay() {
        int zeMonth = lastDay < firstDay ? month+1: month;
        return LocalDate.parse(this.year + "-" + String.format("%02d",zeMonth) + "-" + this.getLastDayTxt());
    }

    public boolean isIn(LocalDate day){
        return day.getMonthValue() == month && day.getYear() == year && day.getDayOfMonth() >= this.getFirstDay() && day.getDayOfMonth() <= this.getLastDay();
    }

    @Override
    public String toString() {
        return String.format("Sem: du %s au %s", firsDayValue, lastDayValue);
    }
}
