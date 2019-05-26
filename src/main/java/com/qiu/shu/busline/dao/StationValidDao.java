package com.qiu.shu.busline.dao;

import com.qiu.shu.busline.domain.Station;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StationValidDao {

    private static final String URL = "jdbc:mysql://localhost:3306/bus?useUnicode=true&characterEncoding=UTF-8";
    private static final String USERNAME = "busRoot";
    private static final String PWD = "12345678";

    public  List<Station> queryAllValidStation() { //模型层：用于处理查询站点在覆盖率 （用于查询数据库）显示有效的站点
        List<Station> stations = new ArrayList<Station>();
        Station station = null;
        // String station = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            //a.导入驱动，加载具体驱动类
            Class.forName("com.mysql.jdbc.Driver");
            //b.与数据库建立连接
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            //c.发送sql,执行(查)
            String sql = "select * from station where status = 1 ";
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            //d.处理结果
            while (rs.next()) {
                String id = rs.getString("id");
                String stationName = rs.getString("stationName");
                String location = rs.getString("location");
                int status = rs.getInt("status");
                station = new Station(id,stationName,location,status);
                stations.add(station);
            }
            return stations;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public  List<Station> queryAllStation() { //用于返回所有status 为1（在用）2（新建）所有站点列表
        List<Station> stations = new ArrayList<Station>();
        Station station = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select * from station where (status = 1 or status =2)";
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            //d.处理结果
            while (rs.next()) {
                String id = rs.getString("id");
                String stationName = rs.getString("stationName");
                String location = rs.getString("location");
                int status = rs.getInt("status");
                station = new Station(id,stationName,location,status);
                stations.add(station);
            }
            return stations;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //根据站点名（后面没加（公交名）也可查询到）查询得到该站点的所有信息
    public  Station queryStationByName(String name) {
        Station station = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select * from station where stationName like ? and (status = 1 or status =2 ) ";
            pstmt = connection.prepareStatement(sql);
            String query = name +"%";
            pstmt.setString(1,query);
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            if(rs.next()) {
                String id = rs.getString("id");
                String stationName = rs.getString("stationName");
                String location = rs.getString("location");
                String busLines = rs.getString("busLines");
                int status = rs.getInt("status");
                station = new Station(id,stationName,location,busLines,status);
                return station;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //新增，通过新增线路-输入新站点名，loc对站点进行新增
    public static int newStationByNameUpdate(String name, String loc,String busLines) { //绿环路春浓路 31.289421, 121.244366 1
        name = name.trim().replace("(公交站)","")+"(公交站)";
        loc = "[" + loc.replace(" ","") + "]";
        Date t =new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String id = df.format(t);
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "insert into station values(?,?,?,?,?)";
            pstmt = connection.prepareStatement(sql);//预编译
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3,loc);
            pstmt.setString(4,busLines);
            pstmt.setInt(5,2);
            int count = pstmt.executeUpdate();//返回值表示增删改几条数据
            // d.处理结果
            if (count > 0) {
                System.out.println("新增站点操作成功！");
            }
            return count;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    //根据站点ID以及想要更改的站点状态的值，来对站点进行更改
    public boolean updateStatus(String stationID, int s) {

        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "update station set `status`= ? where id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, s);
            pstmt.setString(2,stationID);
            int count = pstmt.executeUpdate();//返回是否更新成功
            if (count > 0)
                return true;
            else
                return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
