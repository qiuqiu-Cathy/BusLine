package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.Util.DealCoordUtil;
import com.qiu.shu.busline.domain.*;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.management.openmbean.ArrayType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;

//{"lineName":"嘉定10101010路","loc":"31.311211, 121.143994","sequence":"1"}
//通过lineName获取需要修改的线路名，得到stops
// 根据sequence+1查到coord插入xxx之后的loc 在coord中找到该站点的loc 将loc插入该站点loc之前
// 把该站点的coord信息以及站点信息重新反馈传给前端画出该线路
public class AddCoordServlet extends HttpServlet {
    /*
    public static void main(String[] args) {
        Gson gson=new Gson();
        StationService stationService = new StationService();
        LineService lineService = new LineService();

        String str = "{\"lineName\":\"嘉定10101010路\",\"loc\":\"31.311211, 121.143994\",\"sequence\":\"1\"}";
        Coord coordJson = gson.fromJson(str, Coord.class);
        int sequence = Integer.parseInt(coordJson.getSequence());
        System.out.println("线路名："+ coordJson.getLineName()+" 插入拐点坐标："+coordJson.getLoc()+" 插入该顺序站点之后"+coordJson.getSequence()+"实际"+ sequence);
        List<Double> addCoordLoc = gson.fromJson("["+ coordJson.getLoc()+"]",ArrayList.class);
        System.out.println("插入coord："+ addCoordLoc.get(0)+" , "+addCoordLoc.get(1));
        //根据修改线路的名字获取线路目前的所有字段

        Line line = lineService.queryLineByNameAll(coordJson.getLineName());
        //System.out.println(line.getCoord());
        List<List<Double>> coords = gson.fromJson(line.getCoord(), ArrayList.class);
        //System.out.println(line.getStops());
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
        if(lineService.updateCoordsByLineName(coordJson.getLineName(),coords+"")){
            Line newLine = lineService.queryLineByNameAll(coordJson.getLineName());
            System.out.println(newLine.getCoord());
        }

    }
*/

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson=new Gson();
        PrintWriter out = response.getWriter();
        LineService lineService = new LineService();

        String obj = request.getParameter("obj");//从前端获取json
        Coord coordJson = gson.fromJson(obj, Coord.class);
        int sequence = Integer.parseInt(coordJson.getSequence());
        System.out.println("线路名："+ coordJson.getLineName()+" 插入拐点坐标："+coordJson.getLoc()+" 插入该顺序站点之后"+coordJson.getSequence()+"实际"+ sequence);
        List<Double> addCoordLoc = gson.fromJson("["+ coordJson.getLoc()+"]",ArrayList.class);
        //根据修改线路的名字获取线路目前的所有字段
        Line line = lineService.queryLineByNameAll(coordJson.getLineName());
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
        if(lineService.updateCoordsByLineName(coordJson.getLineName(),coordsJson)){
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