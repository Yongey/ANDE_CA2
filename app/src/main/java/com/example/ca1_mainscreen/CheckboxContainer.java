package com.example.ca1_mainscreen;

import java.util.List;

public class CheckboxContainer {

    private String containerId;
    private List<CheckboxData> checkboxDataList;

    // Empty constructor for Firebase
    public CheckboxContainer() {
    }

    public CheckboxContainer(String containerId, List<CheckboxData> checkboxDataList) {
        this.containerId = containerId;
        this.checkboxDataList = checkboxDataList;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public List<CheckboxData> getCheckboxDataList() {
        return checkboxDataList;
    }

    public void setCheckboxDataList(List<CheckboxData> checkboxDataList) {
        this.checkboxDataList = checkboxDataList;
    }
}
