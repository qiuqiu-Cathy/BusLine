package com.qiu.shu.busline.Util;

import java.util.ArrayList;
import java.util.List;

public class DealNeighbourUtil {

    public static List<Float> locationIntoFloatList(String str){
        //将居民区location中的31.11,121.00类似字符串提取出两个float数字，并转为121.00,31.11这样的list存储
        int index = str.indexOf(',');
        String f1_str = str.substring(0, index);
        String f2_str = str.substring(index+1,str.length());
        List<Float> flist = new ArrayList<Float>();
        flist.add(Float.parseFloat(f2_str));
        flist.add(Float.parseFloat(f1_str));
        return flist;

    }
}
