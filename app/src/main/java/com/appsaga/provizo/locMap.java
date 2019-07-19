package com.appsaga.provizo;

import java.util.HashMap;

public class locMap {

    String source;
    String dest;
    Long price;

    public locMap(String source, String dest, Long price) {
        this.source = source;
        this.dest = dest;
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
