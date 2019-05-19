package com.qiu.shu.busline.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Point;
import com.qiu.shu.busline.service.LineService;
import com.sun.org.apache.bcel.internal.generic.RET;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Floyd {


    private static final LineService lineService = new LineService();

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        List<Point> points = PointUtil.getPointsFromStation();
        points = PointUtil.getPointsFromCoord(points);
        Map<String, Double> disMap = getDisTanceMap(points);
        disMap = floyd(points.size(),disMap);
        System.out.println(disMap.size());
    }


    private static Map<String, Double> floyd(int count, Map<String, Double> disMap) {
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                for (int k = 0; k < count; k++) {

                    double jk = getDisFromDisMap(j, k, disMap);
                    double ji = getDisFromDisMap(j, i, disMap);
                    double ik = getDisFromDisMap(i, k, disMap);
                    if (jk > ji + ik) {
                        addDis(j, k, ji + ik, disMap);
                    }
                }
            }
        }
        return disMap;
    }




    private static Map<String, Double> getDisTanceMap(List<Point> points) {
        Map<String, Double> disMap = new HashMap<String, Double>();
        List<Line> lines = lineService.queryAllValidLine();
        for (Line line : lines) {
            addDisByLine(points, line, disMap);
        }
        return disMap;
    }


    private static void addDisByLine(List<Point> points, Line line, Map<String, Double> disMap) {
        List<List<Double>> list = gson.fromJson(line.getCoord(), ArrayList.class);
        double prex = list.get(0).get(0);
        double prey = list.get(0).get(1);
        int preIndex = PointUtil.getIndexFromPoints(points, prex, prey);
        for (int i = 1; i < list.size(); i++) {
            double curX = list.get(i).get(0);
            double curY = list.get(i).get(1);
            int curIndex = PointUtil.getIndexFromPoints(points, curX, curY);
            if (curIndex == -1 ){
                continue;
            }
            double distance = getDis(prex, prey, curX, curY);
            addDis(preIndex, curIndex, distance, disMap);
            prex = curX;
            prey = curY;
            preIndex = curIndex;
        }
    }

    private static double getDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private static void addDis(int index1, int index2, double dis, Map<String, Double> disMap) {
        int smallIndex = index1 < index2 ? index1 : index2;
        int bigIndex = index1 > index2 ? index1 : index2;
        disMap.put(smallIndex + "-" + bigIndex, dis);
    }

    private static double getDisFromDisMap(int index1, int index2, Map<String, Double> disMap) {
        int smallIndex, bigIndex;
        if (index1 < index2) {
            smallIndex = index1;
            bigIndex = index2;
        } else {
            smallIndex = index2;
            bigIndex = index1;
        }
        String str = smallIndex + "-" + bigIndex;
        if (disMap.containsKey(str)) {
            return disMap.get(str);
        }
        return Double.MAX_VALUE;
    }
}
