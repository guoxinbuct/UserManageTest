package com.test.utils;

import com.test.model.AutoLog;
import org.apache.commons.dbcp.BasicDataSource;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbcpUtil {

    private static BasicDataSource dataSource=new BasicDataSource();

    static{
        try {
            //连接数据库基本信息
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://192.168.0.110:3306/apiAutoTest?characterEncoding=utf-8&useSSL=false");
            dataSource.setUsername("root");
            dataSource.setPassword("root");

            //连接池配置信息
            dataSource.setInitialSize(3);
            dataSource.setMinIdle(3);
            dataSource.setMaxIdle(3);
            dataSource.setMaxActive(3);

            //连接池借出和客户端返回连接检查配置
            dataSource.setTestOnReturn(false);
            dataSource.setTestOnBorrow(false);
            dataSource.setMaxWait(1000);

            //连接池支持预编译
            dataSource.setPoolPreparedStatements(true);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static synchronized Connection getConnection(){
        Connection conn=null;
        try {
            conn=dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(ResultSet rs, PreparedStatement ps,Connection conn){
        try{
            if(rs!=null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(conn!=null){
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Object> handler(ResultSet rs, Class<?> className){
        List<Object> list=new ArrayList<Object>();
        try{
            while(rs.next()){
                ResultSetMetaData metaData=rs.getMetaData();
                int columnCount=metaData.getColumnCount();
                Object object=className.newInstance();
                for(int columnIndex=1;columnIndex<=columnCount;columnIndex++){
                    String columnName=metaData.getColumnName(columnIndex);
                    Field field=className.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(object,rs.getObject(columnIndex));
                }
                list.add(object);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static void query(AutoLog autoLog){
        String sql="select * from autoLog where reqType=?";
        Connection connection=getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=connection.prepareStatement(sql);
            ps.setString(1,autoLog.getReqType());
            rs=ps.executeQuery();
            List<Object> list=handler(rs,AutoLog.class);
            for(Object object:list){
                AutoLog autoLog1=(AutoLog)object;
                System.out.println(autoLog1.getCaseID()+"\t"+autoLog1.getCaseName() +"\t"
                        +autoLog1.getReqType()+"\t"+autoLog1.getReqURL()+"\t"
                        +autoLog1.getExpResult()+"\t"+autoLog1.getActResult()+"\t"
                        +autoLog1.getExecTime()+"\t"+autoLog1.getExecResult());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(rs,ps,connection);
        }
    }

    public static int[] updateBatch(List<AutoLog> list){
        String sql="update autoLog set caseName=? where caseID=?";
        Connection conn=getConnection();
        PreparedStatement ps=null;
        int[] updateCount=null;
        try{
            ps=conn.prepareStatement(sql);
            for(AutoLog autoLog:list){
                ps.setString(1,autoLog.getCaseName());
                ps.setString(2,autoLog.getCaseID());
                ps.addBatch();
            }
            updateCount=ps.executeBatch();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(null,ps,conn);
        }
        return updateCount;
    }

    public static int[] insertBatch(List<AutoLog> list){
        String sql="insert into autoLog(caseID,caseName,reqType,reqURL,reqParam,expResult,actResult,execResult,execTime) values (?,?,?,?,?,?,?,?,?)";
        Connection conn=getConnection();
        PreparedStatement ps=null;
        int[] updateCount=null;
        try{
            ps=conn.prepareStatement(sql);
            for(AutoLog autoLog:list){
                ps.setString(1,autoLog.getCaseID());
                ps.setString(2,autoLog.getCaseName());
                ps.setString(3,autoLog.getReqType());
                ps.setString(4,autoLog.getReqURL());
                ps.setString(5,autoLog.getReqParam());
                ps.setString(6,autoLog.getExpResult());
                ps.setString(7,autoLog.getActResult());
                ps.setString(8,autoLog.getExecResult());
                ps.setString(9,autoLog.getExecTime());
                ps.addBatch();
            }
            updateCount=ps.executeBatch();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(null,ps,conn);
        }
        return updateCount;
    }

    public static void main(String[] args) {
        AutoLog autoLog1=new AutoLog();
        autoLog1.setCaseID("test001");
        autoLog1.setCaseName("登录6001");
        autoLog1.setReqType("post");
        autoLog1.setReqURL("http://localhost:8888/v1/login/test1");
        autoLog1.setReqParam("{'userName':'test01','password':'test0001'}");
        autoLog1.setExpResult("true");
        autoLog1.setActResult("false");
        autoLog1.setExecResult("failed");
        autoLog1.setExecTime("2019122601");
        AutoLog autoLog2=new AutoLog();
        autoLog2.setCaseID("test002");
        autoLog2.setCaseName("登录6002");
        autoLog2.setReqType("post");
        autoLog2.setReqURL("http://localhost:8888/v1/login/test2");
        autoLog2.setReqParam("{'userName':'test02','password':'test0002'}");
        autoLog2.setExpResult("true");
        autoLog2.setActResult("false");
        autoLog2.setExecResult("failed");
        autoLog2.setExecTime("2019122602");
        AutoLog autoLog3=new AutoLog();
        autoLog3.setCaseID("test1002");
        autoLog3.setCaseName("登录6003");
        autoLog3.setReqType("get");
        autoLog3.setReqURL("http://localhost:8888/v1/login/test3");
        autoLog3.setReqParam("{'userName':'test03','password':'test0003'}");
        autoLog3.setExpResult("true");
        autoLog3.setActResult("false");
        autoLog3.setExecResult("failed");
        autoLog3.setExecTime("2019122603");
        List<AutoLog> autoLogList=new ArrayList<AutoLog>();
        autoLogList.add(autoLog1);
        autoLogList.add(autoLog2);
        autoLogList.add(autoLog3);
        int[] updateCount=updateBatch(autoLogList);
        for(int i:updateCount){
            System.out.println(i);
        }

    }
}
