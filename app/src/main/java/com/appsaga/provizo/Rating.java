package com.appsaga.provizo;

import java.io.Serializable;

public class Rating implements Serializable {

    private String Review;
    private float star;
    String key;
    public Rating(String review, float star) {
        Review = review;
        this.star = star;
    }

    public Rating(){}
    public  Rating( Rating val){

        Review=val.getReview();
        star=val.getStar();
    }
    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }
}
