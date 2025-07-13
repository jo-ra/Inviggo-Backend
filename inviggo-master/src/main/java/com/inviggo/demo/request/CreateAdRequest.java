package com.inviggo.demo.request;

import com.inviggo.demo.model.Ad;
import lombok.Getter;
import lombok.Setter;


public class CreateAdRequest {

    private Ad ad;

    public Ad getAd() {
        return ad;
    }
    public void setAd(Ad ad) {
        this.ad = ad;
    }

}
