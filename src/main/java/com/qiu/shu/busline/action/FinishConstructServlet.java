package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

//完成新建 修改线路status状态为3
public class FinishConstructServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson=new Gson();
        PrintWriter out = response.getWriter();
        LineService lineService = new LineService();
        StationService stationService = new StationService();

        String lineName = request.getParameter("lineName");//从前端获取json
        //System.out.println(lineID);

        Line line = lineService.queryLineByNameAll(lineName);
        if(lineService.correctStatus(line.getId(),"3")) {
            Line lineInfo = lineService.queryLineByNameAll(lineName);
            System.out.println("线路名为：" + lineInfo.getLineName() + " 线路状态已经被更新成为" + lineInfo.getStatus());
            if (lineInfo != null) {
                Coordinates c = changeIntoCoord(lineInfo.getCoord());
                Line lineData = new Line(lineInfo.getId(), lineInfo.getLineName(), c, lineInfo.getStops());
                String lineJson = gson.toJson(lineData);
                //System.out.println("修改线路-添加线路至站点得到的lineJson"+lineJson);
                out.write(lineJson);
            } else {
                System.out.println("未获取到新建成功线路的内容");
            }
        }else{
            System.out.println("新建线路失败！");
        }
        out.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO Auto-generated method stub
        doGet(request, response);
    }
}