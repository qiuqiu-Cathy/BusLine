package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.service.LineService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

public class LineQueryByNameServlet extends HttpServlet {
    //根据下拉框选择的线路ID对线路以及其站点进行展示
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        LineService service = new LineService();
        Gson gson = new Gson();

        String lineName = request.getParameter("lineName");
        Line line = service.queryLineByNameAll(lineName);
        if(line!=null){
            //System.out.println(coord);
            Coordinates c = changeIntoCoord(line.getCoord());
            Line lineInfo = new Line(line.getId(),line.getLineName(),c,line.getStops());
            String lineJson = gson.toJson(lineInfo);
            System.out.println(lineJson);
            out.write(lineJson);
        }else{
            out.write("false");
        }
        out.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

}