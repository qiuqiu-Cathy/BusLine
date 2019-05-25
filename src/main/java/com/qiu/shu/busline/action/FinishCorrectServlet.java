package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



public class FinishCorrectServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        StationService stationService = new StationService();
        LineService lineService = new LineService();


        String lineID = request.getParameter("lineID");//从前端获取json
        System.out.println(lineID);
        if(lineService.correctStatus(lineID,"5")){
            out.write(1);//修改成功
        }else{
            System.out.println("修改线路-完成修改-线路的状态修改为5出错！！！");
            out.write(0);
        }
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

