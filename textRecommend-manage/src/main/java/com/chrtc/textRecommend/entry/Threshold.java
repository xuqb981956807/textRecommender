package com.chrtc.textRecommend.entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Threshold implements IThreshold{
    @Value("${threshold}")
    private String threshold;

    public String getKmeanskcuster() {
        return kmeanskcuster;
    }

    @Value("${kmeanskcuster}")
    private String kmeanskcuster;

    public String getThreshold(){
        return  threshold;

    }
public void setThreshold(String threshold){
        this.threshold=threshold;
}
}
