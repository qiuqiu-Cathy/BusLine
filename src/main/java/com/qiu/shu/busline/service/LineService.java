package com.qiu.shu.busline.service;

import com.qiu.shu.busline.dao.LineQueryDao;
import com.qiu.shu.busline.domain.Line;

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
}
