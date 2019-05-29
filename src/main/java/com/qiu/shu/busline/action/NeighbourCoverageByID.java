package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Neighbourhood;
import com.qiu.shu.busline.service.NeighbourhoodService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.Integer.parseInt;

public class NeighbourCoverageByID extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //查看所有有效线路
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        NeighbourhoodService service = new NeighbourhoodService();
        Gson gson = new Gson();

        String queryObj = request.getParameter("obj");//从前端获取json
        Neighbourhood queryJson = gson.fromJson(queryObj,Neighbourhood.class);
        //System.out.println(queryJson.getId()+ " "+queryJson.getMeasure());
        //根据查询的居民区ID，及查询距离，反馈该居民区在此距离内站点个数
        Neighbourhood result = service.queryCoverageByID(queryJson.getId(),queryJson.getMeasure());
        System.out.println("该范围内小区附近的站点个数："+result.getNumber());
        String neighbourhoodsInfo = gson.toJson(result);

        out.write(neighbourhoodsInfo);
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}