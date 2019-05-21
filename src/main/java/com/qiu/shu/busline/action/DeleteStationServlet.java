package com.qiu.shu.busline.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.domain.Stop;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.qiu.shu.busline.Util.DealCoordUtil.changeIntoCoord;


//接受前端传来的Json{"lineID":"20190506-121927","name":"园区路园汽路","sequence":"1"}
//{"lineID":"20190506-121927","name":"园区路百安公路","sequence":"2"}
//{"lineID":"20190506-121927","name":"泰裕路泰丰路","sequence":"10"}
//根据前端传来的Json中的lineID找到需要更新的线路，返回该线路目前的所有字段，根据stationName找到需要修改站点的ID等信息
//通过站点的ID找到线路stops中的定位，删除该段stops并修改其后的sequence等值
//删除 删除站点与前后站点之间的coord 特判删除的是否是第一站或是最后一个站点
//将更新后的line的coord和stops打包成Json传递给前端，使其画出线路，并且标注出站点
//更改该线路的status为"4"
public class DeleteStationServlet
//        extends HttpServlet
{
    public static void main(String[] args) {
        Gson gson=new Gson();
        StationService stationService = new StationService();
        LineService lineService = new LineService();

        String str = "{\"lineID\":\"20190506-121927\",\"name\":\"园区路园汽路\",\"sequence\":\"1\"}";
        Stop stopJson = gson.fromJson(str,Stop.class);
        System.out.println(stopJson.getLineID()+" "+stopJson.getName() +" "+stopJson.getSequence());

        //根据修改线路的名字获取线路目前的所有字段
        Station station = stationService.queryStationByName(stopJson.getName().trim());
        System.out.println(station.getId()+" "+station.getStationName()+" "+station.getLocation());

        //根据Json中的线路ID查询到线路的所有字段
        Line line = lineService.queryLineByID(stopJson.getLineID());

        //获得该线路中stops中包含的stop的具体信息
        Type type = new TypeToken<ArrayList<Stop>>(){}.getType();
        ArrayList<Stop> stopsList = gson.fromJson(line.getStops(),type);
        int sequence = 0;//删除站点的序号
        //System.out.println(station.getId()+"!!!");
        for(Stop stop: stopsList){
            //System.out.println(stop.getName()+" " +stop.getSequence()+" "+stop.getId());
            if(stop.getId().equals(station.getId())){
                sequence = Integer.parseInt(stop.getSequence());
                System.out.println("需要删除的站点序号是" + sequence +"   该线路总共站点个数为："+ stopsList.size());
                break;
            }
        }
        //通过站点的ID找到线路stops中的定位，删除该段stops并修改其后的sequence等值
        List<Stop> delStops = new ArrayList();
        int index = sequence - 1;//需要删除站点的下标
        if(index>=0 && index < stopsList.size()){
            int j = 0;
            for(int i=0;i<stopsList.size();i++){
                if(i!= index){
                    delStops.add(j,stopsList.get(i));
                    delStops.get(j).setSequence(j+1+"");
                    j++;
                }
            }
        }else{
            System.out.println("删除站点的顺序有误！！！");
        }
        if(index==0){//删除首站点

        }else if(index == stopsList.size()-1){//删除尾站点

        }else if(index > 0 && index< stopsList.size()-1){//删除中间站点

        }


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
    }

//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("utf-8");
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("text/html; charset=UTF-8");
//
//        Gson gson=new Gson();
//        PrintWriter out = response.getWriter();
//        StationService stationService = new StationService();
//        LineService lineService = new LineService();
//        String coord = null;
//        String stops = null;
//
//        String obj = request.getParameter("obj");//从前端获取json
//        System.out.println(obj);
//        Stop stopJson = gson.fromJson(obj,Stop.class);
//        System.out.println(stopJson.getType()+" "+stopJson.getLineName()+" "+stopJson.getName()+" "+stopJson.getLoc()+" "+stopJson.getSequence());
//        //根据新增原有站点的名字，获取该站点的所有信息
//        Station station = stationService.queryStationByName(stopJson.getName().trim()); //获取的公交站点名字的末尾可能会有空格！！
//        System.out.println(station.getId()+" "+station.getStationName()+" "+station.getLocation());
//        //根据修改线路的名字获取线路目前的所有字段
//        Line line = lineService.queryLineByNameAll(stopJson.getLineName());
//        //[[31.281874,121.310634],[31.28204,121.310549]]  ,   null
//        //[{"id":"BV10028550","name":"南翔北火车站","location":[31.281874,121.310634],"sequence":"1"},{"id":"BV10029452","name":"银翔路星华路","location":[31.283224,121.31189],"sequence":"2"}]   ,  null
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
//            if(stopJson.getType().equals("oldStopEND")){
//                if(lineService.updateLineStatusById(line.getId())){
//                    System.out.println("线路状态已修改为新建完成");
//                }else{
//                    System.out.println("线路状态新建完成修改失败！！");
//                }
//            }
//            Line lineInfo = lineService.queryLineByID(line.getId());
//            if(lineInfo!=null){
//                Coordinates c = changeIntoCoord(lineInfo.getCoord());
//                Line lineData = new Line(lineInfo.getId(),lineInfo.getLineName(),c,lineInfo.getStops());
//                String lineJson = gson.toJson(lineData);
//                System.out.println(lineJson);
//                out.write(lineJson);
//            }else{
//                System.out.println("线路更新成功，但未根据线路ID找到线路的具体信息");
//            }
//        }else{
//            out.write("false");
//            System.out.println("线路更新失败");
//        }
//        out.close();
    }
