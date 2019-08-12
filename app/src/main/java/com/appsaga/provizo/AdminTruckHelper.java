package com.appsaga.provizo;

import java.io.Serializable;
import java.util.HashMap;

public class AdminTruckHelper implements Serializable {
    private String DropLocation;
    private String PaymentStatus;
    private String PickUpDate;
    private String PickUpLocation;
    private String TruckCompany;
    private String goodsRisk;
    private String user;

    private String DeliveryMode;
    private String amount;
    private HashMap<String, String> Consignee;
    private HashMap<String, String> Consignor;
    private HashMap<String, String> ServiceTruckDetails;
    String key;

    public AdminTruckHelper(String user,String dropLocation, String paymentStatus, String pickUpDate, String pickUpLocation, String truckCompany, String amount, HashMap<String, String> consignee,
                    HashMap<String, String> consignor, HashMap<String, String> serviceTruckDetails,String goodsrisk,String deliverymode) {
        DropLocation = dropLocation;
        PaymentStatus = paymentStatus;
        PickUpDate = pickUpDate;
        PickUpLocation = pickUpLocation;
        TruckCompany = truckCompany;
        this.amount = amount;
        this.user=user;
        Consignee = consignee;
        Consignor = consignor;
        DeliveryMode=deliverymode;
        goodsRisk=goodsrisk;
        ServiceTruckDetails = serviceTruckDetails;
    }



    public AdminTruckHelper(AdminTruckHelper bookings, String key) {
        DropLocation = bookings.getDropLocation();
        PaymentStatus = bookings.getPaymentStatus();
        PickUpDate = bookings.getPickUpDate();
        this.user=bookings.getUser();
        PickUpLocation = bookings.getPickUpLocation();
        TruckCompany = bookings.getTruckCompany();
        this.amount = bookings.getAmount();
        Consignee = bookings.getConsignee();
        DeliveryMode=bookings.getDeliveryMode();
        goodsRisk=bookings.getGoodsRisk();
        Consignor = bookings.getConsignor();
        ServiceTruckDetails = bookings.getServiceTruckDetails();
        this.key = key;
    }

    public AdminTruckHelper() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getGoodsRisk() {
        return goodsRisk;
    }

    public void setGoodsRisk(String goodsRisk) {
        this.goodsRisk = goodsRisk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDeliveryMode() {
        return DeliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        DeliveryMode = deliveryMode;
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
