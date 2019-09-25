package io.tools.trellobacklogsaggregator.model;

public class WeekRest {

    private int firstDay;
    private int indexFirstDay;
    private int lastDay;
    private int indexLastDay;
    private int index;

    public WeekRest(int firstDay, int lastDay, int index, int indexFirstDay, int indexLastDay) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.index = index;
        this.indexFirstDay = indexFirstDay;
        this.indexLastDay = indexLastDay;
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

    public String getFirstDayTxt(){
        return String.format("%02d", firstDay);
    }
    public String getLastDayTxt(){
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

    @Override
    public String toString() {
        return String.format("Sem: du %s au %s", String.format("%02d", firstDay), String.format("%02d", lastDay));
    }
}
