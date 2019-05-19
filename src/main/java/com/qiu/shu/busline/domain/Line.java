package com.qiu.shu.busline.domain;

import com.google.gson.Gson;

public class Line {
    private String id;
    private String lineName;
    private String coord;
    private String stops;
    private int status;
    private String coordinates;


    private static final Gson GSON = new Gson();

    /*public Line(String lineName) {
        this.lineName = lineName;
    }*/

    public Line(String id, String lineName, String coord, String stops, int status) {
        this.id = id;
        this.lineName = lineName;
        this.coord = coord;
        this.stops = stops;
        this.status = status;
    }

    public Line(String id, String lineName, String coord, String stops) {
        this.id = id;
        this.lineName = lineName;
        this.coord = coord;
        this.stops = stops;
    }

    //构造可以以不是String类型的coordinates进行构造，但是，传递给前端一定是字符串形式，前端才会转成json接收
    public Line(String id, String lineName, Coordinates coordinates, String stops) {
        this.id = id;
        this.lineName = lineName;
        this.coordinates = GSON.toJson(coordinates);
        this.stops = stops;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getCoord() {
        return coord;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCoordinates() {
        return coordinates;
    }


}
