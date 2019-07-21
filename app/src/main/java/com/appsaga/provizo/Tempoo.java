package com.appsaga.provizo;

import java.io.Serializable;

public class Tempoo implements Serializable {

    private String Dimension, DropLocation, EstimateAmount, PickUpDate, PickUpLocation,Status;
    String key;

    public Tempoo(String dimension, String dropLocation,String Status, String estimateAmount, String pickUpDate, String pickUpLocation) {
        Dimension = dimension;
        DropLocation = dropLocation;
        this.Status=Status;
        EstimateAmount = estimateAmount;
        PickUpDate = pickUpDate;
        PickUpLocation = pickUpLocation;
    }

    public Tempoo(Tempoo value, String key) {
        Dimension = value.getDimension();
        DropLocation = value.getDropLocation();
        Status=value.getStatus();
        EstimateAmount = value.getEstimateAmount();
        PickUpDate = value.getPickUpDate();
        PickUpLocation = value.getPickUpLocation();
        this.key=key;
    }
    public Tempoo(){}

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    public String getEstimateAmount() {
        return EstimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        EstimateAmount = estimateAmount;
    }

    public String getPickUpDate() {
        return PickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        PickUpDate = pickUpDate;
    }

    public String getPickUpLocation() {
        return PickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        PickUpLocation = pickUpLocation;
    }
}
