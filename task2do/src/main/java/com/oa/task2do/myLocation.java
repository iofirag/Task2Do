package com.oa.task2do;

/**
 * Created by joe on 20/02/14.
 */
public class myLocation {
    Double latitude;
    Double longitude;



    public myLocation(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }
    public Double getLongtitude() {
        return longitude;
    }

    public void setLongitude(Double longtitude) {
        this.longitude = longtitude;
    }
    public void setLatitude(Double latitude) {

        this.latitude = latitude;
    }
}
