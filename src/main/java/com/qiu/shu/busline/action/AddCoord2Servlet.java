package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coord;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Line;
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

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

//{"lineID":"20190506-121927","loc":"31.309873, 121.205149","sequence":"9"}
//通过lineID获取需要修改的线路名，得到stops !!和AddCoordServlet的区别
// 根据sequence+1查到coord插入xxx之后的loc 在coord中找到该站点的loc 将loc插入该站点loc之前
// 把该站点的coord信息以及站点信息重新反馈传给前端画出该线路
public class AddCoord2Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson=new Gson();
        PrintWriter out = response.getWriter();
        LineService lineService = new LineService();

        String obj = request.getParameter("obj");//从前端获取json
        //System.out.println(String.format("obj = %s",obj));
        Coord coordJson = gson.fromJson(obj, Coord.class);
        //System.out.println(String.format("coord = %s" , coordJson));
        Line line = lineService.queryLineByID(coordJson.getLineID());
        coordJson.setLineName(line.getLineName());
        System.out.println(coordJson.getLineName());
        int sequence = Integer.parseInt(coordJson.getSequence());
        System.out.println("线路名："+ coordJson.getLineName()+" 插入拐点坐标："+coordJson.getLoc()+" 插入该顺序站点之后"+coordJson.getSequence()+"实际"+ sequence);
        List<Double> addCoordLoc = gson.fromJson("["+ coordJson.getLoc()+"]",ArrayList.class);
        //根据修改线路的名字获取线路目前的所有字段

        List<List<Double>> coords = gson.fromJson(line.getCoord(), ArrayList.class);
        List<Stop> stopsJson = gson.fromJson(line.getStops(),ArrayList.class);
        String addCoordBerforeStop = stopsJson.get(sequence)+"";
        Stop stop = gson.fromJson(addCoordBerforeStop,Stop.class);
        int coordNum = 0; //用于保存找寻到coord插入某个站点之前，这个站点在coord中的顺序
        for(int i=0;i<coords.size();i++){
            String x1 = coords.get(i).get(0) + "";
            String y1 = coords.get(i).get(1) + "";
            String x2 = stop.getLocation().get(0) + "";
            String y2 = stop.getLocation().get(1) + "";
            if(x1.equals(x2) && y1.equals(y2)){
                coordNum = i;
                System.out.println(i);
                break;
            }
        }
        coords.add(coordNum,addCoordLoc); //前端传入 插入第一个站点之后，coordNum则取第二个站点的位置，coord取代该位置插入
        //根据lineName更新数据库line的coord并返回全新的coord及站点信息使前端显示
        String coordsJson = gson.toJson(coords);
        if(lineService.updateCoordsByLineName(coordJson.getLineName(),coordsJson) && lineService.correctStatus(coordJson.getLineID(),"4")){
            Line lineInfo = lineService.queryLineByNameAll(coordJson.getLineName());
            Coordinates c = changeIntoCoord(lineInfo.getCoord());
            Line lineData = new Line(lineInfo.getId(),lineInfo.getLineName(),c,lineInfo.getStops());
            String lineJson = gson.toJson(lineData);
            System.out.println(lineJson);
            out.write(lineJson);
        }else{
            System.out.println("线路插入coords更新失败！！");
        }
        out.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO Auto-generated method stub
        doGet(request, response);
    }

}