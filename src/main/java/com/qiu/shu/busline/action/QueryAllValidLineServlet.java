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

import static com.qiu.shu.busline.Util.DealCoordUtil.*;

@WebServlet(name = "QueryAllValidLineServlet")
public class QueryAllValidLineServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //查看所有有效线路
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        LineService service = new LineService();
        List<Line> lines = service.queryLineByStatus("1");
        //ArrayList<String> coordsJson = new ArrayList<String>();
        //将从数据库查到的List<Line>中line的coord字段转换成coordinates形式再打包成List<Line>传给前端
//        List<Line> resultLines = new ArrayList<Line>();
//        for(Line line:lines){
//            Coordinates c = changeIntoCoord(line.getCoord());
//            Line lineData = new Line(line.getId(),line.getLineName(),c,line.getStops());
//            resultLines.add(lineData);
//        }
//        String resultLinesJson = gson.toJson(resultLines);
//        out.write(resultLinesJson);
//        out.close();

        if(lines!=null){
            List<Line> linesResult = new ArrayList<Line>();
            Line lineCoordinates =  null;
            for(Line line:lines) {
                Coordinates coordinates = changeIntoCoord(line.getCoord());
                lineCoordinates = new Line(line.getId(),line.getLineName(),coordinates,line.getStops());
                linesResult.add(lineCoordinates);
            }
            request.setAttribute("linesResult", linesResult);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            //response.sendRedirect("lineQuery.jsp");
        }else{
            response.sendRedirect("index.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
