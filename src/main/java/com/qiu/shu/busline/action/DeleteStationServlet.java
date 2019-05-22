package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiu.shu.busline.Util.DealCoordUtil;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.domain.Stop;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;


//接受前端传来的Json{"lineID":"20190506-121927","name":"园区路园汽路","sequence":"1"}
//{"lineID":"20190506-121927","name":"园区路百安公路","sequence":"2"}
//{"lineID":"20190506-121927","name":"泰裕路泰丰路","sequence":"10"}
//根据前端传来的Json中的lineID找到需要更新的线路，返回该线路目前的所有字段，根据stationName找到需要修改站点的ID等信息
//通过站点的ID找到线路stops中的定位，删除该段stops并修改其后的sequence等值
//删除 删除站点与前后站点之间的coord 特判删除的是否是第一站或是最后一个站点
//将更新后的line的coord和stops打包成Json传递给前端，使其画出线路，并且标注出站点
//更改该线路的status为"4"
public class DeleteStationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        StationService stationService = new StationService();
        LineService lineService = new LineService();

        System.out.println("1");
        String obj = request.getParameter("obj");//从前端获取json
        System.out.println("2"+obj);
        Stop stopJson = gson.fromJson(obj, Stop.class);
        System.out.println(stopJson.getLineID() + " " + stopJson.getName() + " " + stopJson.getSequence());
        //根据修改线路的名字获取线路目前的所有字段
        Station station = stationService.queryStationByName(stopJson.getName().trim());
        System.out.println(station.getId() + " " + station.getStationName() + " " + station.getLocation());
        //根据Json中的线路ID查询到线路的所有字段
        Line line = lineService.queryLineByID(stopJson.getLineID());
        //获得该线路中stops中包含的stop的具体信息
        Type type = new TypeToken<ArrayList<Stop>>() {
        }.getType();
        ArrayList<Stop> stopsList = gson.fromJson(line.getStops(), type);
        int sequence = 0;//用于保存删除站点的序号
        for (Stop stop : stopsList) {
            if (stop.getId().equals(station.getId())) {
                sequence = Integer.parseInt(stop.getSequence());
                System.out.println("需要删除的站点序号是" + sequence + "   该线路总共站点个数为：" + stopsList.size());
                break;
            }
        }
        //通过站点的ID找到线路stops中的定位，删除该段stops并修改其后的sequence等值
        List<Stop> delStops = new ArrayList(); //保留删除站点后的新的stops
        int index = sequence - 1;//需要删除站点的下标
        if (index >= 0 && index < stopsList.size()) {
            int j = 0;
            for (int i = 0; i < stopsList.size(); i++) {
                if (i != index) {
                    delStops.add(j, stopsList.get(i));
                    delStops.get(j).setSequence(j + 1 + "");
                    j++;
                }
            }
            String delStopsString = gson.toJson(delStops);//存储删除后的新的stops字符串，用于存入数据库

            //根据删除站点的位置的不同删除coords
            List<List<Double>> coords = gson.fromJson(line.getCoord(), ArrayList.class);
            Stop preStop = null; //删除站点的前一个站点
            Stop nextStop = null; //删除站点的后一个站点
            Stop delStop = stopsList.get(index);
            if (index == 0) {//删除首站点
                nextStop = stopsList.get(index + 1);
            } else if (index == stopsList.size() - 1) {//删除尾站点
                preStop = stopsList.get(index - 1);
            } else if (index > 0 && index < stopsList.size() - 1) {//删除中间站点
                preStop = stopsList.get(index - 1);
                nextStop = stopsList.get(index + 1);
            }
            int delCoordNum = DealCoordUtil.returnCoordNumByStop(coords, delStop);//需要删除站点在coords中的下标
            int preCoordNum = DealCoordUtil.returnCoordNumByStop(coords, preStop);
            int nextCoordNum = DealCoordUtil.returnCoordNumByStop(coords, nextStop);
            List<List<Double>> delCoords = new ArrayList<List<Double>>();
            int n = 0;//用于存储删除后的delCoords下标
            if (index == 0) {//删除首站点
                delCoords = DealCoordUtil.delFirstStopFromCoords(coords, nextCoordNum);
            } else if (index == stopsList.size() - 1) {//删除尾站点
                delCoords = DealCoordUtil.delLastStopFromCoords(coords, preCoordNum);
            } else if (index > 0 && index < stopsList.size() - 1) {//删除中间站点
                delCoords = DealCoordUtil.delMidStopFromCoords(coords, preCoordNum, nextCoordNum);
            }
            String delCoordsString = gson.toJson(delCoords);
            if(lineService.updateLineByNameCoordStops(line.getLineName().trim(), delCoordsString, delStopsString)&&lineService.correctStatus(line.getId(),"4")) {
                //重新查询该线路线路并将其status改为4
                Line lineInfo = lineService.queryLineByID(line.getId());
                if (lineInfo != null) {
                    Coordinates c = changeIntoCoord(lineInfo.getCoord());
                    Line lineData = new Line(lineInfo.getId(), lineInfo.getLineName(), c, lineInfo.getStops());
                    String lineJson = gson.toJson(lineData);
                    System.out.println(lineJson);
                    out.write(lineJson);
                }
            }else{
                System.out.println("更新删除站点的stops和coords失败");
            }
        } else {
            System.out.println("删除站点的顺序有误！！！");
        }
        out.close();
    }
}

