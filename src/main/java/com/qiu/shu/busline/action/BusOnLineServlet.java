package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Stop;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

public class BusOnLineServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson=new Gson();
        PrintWriter out = response.getWriter();
        LineService lineService = new LineService();
        StationService stationService = new StationService();

        String lineID = request.getParameter("queryLineID");//从前端获取json
        //System.out.println(lineID);

        Line lineInfo = lineService.busOnLine(lineID);
        if(lineInfo!=null){
            Coordinates c = changeIntoCoord(lineInfo.getCoord());
            Line lineData = new Line(lineInfo.getId(),lineInfo.getLineName(),c,lineInfo.getStops());
            String lineJson = gson.toJson(lineData);
            //System.out.println("修改线路-添加线路至站点得到的lineJson"+lineJson);
            out.write(lineJson);
        }else{
            System.out.println("线路投入运营失败！");
        }
        out.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO Auto-generated method stub
        doGet(request, response);
    }
}