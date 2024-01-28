package com.example.ca1_mainscreen;

import java.util.List;

public class CheckboxContainer {

    private String containerId;
    private List<CheckboxData> checkboxDataList;
    private long timestamp;
    private String receivedText;

    public String getReceivedText() {
        return receivedText;
    }

    public void setReceivedText(String receivedText) {
        this.receivedText = receivedText;
    }

    // Empty constructor for Firebase
    public CheckboxContainer() {
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public CheckboxContainer(String containerId, List<CheckboxData> checkboxDataList,String receivedText) {
        this.containerId = containerId;
        this.checkboxDataList = checkboxDataList;
        this.receivedText = receivedText;
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
