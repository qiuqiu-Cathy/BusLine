package com.qiu.shu.busline.action;

import com.qiu.shu.busline.service.LineService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class DeleteLineByIdServlet  extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        LineService service = new LineService();

        //接收前端传来的删除线路ID
        String id = request.getParameter("id");
        boolean result = service.deleteLineById(id) ;
        if (result) {
            out.write("1");
        }else{
            out.write("0");
        }
        out.close();


//        response.setContentType("text/html; charset=UTF-8");
//        response.setCharacterEncoding("utf-8");
//        if(result) {
////			out.print();
//            //response.getWriter().println("删除成功！");
//            response.sendRedirect("QueryAllLinesServlet");//重新查询 所有线路
//        }else {
//            response.getWriter().println("删除失败！");
//        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
