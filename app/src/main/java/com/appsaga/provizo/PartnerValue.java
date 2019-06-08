package com.appsaga.provizo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PartnerValue implements Serializable {

    String companyName;
    HashMap<String,HashMap<String,Long>> locationMap;

    public PartnerValue(String companyName, HashMap<String, HashMap<String, Long>> locationMap) {
        this.companyName = companyName;
        this.locationMap = locationMap;
    }

    public PartnerValue() {

    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public HashMap<String, HashMap<String, Long>> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(HashMap<String, HashMap<String, Long>> locationMap) {
        this.locationMap = locationMap;
    }
}
