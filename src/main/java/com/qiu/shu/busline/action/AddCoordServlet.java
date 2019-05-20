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
        System.out.println(coords);


    }

  /*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        Gson gson=new Gson();
        PrintWriter out = response.getWriter();
        StationService stationService = new StationService();
        LineService lineService = new LineService();
        String coord = null;
        String stops = null;

        String obj = request.getParameter("obj");//从前端获取json
        System.out.println(obj);
        Stop stopJson = gson.fromJson(obj,Stop.class);
        System.out.println(stopJson.getType()+" "+stopJson.getLineName()+" "+stopJson.getName()+" "+stopJson.getLoc()+" "+stopJson.getSequence());
        //根据新增原有站点的名字，获取该站点的所有信息
        Station station = stationService.queryStationByName(stopJson.getName().trim()); //获取的公交站点名字的末尾可能会有空格！！
        System.out.println(station.getId()+" "+station.getStationName()+" "+station.getLocation());
        //根据修改线路的名字获取线路目前的所有字段
        Line line = lineService.queryLineByNameAll(stopJson.getLineName());
        //[[31.281874,121.310634],[31.28204,121.310549]]  ,   null
        //[{"id":"BV10028550","name":"南翔北火车站","location":[31.281874,121.310634],"sequence":"1"},{"id":"BV10029452","name":"银翔路星华路","location":[31.283224,121.31189],"sequence":"2"}]   ,  null
        String stopString = "{\"id\":\"" + station.getId()+"\",\"name\":\"" + station.getStationName().replace("(公交站)","") + "\",\"location\":" + station.getLocation() + ",\"sequence\":\"" + stopJson.getSequence()+"\"}";
        if(line.getCoord()==null||line.getCoord()==""){
            coord = "["+ station.getLocation()+"]";
            stops = "[" + stopString + "]";
//            System.out.println(coord);
//            System.out.println(lines.getCoord());
//            System.out.println(stops);
//            System.out.println(lines.getStops());
        }else{
            coord = "[" + DealCoordUtil.revise(line.getCoord()) + "," + station.getLocation()+"]";
            stops = "[" + DealCoordUtil.revise(line.getStops()) + "," + stopString +"]";
//            System.out.println(coord);
//            System.out.println(lines.getCoord());
//            System.out.println(stops);
//            System.out.println(lines.getStops());
        }
        System.out.println(lineService.updateLineByNameCoordStops(stopJson.getLineName().trim(),coord,stops));//更新成功则返回true
        //!!!!存在bug！！！未更新station中的buslines！！！！
        if(lineService.updateLineByNameCoordStops(stopJson.getLineName().trim(),coord,stops)){
            if(stopJson.getType().equals("oldStopEND")){
                if(lineService.updateLineStatusById(line.getId())){
                    System.out.println("线路状态已修改为新建完成");
                }else{
                    System.out.println("线路状态新建完成修改失败！！");
                }
            }
            Line lineInfo = lineService.queryLineByID(line.getId());
            if(lineInfo!=null){
                Coordinates c = changeIntoCoord(lineInfo.getCoord());
                Line lineData = new Line(lineInfo.getId(),lineInfo.getLineName(),c,lineInfo.getStops());
                String lineJson = gson.toJson(lineData);
                System.out.println(lineJson);
                out.write(lineJson);
            }else{
                System.out.println("线路更新成功，但未根据线路ID找到线路的具体信息");
            }
        }else{
            out.write("false");
            System.out.println("线路更新失败");
        }
        out.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO Auto-generated method stub
        doGet(request, response);
    }
*/
}