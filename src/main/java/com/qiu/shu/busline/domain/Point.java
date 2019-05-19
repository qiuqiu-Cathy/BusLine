package com.qiu.shu.busline.domain;

public class Point { //遍历每条线路中的coord中的每一个点，区分其中
    private double x;//点的经度
    private double y;//点的纬度
    private String stationID;//若该点是站点则放入站点ID；否则存null
    private int isStation;//判断该点是否为站点，若是，则1；若只是拐点则0

    public Point(Station station){ //将站点类型转换成点类型
        this.x = station.getX();
        this.y = station.getY();
        this.stationID = station.getId();
        this.isStation = 1;
    }

    public Point(double x , double y ){ //将站点类型转换成点类型
        this.x = x;
        this.y = y;
        this.isStation = 0;
    }



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

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public int getIsStation() {
        return isStation;
    }

    public void setIsStation(int isStation) {
        this.isStation = isStation;
    }
}
