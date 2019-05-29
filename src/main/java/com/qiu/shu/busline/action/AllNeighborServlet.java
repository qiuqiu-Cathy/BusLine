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


public class AllNeighborServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取所有居民区列表
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        NeighbourhoodService neighbourhoodService = new NeighbourhoodService();
        List<Neighbourhood> resultNeighbor = neighbourhoodService.queryAllNeighbor();

        String resultNeighborJson = gson.toJson(resultNeighbor);
        out.write(resultNeighborJson);
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
