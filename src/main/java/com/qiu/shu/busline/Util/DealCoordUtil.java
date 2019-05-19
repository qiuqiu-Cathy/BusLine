package com.qiu.shu.busline.Util;

//import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiu.shu.busline.domain.Coordinates;
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
