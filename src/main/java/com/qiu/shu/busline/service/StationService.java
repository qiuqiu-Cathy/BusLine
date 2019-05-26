package com.qiu.shu.busline.service;

import com.qiu.shu.busline.dao.LineQueryDao;
import com.qiu.shu.busline.dao.StationValidDao;
import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Station;

;
import java.util.List;

public class StationService {
    StationValidDao stationValidDao = new StationValidDao();
    LineQueryDao lineQueryDao = new LineQueryDao();
    public List<Station> queryAllValidStation(){ //status=1
        return stationValidDao.queryAllValidStation();
    }

    public List<Station> queryAllStaion(){//status=1, status=2
        return stationValidDao.queryAllStation();
    }

    public Station queryStationByName(String name){
        return stationValidDao.queryStationByName(name);
    }

    public boolean updateStatus(String stationID,int status){
        return stationValidDao.updateStatus(stationID,status);
    }

    public int newStationByNameUpdate(String stationName,String loc,String lineName){
        Line line = lineQueryDao.queryLineByNameAll(lineName);
        String busLines = "[{\"id\":\"" + line.getId() + "\",\"name\":\"" + line.getLineName() + "\"}]";
        //[{"id":"310100017097","name":"上嘉线(上海南站(南广场)--嘉定客运中心)"}]
        return stationValidDao.newStationByNameUpdate(stationName,loc,busLines);
    }
}
