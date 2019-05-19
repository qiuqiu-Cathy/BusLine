package com.qiu.shu.busline.action;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Stop;
import com.qiu.shu.busline.service.LineService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//import net.sf.json.JSONObject;


import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

public class LineQueryDetailServlet extends HttpServlet {
    public static void main(String[] args) {
            //数据库存储的stops即为jsonArray字符串 jsonArray转List<Stop>
            Gson gson = new Gson();
            LineService service = new LineService();

            String stopsJson = service.queryLineByNameStops("嘉定118路(南翔北火车站--顺达路科盛路)");
            System.out.println(stopsJson);
		 	String str = "[{\"id\":\"BV10028550\",\"name\":\"南翔北火车站\",\"location\":[31.281912,121.310611],\"sequence\":\"1\"},{\"id\":\"BV10029452\",\"name\":\"银翔路星华路\",\"location\":[31.283194,121.311874],\"sequence\":\"2\"},{\"id\":\"BV10028549\",\"name\":\"银翔路扬子路\",\"location\":[31.285885,121.316338],\"sequence\":\"3\"}]";
            List<Stop> stops = gson.fromJson(stopsJson, new TypeToken<List<Stop>>() {}.getType());//对于不是类的情况，用这个参数给出
            for (Stop stop : stops) {
                System.out.println(stop.getId()+" "+stop.getName()+" "+stop.getLocation()+" "+stop.getSequence());
            }
	 }

//    //还存在万一前端输入的名字线路不存在，前端alert不能正确显示区别线路是否存在 dao层需修改
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受前端传来的查询公交线路名
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        LineService service = new LineService();
        Gson gson = new Gson();

        String lineName = request.getParameter("queryLineName");
        String stops = service.queryLineByNameStops(lineName);
        if(stops!=null){
            String stopsInfo = gson.toJson(stops);
            System.out.println(stopsInfo);
            out.write(stopsInfo);
        } else{
            out.write("false");
        }
        out.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
