package com.example.ca1_mainscreen.Model;

import java.util.List;

public class HabitModel {
    public static final int STATUS_COMPLETED = 1;
    public static final int STATUS_INCOMPLETE = 0;
    private int id, status;
    private String habit;
    private List<String> days;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public boolean isMondaySelected() {
        return days.contains("Monday");
    }

    public boolean isTuesdaySelected() {
        return days.contains("Tuesday");
    }

    public boolean isWednesdaySelected() {
        return days.contains("Wednesday");
    }

    public boolean isThursdaySelected() {
        return days.contains("Thursday");
    }

    public boolean isFridaySelected() {
        return days.contains("Friday");
    }

    public boolean isSaturdaySelected() {
        return days.contains("Saturday");
    }

    public boolean isSundaySelected() {
        return days.contains("Sunday");
    }

    public String getDaysAsString() {
        StringBuilder daysString = new StringBuilder();
        if (isMondaySelected()) {
            daysString.append("Monday ");
        }
        if (isTuesdaySelected()) {
            daysString.append("Tuesday ");
        }
        if (isWednesdaySelected()) {
            daysString.append("Wednesday ");
        }
        if (isThursdaySelected()) {
            daysString.append("Thursday ");
        }
        if (isFridaySelected()) {
            daysString.append("Friday ");
        }
        if (isSaturdaySelected()) {
            daysString.append("Saturday ");
        }
        if (isSundaySelected()) {
            daysString.append("Sunday ");
        }
        return daysString.toString().trim();
    }
}
