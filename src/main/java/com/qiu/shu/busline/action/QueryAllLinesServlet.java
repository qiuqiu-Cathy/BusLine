package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.service.LineService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;


@WebServlet(name = "QueryAllLinesServlet")
public class QueryAllLinesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //查看所有有效线路
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        LineService service = new LineService();
        List<Line> lines = service.queryAllValidLine();
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


//        System.out.println(lines);
//        request.setAttribute("lines", lines);
//        //因为request域中有数据，因此需要通过请求转发的方式跳转 （重定向会丢失request域）
//        //pageContext<request<session<application
//        request.getRequestDispatcher( "lineManagement.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}

