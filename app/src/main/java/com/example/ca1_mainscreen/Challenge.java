package com.example.ca1_mainscreen;

import java.util.List;
import java.util.Map;

public class Challenge {
    private String clickedText;
    private List<String> selectedLanguages;
    private Map<String, Boolean> checkedStatus;
    public Challenge() {
        // Default constructor required for DataSnapshot.getValue(Challenge.class)
    }

    public Challenge(String clickedText, List<String> selectedLanguages, Map<String, Boolean> checkedStatus) {
        this.clickedText = clickedText;
        this.selectedLanguages = selectedLanguages;
        this.checkedStatus = checkedStatus;
    }
    public Map<String, Boolean> getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(Map<String, Boolean> checkedStatus) {
        this.checkedStatus = checkedStatus;
    }


    public String getClickedText() {
        return clickedText;
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }
}