package de.rampro.activitydiary.ui.plan;

public class Plan {
    private int year;
    private int month;
    private int dayOfMonth;
    private String time;
    private String title;

    public Plan(int year, int month, int dayOfMonth, String time, String title) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.time = time;
        this.title = title;
    }

    public Plan() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
