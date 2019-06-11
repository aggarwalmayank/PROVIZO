package com.appsaga.provizo;

import java.io.Serializable;
import java.util.HashMap;

public class Bookings implements Serializable {

    String DropLocation;
    String PaymentStatus;
    String PickUpDate;
    String PickUpLocation;
    String TruckCompany;
    String amount;
    HashMap<String,String> Consignee;
    HashMap<String,String> Consignor;
    HashMap<String,String> ServiceTruckDetails;

    public Bookings(String dropLocation, String paymentStatus, String pickUpDate, String pickUpLocation, String truckCompany, String amount, HashMap<String, String> consignee, HashMap<String, String> consignor, HashMap<String, String> serviceTruckDetails) {
        DropLocation = dropLocation;
        PaymentStatus = paymentStatus;
        PickUpDate = pickUpDate;
        PickUpLocation = pickUpLocation;
        TruckCompany = truckCompany;
        this.amount = amount;
        Consignee = consignee;
        Consignor = consignor;
        ServiceTruckDetails = serviceTruckDetails;
    }

    public Bookings()
    {

    }

    public String getDropLocation() {
        return DropLocation;
    }

    public void setDropLocation(String dropLocation) {
        DropLocation = dropLocation;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
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

    public String getTruckCompany() {
        return TruckCompany;
    }

    public void setTruckCompany(String truckCompany) {
        TruckCompany = truckCompany;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
}
