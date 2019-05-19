package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Neighbourhood;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.NeighbourhoodService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;


import static java.lang.Integer.parseInt;


@WebServlet(name = "NeighbourCoverageServlet")
public class NeighbourCoverageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //查看所有有效线路
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        NeighbourhoodService service = new NeighbourhoodService();
        Gson gson = new Gson();

        int measure = parseInt(request.getParameter("measure"));
        List<Neighbourhood> neighbourhoods = service.queryCoverage(measure);
        String neighbourhoodsInfo = gson.toJson(neighbourhoods);
        System.out.println(neighbourhoodsInfo);
        out.write(neighbourhoodsInfo);

        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
