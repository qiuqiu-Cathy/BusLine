package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.qiu.shu.busline.Util.DealCoordUtil;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.domain.Stop;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;


//接受前端传来的Json{"type":"oldStop","lineName":"嘉定测试1路","name":"鸵鸟路民丰路(公交站) ","location":"31.311132,121.143509","sequence":"1"}
//{"type":"oldStop","lineName":"嘉定测试2路","name":"鸵鸟路民丰路(公交站) ","location":"","sequence":"1"}
//根据前端传来的Json中的lineName找到需要更新的线路，根据name找到添加站点的id，更新线路的coord，stops两列
//将更新后的line的coord和stops打包成Json传递给前端，使其画出线路，并且标注出站点
public class AddOldStopToLineServlet extends HttpServlet {

//    public static void main(String[] args) {
//        Gson gson=new Gson();
//        StationService stationService = new StationService();
//        LineService lineService = new LineService();
//        String coord = null;
//        String stops = null;
//
//        String str = "{\"type\":\"oldStopEND\",\"lineName\":\"嘉定10101010路\",\"name\":\"鸵鸟路民丰路(公交站) \",\"loc\":\"\",\"sequence\":\"1\"}";
//        Stop stopJson = gson.fromJson(str,Stop.class);
//        System.out.println(stopJson.getType()+" "+stopJson.getLineName()+" "+stopJson.getName()+" "+stopJson.getLoc()+" "+stopJson.getSequence());
//        //根据新增原有站点的名字，获取该站点的所有信息
//
//        System.out.println(stopJson.getType().equals("oldStopEND"));
//        System.out.println(stopJson.getType()=="oldStopEND");
//        Station station = stationService.queryStationByName(stopJson.getName().trim());
//        System.out.println(station.getId()+" "+station.getStationName()+" "+station.getLocation());
//        //根据修改线路的名字获取线路目前的所有字段
//        Line line = lineService.queryLineByNameAll(stopJson.getLineName());
//        //Line lines = lineService.queryLineByNameAll("嘉定115路(南翔北火车站--公交南翔站)");
//        //Line line = lineService.queryLineByNameAll("嘉定115路(南翔北火车站--公交南翔站)");
//       //[[31.281874,121.310634],[31.28204,121.310549]]  ,   null
//       //[{"id":"BV10028550","name":"南翔北火车站","location":[31.281874,121.310634],"sequence":"1"},{"id":"BV10029452","name":"银翔路星华路","location":[31.283224,121.31189],"sequence":"2"}]   ,  null
//        String stopString = "{\"id\":\"" + station.getId()+"\",\"name\":\"" + station.getStationName().replace("(公交站)","") + "\",\"location\":" + station.getLocation() + ",\"sequence\":\"" + stopJson.getSequence()+"\"}";
//        if(line.getCoord()==null||line.getCoord()==""){
//            coord = "["+ station.getLocation()+"]";
//            stops = "[" + stopString + "]";
////            System.out.println(coord);
////            System.out.println(lines.getCoord());
////            System.out.println(stops);
////            System.out.println(lines.getStops());
//        }else{
//            coord = "[" + DealCoordUtil.revise(line.getCoord()) + "," + station.getLocation()+"]";
//            stops = "[" + DealCoordUtil.revise(line.getStops()) + "," + stopString +"]";
////            System.out.println(coord);
////            System.out.println(lines.getCoord());
////            System.out.println(stops);
////            System.out.println(lines.getStops());
//        }
//        System.out.println(lineService.updateLineByNameCoordStops(stopJson.getLineName().trim(),coord,stops));//更新成功则返回true
//        //!!!!存在bug！！！未更新station中的buslines！！！！
//        if(lineService.updateLineByNameCoordStops(stopJson.getLineName().trim(),coord,stops)){
//            Line lineInfo = lineService.queryLineByID(line.getId());
//            if(lineInfo!=null){
//                Coordinates c = changeIntoCoord(lineInfo.getCoord());
//                Line lineData = new Line(lineInfo.getId(),lineInfo.getLineName(),c,lineInfo.getStops());
//                String lineJson = gson.toJson(lineData);
//                System.out.println(lineJson);
//                //out.write(lineJson);
//            }else{
//                //out.write("false");
//            }
//            //out.close();
//        }
//    }

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

}
