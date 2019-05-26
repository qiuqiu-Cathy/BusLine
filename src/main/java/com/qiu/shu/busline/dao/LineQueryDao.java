package com.qiu.shu.busline.dao;

import com.qiu.shu.busline.domain.Line;
import com.qiu.shu.busline.domain.Stop;

import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.qiu.shu.busline.Util.DBUtil.rs;

//模型层：用于处理公交线路有关的数据库增删改查操作 （用于查询数据库）
public class LineQueryDao {
    private static final String URL = "jdbc:mysql://localhost:3306/bus";
    private static final String USERNAME = "busRoot";
    private static final String PWD = "12345678";

    //通过输入完整的！！！线路名，返回该线路的stops字符串
    // 更改！！从原先线路名查到所有有效线路，改成查到所有有效、在建、新建完成的线路
    public static String queryLineByNameStops(String lineName) {
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select stops from line where lineName = ?  and (status = 1 or status = 2 or status = 3) ";
            pstmt = connection.prepareStatement(sql);
            String query = lineName;
            pstmt.setString(1,query );
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            String stop;
            if (rs.next()) {
                stop = rs.getString("stops");
                return stop;
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

    //通过输入完整的！！！线路名，返回该线路的所有字段
    // 查到所有有效、在建、新建完成的线路
    public static Line queryLineByNameAll(String lineName) {
        PreparedStatement pstmt = null;
        Connection connection = null;
        Line line = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select * from line where lineName = ?  and status in (1,2,3,4,5) ";
            pstmt = connection.prepareStatement(sql);
            String query = lineName;
            pstmt.setString(1,query );
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("lineName");
                String coord = rs.getString("coord");
                String stops = rs.getString("stops");
                int status = rs.getInt("status");
                line = new Line(id,name,coord,stops,status);
                return line;
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

    //通过输入线路名，模糊查询，返回所有查询到线路的的stops字符串List
    public static List<String> queryLineLikeNameStops(String lineName) {
        List<String> stopsList = new ArrayList<String>();
        String stops ;
        //Line line = null;
        //ArrayList<String> stops = new ArrayList<String>();
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select stops from line where lineName like ?  and status = 1 ";
            pstmt = connection.prepareStatement(sql);
            String query = "%"+lineName+"%";
            pstmt.setString(1,query );
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            while(rs.next()) {
                stops = rs.getString("stops");
                stopsList.add(stops);
            }
            return stopsList;
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

    //查询 通过线路名称查询公交coords
    public static ArrayList<String> queryLineByName(String lineName) {
        ArrayList<String> coords = new ArrayList<String>();
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            //a.导入驱动，加载具体驱动类
            Class.forName("com.mysql.jdbc.Driver");
            //b.与数据库建立连接
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            //c.发送sql,执行(查)
            String sql = "select coord from line where lineName like ?  ";
            pstmt = connection.prepareStatement(sql);
            String query = "%"+ lineName+ "%";
            pstmt.setString(1,query );
            //执行sql语句[查]
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            //d.处理结果
            while (rs.next()) {
                String coord = rs.getString("coord");
                coords.add(coord);
            }
            return coords;
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

    //查询 通过线路ID查询公交的所有有效信息，返回Line
    public static Line queryLineByID(String lineID) {
        Line line = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select * from line where id = ?  ";
            pstmt = connection.prepareStatement(sql);
            String query = lineID;
            pstmt.setString(1, query);
            //执行sql语句[查]
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            //d.处理结果
            if (rs.next()) {
                String id = rs.getString("id");
                String lineName = rs.getString("lineName");
                String coord = rs.getString("coord");
                String stops = rs.getString("stops");
                line = new Line(id, lineName, coord, stops, 1);
            }
            return line;
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



    //查询 通过线路id查询公交线路的所有信息，查看该公交线路是否有效，有效的话返回该公交线路的所有信息，否则返回null
    //更改！！将原先status从1改为1,2,3
    //更改！！将原先status从1,2,3改为1,2,3,4,5
    public Line queryLineById(String lineId) {
        PreparedStatement pstmt = null;
        Connection connection = null;
        Line line = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select * from line where id = ? and (status = 1 or status = 2 or status = 3 or status = 4 or status = 5) "; //aaa
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, lineId);
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            if(rs.next()) {
                String id = rs.getString("id");
                String lineName = rs.getString("lineName");
                String coord = rs.getString("coord");
                String stops = rs.getString("stops");
                int status = rs.getInt("status");
                line = new Line(id,lineName,coord,stops,status);
            }
            return line;
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

    //根据公交的ID查询此公交线路是否有效
    public boolean isExistByID(String id) {//true:此人存在  false:不存在
        return queryLineById(id)==null? false:true  ;
    }

    //根据完整的!!!线路名查询此公交线路是否已经存在
    public boolean isExistByName(String lineName){
        return queryLineByNameAll(lineName)==null? false:true ;
    }

    //更新 通过id选择来使公交线路设置为无效达到删除的效果(实则是更新操作，更新status属性)
    public boolean deleteLineById(String id){
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "update line set `status`='0' where id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            int count  = pstmt.executeUpdate();//返回是否更新成功
            if(count>0)
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

    //更新 通过lineName来确定修改线路，将传入的coords更新至该line
    public boolean updateCoordsByLineName(String lineName,String coords){
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "update line set `coord`= ? where lineName = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, coords);
            pstmt.setString(2,lineName);
            int count  = pstmt.executeUpdate();//返回是否更新成功
            if(count>0)
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

    //更新 通过lineID来确定线路，修改线路的status
    public boolean correctStatus(String lineID,String STATUS){
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "update line set `status`= ? where id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, STATUS);
            pstmt.setString(2,lineID);
            int count  = pstmt.executeUpdate();//返回是否更新成功
            if(count>0)
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

    //更新 通过线路ID来更新线路的status为3(新建完成)
    public boolean updateLineStatusById(String id){
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "update line set `status`='3' where id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            int count  = pstmt.executeUpdate();//返回是否更新成功
            if(count>0)
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

    //更新 通过公交完整线路名，以及更新改造完成的coord，stops，来修改line
    public boolean updateLineByNameCoordStops(String name,String coord,String stops){
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "update line set coord = ? , stops = ? where lineName = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, coord);
            pstmt.setString(2,stops);
            pstmt.setString(3,name);
            int count  = pstmt.executeUpdate();//返回是否更新成功
            if(count>0)
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




    //public static int id = 99999999;


    //新增，通过新增线路-输入的线路名，新增线路
    public static int newLineByNameUpdate(String newLineName) {
        //id ++;
        Date t =new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String id = df.format(t);
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "insert into line values(?,?,?,?,?)";
            pstmt = connection.prepareStatement(sql);//预编译
            pstmt.setString(1, id);
            pstmt.setString(2,newLineName);
            pstmt.setString(3,null);
            pstmt.setString(4,null);
            pstmt.setInt(5,2);
            int count = pstmt.executeUpdate();//返回值表示增删改几条数据
            // d.处理结果
            if (count > 0) {
                System.out.println("操作成功！");
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

    //改！！原先是status为1的所有有效线路，现在是status为1和3和5 包含了新建完毕以及修改完毕的线路
    //查询 查询所有有效的公交线路的所有字段信息 status=1
    public static List<Line> queryAllValidLine() {
        List<Line> lines = new ArrayList<Line>();
        Line line = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select * from line where (status = 1 or status = 3 or status = 5)";
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            //d.处理结果
            while (rs.next()) {
                String id = rs.getString("id");
                String lineName = rs.getString("lineName");
                String coord = rs.getString("coord");
                String stops = rs.getString("stops");
                int status = rs.getInt("status");
                line = new Line(id,lineName,coord,stops,status);
                lines.add(line);
            }
            return lines;
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

    //根据所给状态值反馈该状态下的所有公交线路
    public static List<Line> queryLineByStatus(String s) {
        int statusNum = Integer.parseInt(s);
        List<Line> lines = new ArrayList<Line>();
        Line line = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select id,lineName,coord,stops from line where status = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, statusNum);
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            //d.处理结果
            while (rs.next()) {
                String id = rs.getString("id");
                String lineName = rs.getString("lineName");
                String coord = rs.getString("coord");
                String stops = rs.getString("stops");
                line = new Line(id,lineName,coord,stops,statusNum);
                lines.add(line);
            }
            return lines;
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

    //查询 查询所有在建的公交线路的所有字段信息 status=2
    public static List<Line> queryUnderConstructLine() {
        List<Line> lines = new ArrayList<Line>();
        Line line = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select id,lineName,coord,stops from line where status = 2 ";
            pstmt = connection.prepareStatement(sql);
            //pstmt.setString(1, "2");
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            while (rs.next()) {
                String id = rs.getString("id");
                String lineName = rs.getString("lineName");
                String coord = rs.getString("coord");
                String stops = rs.getString("stops");
                line = new Line(id,lineName,coord,stops,2);
                lines.add(line);
            }
            return lines;
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


    //查询 查询所有正在修改的公交线路的所有字段信息 status=4
    public static List<Line> queryCorrectingLine() {
        List<Line> lines = new ArrayList<Line>();
        Line line = null;
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PWD);
            String sql = "select id,lineName,coord,stops from line where status = 4 ";
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();//返回值表示增删改几条数据
            while (rs.next()) {
                String id = rs.getString("id");
                String lineName = rs.getString("lineName");
                String coord = rs.getString("coord");
                String stops = rs.getString("stops");
                line = new Line(id,lineName,coord,stops,4);
                lines.add(line);
            }
            return lines;
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
