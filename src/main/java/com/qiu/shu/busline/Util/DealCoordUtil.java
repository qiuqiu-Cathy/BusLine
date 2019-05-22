package com.qiu.shu.busline.Util;

//import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coordinates;
import com.qiu.shu.busline.domain.Stop;
//import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class DealCoordUtil {

//	 public static void main(String[] args) {
//		 	String str = "[[31.29672,121.321794],[31.292049,121.314194],[31.29672,121.321794],[31.292049,121.314194]]";
//		 	String json = changeIntoJson(str);
//		 	System.out.println(json);
//	 }
	 
	 
	 public static Coordinates changeIntoCoord(String str){
		 List<List<Double>> coods = new ArrayList();
		 String revised = revise(str);
		 String changed = change(revised);
		 String[] cood = changed.split(";");
		 	for(String s : cood){
		 		List<Double> f = changeIntoDoubleList(s);
		 		coods.add(f);
		 	}
		 Coordinates coord = new Coordinates();
		 coord.setCoordinates(coods);
		 coord.setType("LineString");
		 return coord;
	 }

    public static String changeIntoJson(String str){
        List<List<Double>> coods = new ArrayList();
        String revised = revise(str);
        String changed = change(revised);
        String[] cood = changed.split(";");
        for(String s : cood){
            List<Double> f = changeIntoDoubleList(s);
            coods.add(f);
        }
        Coordinates coord = new Coordinates();
        coord.setCoordinates(coods);
        coord.setType("LineString");
        Gson gson = new Gson();
        return gson.toJson(coord);
    }

    //根据站点的Location，返回其在所在线路的coords中的下标
    public static int returnCoordNumByStop(List<List<Double>> coords, Stop stop){
	 	if(stop!=null) {
			String x = stop.getLocation().get(0) + "";
			String y = stop.getLocation().get(1) + "";
			for (int i = 0; i < coords.size(); i++) {
				String cx = coords.get(i).get(0) + "";
				String cy = coords.get(i).get(1) + "";
				if (cx.equals(x) && cy.equals(y)) {
					return i;
				}
			}
			return -1;//表明未在coords中找到该站点的序号
		}else{
	 		return -2;//表明输入的站点为空
		}
	}

	public static List<List<Double>> delFirstStopFromCoords(List<List<Double>> coords,int nextStopIndex){//index默认为0
	 	List<List<Double>> ans = new ArrayList<List<Double>>();
	 	int j = 0;
	 	for (int i=0;i<coords.size();i++){
	 		if(i!=0 && i >= nextStopIndex){
	 			ans.add(j,coords.get(i));
				j++;
			}
		}
	 	return ans;
	}

	public static List<List<Double>> delMidStopFromCoords(List<List<Double>> coords,int preStopIndex, int nextStopIndex){//index默认为0
		List<List<Double>> ans = new ArrayList<List<Double>>();
		int j = 0;
		for (int i=0;i<coords.size();i++){
			if(i<=preStopIndex || i>=nextStopIndex){
				ans.add(j,coords.get(i));
				j++;
			}
		}
		return ans;
	}

	public static List<List<Double>> delLastStopFromCoords(List<List<Double>> coords,int preStopIndex){
		List<List<Double>> ans = new ArrayList<List<Double>>();
		int j = 0;
		for (int i=0;i<coords.size();i++){
			if(i <= preStopIndex){
				ans.add(j,coords.get(i));
				j++;
			}
		}
		return ans;
	}

	 
	 public static String revise(String str){ //去除字符串最外层的[ ]
	 	return  str.substring(1,str.length()-1);
	 }
	 
	 public static String change (String str){ //将经纬度坐标的数组中的以第一个，开始数，每偶数个逗号变成分号分割
		 int cnt = 0;
		 char[] str_char = str.toCharArray();
		 for(int i = 0; i<str_char.length ;i++){
			 char ch = str_char[i];
			 if(ch == ','){
				 cnt++;
				 if(cnt %2 == 0){
					 str_char[i] = ';';
				 }
			 }
		 }
		 return new String(str_char);
	 }

	 //float类型改成了double！！
	 public static List<Double> changeIntoDoubleList(String str){
	 	//将经纬度中的[31.11,121.00]类似的提取出两个float数字，并转为121.00,31.11这样的list存储
		 int index = str.indexOf(',');
		 String f1_str = str.substring(1, index);
		 String f2_str = str.substring(index+1,str.length()-1);
		 List<Double> flist = new ArrayList<Double>();
		 flist.add(Double.parseDouble(f2_str));
		 flist.add(Double.parseDouble(f1_str));
		 return flist;
		 
	 }
}
