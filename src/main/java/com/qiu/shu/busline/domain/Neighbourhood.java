package com.qiu.shu.busline.domain;

public class Neighbourhood {
    private String id;
    private String neighborhoodName;
    private String location;
    private int number;


    public Neighbourhood(String id, String neighborhoodName, String location, int number) {
        this.id = id;
        this.neighborhoodName = neighborhoodName;
        this.location = location;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeighborhoodName() {
        return neighborhoodName;
    }

    public void setNeighborhoodName(String neighborhoodName) {
        this.neighborhoodName = neighborhoodName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}
