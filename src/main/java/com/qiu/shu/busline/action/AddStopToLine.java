package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.Util.DealCoordUtil;
import com.qiu.shu.busline.Util.DealStopUtil;
import com.qiu.shu.busline.domain.*;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

//修改线路-添加该站点至线路
//添加新站点：根据type识别，根据name；loc；status=2；busLine由LineID查询获得进行station表的更新
//添加原有站点或更新新站点以后，根据sequence对line进行更新
//更改status为4
//根据line的ID重新查询线路并反馈给前端页面
//public class AddStopToLine extends HttpServlet {
public class AddStopToLine {
    public static void main(String[] args) {
        Gson gson = new Gson();
        LineService lineService = new LineService();
        StationService stationService = new StationService();

        //String json = "{\"type\":\"newStop\",\"lineID\":\"20190506-121927\",\"name\":\"明二黄先生墓站\",\"loc\":\"31.325215, 121.213961\",\"sequence\":\"3\"}";
        String json = "{\"type\":\"oldStop\",\"lineID\":\"20190506-121927\",\"name\":\"泰众路泰丰路(公交站)\",\"loc\":\"\",\"sequence\":\"9\"}";
        Stop stopObj = gson.fromJson(json,Stop.class);
        Line line = lineService.queryLineByID(stopObj.getLineID());
        Station station = stationService.queryStationByName("安亭地铁站(公交站)");
        String Loc = DealCoordUtil.revise(station.getLocation());
        System.out.println(Loc);
        //List<List<Double>> loc = gson.fromJson(line.getCoord(),ArrayList.class);
        //List<Stop> loc = gson.fromJson(line.getStops(),ArrayList.class);
        List<Double> loc = gson.fromJson(station.getLocation(),ArrayList.class);
        System.out.println(loc.get(0)+" "+loc.get(1));


        //System.out.println(addStation.getId()+addStation.getLocation());
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson=new Gson();
        PrintWriter out = response.getWriter();
        LineService lineService = new LineService();
        StationService stationService = new StationService();

        String obj = request.getParameter("obj");//从前端获取json
        //{"type":"newStop","lineID":"20190506-121927","name":"明二黄先生墓站","loc":"31.325215, 121.213961","sequence":"3"}
        //{"type":"oldStop","lineID":"20190506-121927","name":"泰众路泰丰路(公交站)","loc":"","sequence":"9"}
        System.out.println(obj);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO Auto-generated method stub
        doGet(request, response);
    }
}
