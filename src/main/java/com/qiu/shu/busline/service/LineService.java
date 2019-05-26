package com.qiu.shu.busline.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiu.shu.busline.Util.DealCoordUtil;
import com.qiu.shu.busline.Util.DealStopUtil;
import com.qiu.shu.busline.dao.LineQueryDao;
import com.qiu.shu.busline.domain.Coord;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.domain.Stop;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LineService {
    LineQueryDao lineQueryDao = new LineQueryDao();

    //status = 1(在运营),3(新建完毕),5(修改完毕)
    public List<Line> queryAllValidLine(){
        return lineQueryDao.queryAllValidLine();
    }

    public List<Line> queryLineByStatus(String status){
        return lineQueryDao.queryLineByStatus(status);
    }
    //status=4 正在修改的所有线路
    public List<Line> queryCorrectingLine(){
        return lineQueryDao.queryCorrectingLine();
    }

    //返回所有处于在建状态的线路列表
    public List<Line> queryUnderConstructLine(){
        return lineQueryDao.queryUnderConstructLine();
    }

    public ArrayList<String> queryLineByName(String lineName){
        return lineQueryDao.queryLineByName(lineName);
    }

    public Line queryLineByID(String lineID){
        return lineQueryDao.queryLineById(lineID);
    }

    public boolean deleteLineById(String id){
        if(lineQueryDao.isExistByID(id)) {
            return lineQueryDao.deleteLineById(id);
        }
        return false ;
    }

    //更新 通过线路ID来更新线路的status为3(新建完成)
    public  boolean updateLineStatusById(String ID){
        return lineQueryDao.updateLineStatusById(ID);
    }

    //通过输入完整的！！！线路名，返回该线路的stops字符串
    public String queryLineByNameStops(String lineName){
        return lineQueryDao.queryLineByNameStops(lineName);
    }

    //通过输入线路名，模糊查询，返回所有查询到线路的的stops字符串List
    public  List<String> queryLineLikeNameStops(String lineName){
        return lineQueryDao.queryLineLikeNameStops(lineName);
    }

    public int newLineByNameUpdate(String newLineName){
        if(!lineQueryDao.isExistByName(newLineName)) { //如果根据输入的完整线路名查询，线路不存在的话再进行新建
            return lineQueryDao.newLineByNameUpdate(newLineName);
        }else{
            return -1; //表示该线路已经存在
        }
    }

    //通过完整线路名查询到该线路所有字段
    public  Line queryLineByNameAll(String lineName){
        return lineQueryDao.queryLineByNameAll(lineName);
    }

    //通过完整线路ID 将线路status变为1；stops中所包含所有站点遍历一遍若是有status为2的站点重新设置为1
    public Line busOnLine(String lineID){
        Gson gson = new Gson();
        StationService stationService = new StationService();

        if(lineQueryDao.correctStatus(lineID,"1")){
            Line line = lineQueryDao.queryLineById(lineID);
            Type stopType = new TypeToken<ArrayList<Stop>>() {}.getType();
            ArrayList<Stop> originStops = gson.fromJson(line.getStops(), stopType);
            for(Stop stop:originStops){
                Station station = stationService.queryStationByName(stop.getName());
                if(station.getStatus()==2){
                    stationService.updateStatus(station.getId(),1);
                }
            }
            return line;
        }else{
            return null;
        }
    }

    public boolean updateLineByNameCoordStops(String lineName,String coord, String stops){
        return lineQueryDao.updateLineByNameCoordStops(lineName,coord,stops);
    }

    //根据完整线路名称及更新后的coord去更新line
    public boolean updateCoordsByLineName(String lineName, String coords){
        return lineQueryDao.updateCoordsByLineName(lineName,coords);
    }

    public boolean correctStatus(String lineID,String status){
        return  lineQueryDao.correctStatus(lineID,status);
    }

    //修改线路-添加站点至线路
    public Line addStopToLine(Stop addStop){
        //System.out.println("addStop"+addStop.getType()+" "+addStop.getLineID()+" "+addStop.getName()+" "+addStop.getLoc()+" "+addStop.getSequence());
        LineService lineService = new LineService();
        StationService stationService = new StationService();
        Gson gson = new Gson();
        Line resultLine = null;

        Line line = lineService.queryLineByID(addStop.getLineID());//获取需要修改的公交线路信息
        String originCoordJson = line.getCoord();
        //System.out.println("原始coord："+originCoordJson);
        String originStopsJson = line.getStops();
        //System.out.println("原始站点："+originStopsJson);
        Type coordType = new TypeToken<List<List<Double>>>(){}.getType();
        List<List<Double>> originCoord = gson.fromJson(originCoordJson, coordType);

        Type stopType = new TypeToken<ArrayList<Stop>>() {}.getType();
        ArrayList<Stop> originStops = gson.fromJson(line.getStops(), stopType);

        List<List<Double>> resultCoord = new ArrayList<List<Double>>();
        List<Stop> resultStops = new ArrayList<Stop>();
        Station addStation = null;

        if(addStop.getType().equals("newStop")){
            addStation = DealStopUtil.newStation(addStop);
        }else{
            addStation = stationService.queryStationByName(addStop.getName());
        }
        Type locType = new TypeToken<List<Double>>() {}.getType();
        List<Double> addLoc = gson.fromJson(addStation.getLocation(),locType);
        //System.out.println("addLoc:"+addLoc);
        Stop addThisStop = new Stop(addStation.getId(),addStation.getStationName(),addLoc,addStop.getSequence());
        //System.out.println("addThisStop:"+addThisStop.getId()+" "+addThisStop.getSequence());
        int seq = Integer.parseInt(addStop.getSequence());
        //System.out.println("seq："+seq+"，originStop.size()："+originStops.size());
        if(seq==1){//修改站点为首站点
            //System.out.println("开始修改-添加首站点到线路");
            for(int i=0; i<originCoord.size()+1;i++){
                if(i==0){
                    resultCoord.add(0,addLoc);
                }else{
                    resultCoord.add(i,originCoord.get(i-1));
                }
            }
            for (int i=0;i<originStops.size()+1;i++){
                if(i==0){
                    resultStops.add(0,addThisStop);
                }else {
                    resultStops.add(i,originStops.get(i-1));
                    resultStops.get(i).setSequence(i+1+"");
                }
            }
        }else if(seq==originStops.size()+1){//修改站点为尾站
            //System.out.println("开始修改-添加尾站点到线路");
            //System.out.println("originCoordSize:"+originCoord.size()+"    "+originCoord.get(0)+"addLoc"+addLoc);
            for(int i=0;i<originCoord.size();i++){
                //System.out.println("第"+i+"次添加coord");
                resultCoord.add(i,originCoord.get(i));
                //System.out.println("添加的coord为"+resultCoord.get(i));
            }
            //System.out.println("1::"+(originCoord.size()+1) + "   "+addLoc+"bug!!!在下方！！！！！");
            resultCoord.add(addLoc);
            //System.out.println("resultCoord.size():"+resultCoord.size()+"   "+resultCoord.get(originCoord.size()));
            //System.out.println("Coord修改完毕开始修改stops");
            //System.out.println("originStops.size()"+originStops.size()+"  例如"+originStops.get(0));
            for(int i=0;i<originStops.size();i++){
                resultStops.add(i,originStops.get(i));
                //System.out.println("i:"+i);
            }
            //System.out.println("resultStop添加原有站点完毕，开始添加末尾stops的信息：bug!!!");
            //System.out.println(addThisStop.getId()+" "+addThisStop.getSequence()+"  "+addThisStop.getName());
            resultStops.add(addThisStop);
            //System.out.println("resultStops.size():"+resultStops.size()+"   "+resultStops.get(originStops.size()));
        }else if(seq>1 && seq<=originStops.size()){ //修改站点为中间站点
            //System.out.println("开始修改-添加中间站点到线路");
            int correctIndex = seq - 1;
            //System.out.println("correctIndex"+correctIndex);
            Stop preStop = gson.fromJson(gson.toJson(originStops.get(correctIndex-1)),Stop.class);
            //System.out.println(preStop.getId()+" "+preStop.getSequence());
            Stop nextStop = originStops.get(correctIndex);
            int preIndexInCoord = DealCoordUtil.returnCoordNumByStop(originCoord,preStop);
            int nextIndexInCoord = DealCoordUtil.returnCoordNumByStop(originCoord,nextStop);
            int j = 0;
            //System.out.println("preIndex"+ preIndexInCoord);
            for(int i=0;i<originCoord.size();i++){
                if(i< preIndexInCoord){
                    //System.out.println("i<pre:"+j+" "+originCoord.get(i));
                    resultCoord.add(j,originCoord.get(i));
                    j ++;
                }else if(i==preIndexInCoord){
                    //System.out.println("i=pre:"+j);
                    resultCoord.add(j,originCoord.get(i));
                    resultCoord.add(j+1,addLoc);
                    j = j+2;
                }else if(i>=nextIndexInCoord){
                    //System.out.println("i>next:"+j);
                    resultCoord.add(j,originCoord.get(i));
                    j++;
                }
            }
            for(int m=0;m<originStops.size()+1;m++){
                if(m<correctIndex){
                    resultStops.add(m,originStops.get(m));
                }if(m==correctIndex){
                    resultStops.add(m,addThisStop);
                }if(m>correctIndex){
                    resultStops.add(m,originStops.get(m-1));
                    resultStops.get(m).setSequence(m+1+"");
                }
            }
        }
        String resultCoordJson = gson.toJson(resultCoord);
        String resultStopsJson = gson.toJson(resultStops);
        if(lineService.updateLineByNameCoordStops(line.getLineName(),resultCoordJson,resultStopsJson)){
            System.out.println("线路更新成功");
            resultLine = lineService.queryLineByID(line.getId());
        }
        if(resultLine==null){
            System.out.println("添加站点至线路失败！！");
        }
        if(resultCoord.size()==0){
            System.out.println("获得resultCoord失败");
        }
        if(resultStops.size()==0){
            System.out.println("获得resultStops失败");
        }
        return resultLine;
    }
}
