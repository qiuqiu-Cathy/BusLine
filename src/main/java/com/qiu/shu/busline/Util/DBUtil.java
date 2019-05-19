package com.qiu.shu.busline.Util;

/**
 * Created by Yisa on 16/9/8.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类
 * @author
 *
 */

//通用的数据操作方法
public class DBUtil {
    private static final String URL  = "jdbc:mysql://localhost:3306/bus" ;
    private static final String USERNAME  ="busRoot" ;
    private static final String PASSWORD  ="12345678" ;
    public static PreparedStatement pstmt = null ;
    public static Connection connection = null ;
    public static ResultSet rs = null ;

//    //通用的：查询总数
//    public static int getTotalCount(String sql ) {//select count(*) from xx ;
//        int count = -1 ;
//        try {
//            pstmt = createPreParedStatement(sql, null) ;
//            rs = pstmt.executeQuery() ;//88
//            if(rs.next()) {
//                count= rs.getInt( 1 ) ;
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            closeAll(rs, pstmt, connection);
//        }
//        return count ;
//    }


    //通用的：当前页的数据集合 ,因此当前的数据 是强烈依赖于实体类，例如 显示当前页的学生， List<Student>
    //因此需要将此方法 写入到dao层

//	public static List<Student>




    //通用的增删改
    public static boolean executeUpdate(String sql,Object[] params) {//{"zs",1}
        try {
            //Object[] obs = { name,age ,...,x} ;
//			  String sql = "delete from xxx where Name = ? or id = ?  " ;
//			  pstmt.setInt(1,sno );
            //setXxx()方法的个数 依赖于 ?的个数， 而?的个数 又和 数组params的个数一致
            //setXxx()方法的个数 ->数组params的个数一致
            pstmt = createPreParedStatement(sql,params);
            int count = pstmt.executeUpdate() ;
            if(count>0)
                return true ;
            else
                return false ;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false ;
        } catch (SQLException e) {
            e.printStackTrace();
            return false ;
        }catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
        finally {
            closeAll(null,pstmt,connection);
        }
    }
    //Statement
    public static void closeAll(ResultSet rs, Statement stmt, Connection connection)
    {
        try {
            if(rs!=null)rs.close();
            if(pstmt!=null)pstmt.close();
            if(connection!=null)connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver") ;
        return  DriverManager.getConnection( URL,USERNAME,PASSWORD ) ;
    }

    public static PreparedStatement createPreParedStatement(String sql,Object[] params) throws ClassNotFoundException, SQLException {
        pstmt = getConnection() .prepareStatement( sql) ;
        if(params!=null ) {
            for(int i=0;i<params.length;i++) {
                pstmt.setObject(i+1, params[i]);
            }
        }
        return pstmt;
    }

//    //通用的查  :通用 表示  适合与 任何查询
//    public static ResultSet executeQuery( String sql ,Object[] params) {//select xxx from xx where name=? or id=?
//        Student student = null;
//
//        List<Student> students = new ArrayList<>();
//        try {
//
//            //				  String sql = "select * from student" ;//select enmae ,job from xxxx where...id>3
//
//            pstmt = createPreParedStatement(sql,params);
//            rs =  pstmt.executeQuery() ;
//            return rs ;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            return null ;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null ;
//        }catch (Exception e) {
//            e.printStackTrace();
//            return null ;
//        }
//    }

}


//public class DBUtil {
//    private static Connection connection = null;
//    private static PreparedStatement preparedStatement = null;
//    private static ResultSet resultSet = null;
//    private static final String URL = "jdbc:mysql://localhost:3306/bus";
//    private static final String USERNAME = "busRoot";
//    private static final String PWD = "12345678";
//
//    //初始化
//    static {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 获取连接
//     * @return
//     */
//    private static Connection getConnection() {
//        try {
////            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tbl?useUnicode=true&characterEncoding=UTF-8", "yisa", "ff123321");
//            connection = DriverManager.getConnection(URL, USERNAME, PWD);
//            //将数据库名字修改为自己的数据库
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return connection;
//    }
//
//    /**
//     * 关闭连接、预处理语句和结果集
//     * @param connection
//     * @param preparedStatement
//     * @param resultSet
//     */
//    private static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
//        try {
//            if (resultSet != null) {
//                resultSet.close();
//                resultSet = null;
//            }
//
//            if (preparedStatement != null) {
//                preparedStatement.close();
//                preparedStatement = null;
//            }
//
//            if (connection != null) {
//                connection.close();
//                connection = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 查询数据库
//     * @param sql SQL语句
//     * @param parameters 参数
//     * @return
//     */
//    public static ArrayList<Object[]> query(String sql, String[] parameters) {
//        ArrayList<Object[]> list = new ArrayList<Object[]>();
//        try {
//            connection = getConnection();
//            preparedStatement = connection.prepareStatement(sql);
//            for (int i = 0; i < parameters.length; i++) {
//                preparedStatement.setString(i + 1, parameters[i]);
//            }
//            resultSet = preparedStatement.executeQuery();
//            int columnCount = resultSet.getMetaData().getColumnCount();
//
//            while (resultSet.next()) {
//                Object[] objects = new Object[columnCount];
//                for (int i = 0; i < columnCount; i++) {
//                    objects[i] = resultSet.getObject(i + 1);
//                }
//                list.add(objects);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close(connection, preparedStatement, resultSet);
//        }
//        return list;
//    }
//
//    /**
//     * 更新数据库
//     * @param sqls SQL语句数组
//     * @param parameters 参数数组
//     */
//    public static void updates(String[] sqls, String[][] parameters) {
//        try {
//            connection = getConnection();
//            connection.setAutoCommit(false);
//            for (int i = 0; i < sqls.length; i++) {
//                preparedStatement = connection.prepareStatement(sqls[i]);
//                for (int j = 0; j < parameters[i].length; j++) {
//                    preparedStatement.setString(j + 1, parameters[i][j]);
//                }
//                preparedStatement.executeUpdate();
//            }
//            connection.commit();
//        } catch (Exception e) {
//            try {
//                connection.rollback();
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//            e.printStackTrace();
//        } finally {
//            close(connection, preparedStatement, resultSet);
//        }
//    }
//}
