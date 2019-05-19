package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//在新建线路-开始按钮时出发的功能  展现目前所有有效的站点 并把新建的线路名传至后台，进行新建线路的数据库新增！！
public class AddStopStartServlet extends HttpServlet {
    //private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson=new Gson();
        PrintWriter out = response.getWriter();
        StationService service = new StationService();
        LineService lineService = new LineService();

        String newLineName = request.getParameter("newLineName");

        int count = lineService.newLineByNameUpdate(newLineName);//根据新建线路名-进行新建线路
        if(count==1){
            System.out.println("新增操作成功");
            List<Station> stations = service.queryAllValidStation();
            String info=gson.toJson(stations);
            out.write(info);
            //System.out.println(stations);
        }else if(count==0){
            out.write("0");
            System.out.println("新增操作失败");
        }else if(count==-1){
            out.write("2");
            System.out.println("该线路已存在！");
        }

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO Auto-generated method stub
        doGet(request, response);
    }

}
