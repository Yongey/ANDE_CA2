package com.example.ca1_mainscreen;

public class UserProgress {
    private int progressBarValue;
    private boolean[] radioButtonStates;

    public UserProgress() {
        // Default constructor required for calls to DataSnapshot.getValue(UserProgress.class)
    }

    public UserProgress(int progressBarValue, boolean[] radioButtonStates) {
        this.progressBarValue = progressBarValue;
        this.radioButtonStates = radioButtonStates;
    }

    public int getProgressBarValue() {
        return progressBarValue;
    }

    public boolean[] getRadioButtonStates() {
        return radioButtonStates;
    }
}
