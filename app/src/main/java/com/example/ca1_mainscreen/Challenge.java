package com.example.ca1_mainscreen;

import java.util.List;

public class Challenge {
    private String clickedText;
    private List<String> selectedLanguages;

    public Challenge() {
        // Default constructor required for DataSnapshot.getValue(Challenge.class)
    }

    public Challenge(String clickedText, List<String> selectedLanguages) {
        this.clickedText = clickedText;
        this.selectedLanguages = selectedLanguages;
    }

    public String getClickedText() {
        return clickedText;
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }
}