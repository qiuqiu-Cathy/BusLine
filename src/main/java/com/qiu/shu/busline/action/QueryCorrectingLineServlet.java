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
import java.util.ArrayList;
import java.util.List;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

//查询status为4，即正在修改中的线路列表
public class QueryCorrectingLineServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        LineService service = new LineService();
        List<Line> lines = service.queryCorrectingLine();
        Gson gson=new Gson();
        PrintWriter out = response.getWriter();

        List<Line> linesResult = new ArrayList<Line>();
        Line lineCoordinates =  null;
        for(Line line:lines) {
            Coordinates coordinates = changeIntoCoord(line.getCoord());
            lineCoordinates = new Line(line.getId(),line.getLineName(),coordinates,line.getStops());
            linesResult.add(lineCoordinates);
        }
        String buslineInfo=gson.toJson(linesResult);
        out.write(buslineInfo);
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
