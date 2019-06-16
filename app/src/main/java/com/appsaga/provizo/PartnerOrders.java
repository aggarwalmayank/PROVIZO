package com.appsaga.provizo;

import java.io.Serializable;
import java.util.HashMap;

public class PartnerOrders implements Serializable {

    private String DropLocation;
    private String PickUpDate;
    private String PickUpLocation;
    private HashMap<String, String> Consignee;
    private HashMap<String, String> Consignor;
    private HashMap<String, String> ServiceTruckDetails;
    String key;

    public PartnerOrders(String dropLocation,  String pickUpDate, String pickUpLocation,  HashMap<String, String> consignee, HashMap<String, String> consignor, HashMap<String, String> serviceTruckDetails) {
        DropLocation = dropLocation;
        PickUpDate = pickUpDate;
        PickUpLocation = pickUpLocation;
        Consignee = consignee;
        Consignor = consignor;
        ServiceTruckDetails = serviceTruckDetails;
    }

    public PartnerOrders(PartnerOrders bookings, String key) {
        DropLocation = bookings.getDropLocation();

        PickUpDate = bookings.getPickUpDate();
        PickUpLocation = bookings.getPickUpLocation();
        Consignee = bookings.getConsignee();
        Consignor = bookings.getConsignor();
        ServiceTruckDetails = bookings.getServiceTruckDetails();
        this.key = key;
    }

    public PartnerOrders() {

    }


    public String getDropLocation() {
        return DropLocation;
    }

    public void setDropLocation(String dropLocation) {
        DropLocation = dropLocation;
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



    public HashMap<String, String> getConsignee() {
        return Consignee;
    }

    public void setConsignee(HashMap<String, String> consignee) {
        Consignee = consignee;
    }

    public HashMap<String, String> getConsignor() {
        return Consignor;
    }

    public void setConsignor(HashMap<String, String> consignor) {
        Consignor = consignor;
    }

    public HashMap<String, String> getServiceTruckDetails() {
        return ServiceTruckDetails;
    }

    public void setServiceTruckDetails(HashMap<String, String> serviceTruckDetails) {
        ServiceTruckDetails = serviceTruckDetails;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}