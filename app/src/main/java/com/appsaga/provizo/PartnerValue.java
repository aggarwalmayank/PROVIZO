package com.appsaga.provizo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PartnerValue implements Serializable {

    private String companyName,truckStatus;
    private HashMap<String,HashMap<String,HashMap<String,Long>>> locationMap;

    public PartnerValue(String companyName, HashMap<String,HashMap<String,HashMap<String,Long>>> locationMap,String truckStatus) {
        this.companyName = companyName;
        this.locationMap = locationMap;
        this.truckStatus=truckStatus;
    }

    public PartnerValue() {

    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public HashMap<String,HashMap<String,HashMap<String,Long>>> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(HashMap<String,HashMap<String,HashMap<String,Long>>> locationMap) {
        this.locationMap = locationMap;
    }

    public String getTruckStatus() {
        return truckStatus;
    }

    public void setTruckStatus(String truckStatus) {
        this.truckStatus = truckStatus;
    }
}
