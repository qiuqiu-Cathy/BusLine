package com.qiu.shu.busline.domain;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String id;
    private String stationName;
    private String location;
    private String busLines;
    private int status;

    private double x; //站点的经度
    private double y; //站点的纬度

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Station(String location,String id){
        List<Double> loc = new ArrayList<Double>();
        int index = location.indexOf(',');
        this.x = Double.parseDouble(location.substring(1, index));
        this.y = Double.parseDouble(location.substring(index+1,location.length()-1));
        this.id = id;
    }

    public Station(String id, String stationName, String location, int status) {
        this.id = id;
        this.stationName = stationName;
        this.location = location;
        this.status = status;
    }

    public Station(String id, String stationName, String location,String busLines ,int status) {
        this.id = id;
        this.stationName = stationName;
        this.location = location;
        this.busLines =busLines;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusLines() {
        return busLines;
    }

    public void setBusLines(String busLines) {
        this.busLines = busLines;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
