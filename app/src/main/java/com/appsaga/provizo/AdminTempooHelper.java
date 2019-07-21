package com.appsaga.provizo;

import java.io.Serializable;

public class AdminTempooHelper implements Serializable {

    private String Dimension, DropLocation, Status, EstimateAmount, PickUpLocation, User;
String key;
    public AdminTempooHelper(String dimension, String dropLocation, String status, String estimateAmount, String pickUpLocation, String user) {
        Dimension = dimension;
        DropLocation = dropLocation;
        Status = status;
        EstimateAmount = estimateAmount;
        PickUpLocation = pickUpLocation;
        User = user;
    }

    public AdminTempooHelper() {

    }
    public AdminTempooHelper(AdminTempooHelper help, String key) {
        Dimension=help.getDimension();
        DropLocation=help.getDropLocation();
        Status=help.getStatus();
        EstimateAmount=help.getEstimateAmount();
        PickUpLocation=help.getPickUpLocation();
        User=help.getUser();
        this.key=key;
    }

    public String getDimension() {
        return Dimension;
    }

    public void setDimension(String dimension) {
        Dimension = dimension;
    }

    public String getDropLocation() {
        return DropLocation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDropLocation(String dropLocation) {
        DropLocation = dropLocation;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEstimateAmount() {
        return EstimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        EstimateAmount = estimateAmount;
    }

    public String getPickUpLocation() {
        return PickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        PickUpLocation = pickUpLocation;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
