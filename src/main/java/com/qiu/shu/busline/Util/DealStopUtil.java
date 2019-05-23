package com.qiu.shu.busline.Util;

import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Station;
import com.qiu.shu.busline.domain.Stop;
import com.qiu.shu.busline.service.LineService;
import com.qiu.shu.busline.service.StationService;

public class DealStopUtil {
    public static void main(String[] args) {
		 	String str = "[{\"id\":\"BV10029335\",\"name\":\"鹤旋路金运路\",\"location\":[31.238602,121.320995],\"sequence\":\"1\"},{\"id\":\"BV10326668\",\"name\":\"金沙江西路华江路\",\"location\":[31.240938,121.328606],\"sequence\":\"2\"},{\"id\":\"BV10641845\",\"name\":\"沙河路鹤旋东路\",\"location\":[31.23842,121.332649],\"sequence\":\"3\"},{\"id\":\"BV10641846\",\"name\":\"丰华路天创路\",\"location\":[31.233368,121.329521],\"sequence\":\"4\"},{\"id\":\"BV10641844\",\"name\":\"爱特路金运路\",\"location\":[31.232912,121.32],\"sequence\":\"5\"},{\"id\":\"BV10641843\",\"name\":\"爱特路金园一路\",\"location\":[31.232908,121.313751],\"sequence\":\"6\"},{\"id\":\"BV10029643\",\"name\":\"鹤芳路金园一路\",\"location\":[31.235613,121.313316],\"sequence\":\"7\"},{\"id\":\"BV10381302\",\"name\":\"金耀南路鹤芳路\",\"location\":[31.237478,121.311279],\"sequence\":\"8\"},{\"id\":\"BV10029323\",\"name\":\"金耀路鹤旋路\",\"location\":[31.239302,121.311821],\"sequence\":\"9\"},{\"id\":\"BV10029645\",\"name\":\"鹤霞路星华公路\",\"location\":[31.244682,121.30896],\"sequence\":\"10\"},{\"id\":\"BV10029646\",\"name\":\"鹤霞路金耀路\",\"location\":[31.245665,121.312675],\"sequence\":\"11\"},{\"id\":\"BV10029647\",\"name\":\"鹤霞路金园一路\",\"location\":[31.246588,121.318176],\"sequence\":\"12\"}]";
//		 	String json = changeIntoJson(str);
//		 	System.out.println(json);
	 }

	 public static Station newStation(Stop stop){
		 LineService lineService = new LineService();
		 StationService stationService = new StationService();
		 Line line = lineService.queryLineByID(stop.getLineID());
		 stationService.newStationByNameUpdate(stop.getName(),stop.getLoc(),line.getLineName());
		 Station station = stationService.queryStationByName(stop.getName());
		 return station;
	 }
}
