package com.qiu.shu.busline.Util;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Point;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import java.util.ArrayList;
import java.util.List;

public class PointUtil {


    private static final LineService lineService = new LineService();

    private static final StationService stationService = new StationService();

    private static final Gson gson = new Gson();

    public static List<Point> getPointsFromStation() {
        //获取所有status为1(在用)2(新建)的List<Station>
        List<Station> stationInfos = stationService.queryAllStaion();
        List<Station> stations = new ArrayList<Station>();//{x,y,id}
        for (Station station : stationInfos) {
            Station astation = new Station(station.getLocation(), station.getId());
            stations.add(astation);
        }
//        for(Station station:stations){
//            System.out.println(station.getX()+" "+station.getY()+" "+station.getId());
//        }
        //根据Line中的coord信息来构造List<Point>
        // 1.将所有的List<Station> station 转换成List<Point> point
        List<Point> points = new ArrayList<Point>(); //{x,y,staionID,isStation}
        for (Station station : stations) { //将所有有效的station转变成Point
            Point point = new Point(station);
            points.add(point);
        }
        return points;
    }

    public static List<Point> getPointsFromCoord(List<Point> points) {
        List<Line> lines = lineService.queryAllValidLine();
        for (Line line : lines) {
            String coord = line.getCoord();
            List<List<Double>> list = gson.fromJson(coord, ArrayList.class);
            for (List<Double> node : list) {
                double x = node.get(0);
                double y = node.get(1);
                addPointIfNotExist(points, x, y);
            }
        }
        return points;
    }


    private static void addPointIfNotExist(List<Point> points, double x, double y) {
        if (isExisted(points, x, y)) {
        } else {
            Point p = new Point(x, y);
            points.add(p);
        }

    }

    private static boolean isExisted(List<Point> points, double x, double y) {
        for (Point p : points) {
            if (Double.compare(p.getX(), x) == 0 && Double.compare(p.getY(), y) == 0) {
                return true;
            }
        }
        return false;
    }


    public static int getStationPointCount(List<Point> points) {
        int count = 0;
        for (Point p : points) {
            if (p.getIsStation() == 1) {
                count++;

            }
        }
        return count;
    }

    public static int getIndexFromPoints(List<Point> points, double x, double y) {
        for (int i = 0; i < points.size(); i++) {
            if (isEqual(points.get(i), x, y)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isEqual(Point p, double x, double y) {
        if (Double.compare(p.getX(), x) == 0 && Double.compare(p.getY(), y) == 0) {
            return true;
        }
        return false;

    }

}
