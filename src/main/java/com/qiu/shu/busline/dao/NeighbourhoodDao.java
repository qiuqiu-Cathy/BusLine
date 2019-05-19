package com.qiu.shu.busline.dao;

import com.qiu.shu.busline.domain.Neighbourhood;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NeighbourhoodDao {
    private static final String URL = "jdbc:mysql://localhost:3306/bus";
    private static final String USERNAME = "busRoot";
    private static final String PWD = "12345678";

    //查询 查询所有有效的公交线路的所有字段信息
    public static List<Neighbourhood> queryAllNeighbourhood() {
        List<Neighbourhood> neighbourhoods = new ArrayList<Neighbourhood>();
        Neighbourhood neighbourhood = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            //a.导入驱动，加载具体驱动类
            Class.forName("com.mysql.jdbc.Driver");
            //b.与数据库建立连接
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            //c.发送sql,执行(查)
            String sql = "select * from neighbourhood";
            pstmt = connection.prepareStatement(sql);
            //pstmt.setString(1, "1");
            //执行sql语句[查]
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            //d.处理结果
            while (rs.next()) {
                String id = rs.getString("id");
                String neighborName = rs.getString("neighborName");
                String location = rs.getString("location");
                int number = rs.getInt("number");
                neighbourhood = new Neighbourhood(id,neighborName,location,number);
                neighbourhoods.add(neighbourhood);
            }
            return neighbourhoods;
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
}
