package com.qiu.shu.busline.service;

import com.google.gson.Gson;
import com.qiu.shu.busline.Util.DealStopUtil;
import com.qiu.shu.busline.dao.LineQueryDao;
import com.qiu.shu.busline.domain.Coord;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.domain.Stop;

import java.util.ArrayList;
import java.util.List;

public class LineService {
    LineQueryDao lineQueryDao = new LineQueryDao();

    //status = 1(在运营),3(新建完毕),5(修改完毕)
    public List<Line> queryAllValidLine(){
        return lineQueryDao.queryAllValidLine();
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
        LineService lineService = new LineService();
        StationService stationService = new StationService();
        Gson gson = new Gson();
        Line resultLine = null;

        Line line = lineService.queryLineByID(addStop.getLineID());//获取需要修改的公交线路信息
        String originCoordJson = line.getCoord();
        String originStopsJson = line.getStops();
        List<List<Double>> originCoord = gson.fromJson(originCoordJson, ArrayList.class);
        List<Stop> originStops = gson.fromJson(originStopsJson,ArrayList.class);
        List<List<Double>> resultCoord = null;
        List<Stop> resultStops = null;
        Station addStation = null;

        if(addStop.getType().equals("newStop")){
            addStation = DealStopUtil.newStation(addStop);
        }else{
            addStation = stationService.queryStationByName(addStop.getName());
        }
        List<Double> addLoc = gson.fromJson(addStation.getLocation(),ArrayList.class);
        Stop addThisStop = new Stop(addStation.getId(),addStation.getStationName(),addLoc,addStop.getSequence());

        int seq = Integer.parseInt(addStop.getSequence());
        if(seq==1){//修改站点为首站点
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
                }
            }
        }else if(seq==originStops.size()+1){//修改站点为尾站
            for(int i=0;i<originCoord.size();i++){
                resultCoord.add(i,originCoord.get(i));
            }
            resultCoord.add(originCoord.size()+1,addLoc);
            for(int i=0;i<originStops.size();i++){
                resultStops.add(i,originStops.get(i));
            }
            resultStops.add(originStops.size()+1,addThisStop);
        }else if(seq>1 && seq<=originStops.size()){ //修改站点为中间站点

        }
        String resultCoordJson = gson.toJson(resultCoord);
        String resultStopsJson = gson.toJson(resultStops);
        if(lineService.updateLineByNameCoordStops(line.getLineName(),resultCoordJson,resultStopsJson)){
            resultLine = lineService.queryLineByID(line.getId());
        }

        if(resultLine==null||resultCoord==null||resultStops==null){
            System.out.println("添加站点至线路失败！！");
        }
        return resultLine;

    }
}
