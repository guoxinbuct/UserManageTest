package com.test.utils;

import com.test.model.AutoLog;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtil {
    private static String url="jdbc:mysql://192.168.0.110:3306/apiAutoTest?characterEncoding=UTF-8&useSSL=false";
    private static String userName="root";
    private static String password="root";

    public static void query(String sql){
        //String sql="select * from autoLog where reqType=?";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,userName,password);
            ps=conn.prepareStatement(sql);
            ps.setString(1,"post");
            rs=ps.executeQuery();
            /*while(rs.next()){
                System.out.println(rs.getString("caseID"));
                System.out.println(rs.getString("caseName"));
                System.out.println(rs.getString("reqType"));
                System.out.println(rs.getString("reqURL"));
                System.out.println(rs.getString("reqParam"));
                System.out.println(rs.getString("expResult"));
                System.out.println(rs.getString("actResult"));
                System.out.println(rs.getString("execResult"));
                System.out.println(rs.getString("execTime"));

            }*/
            List<Object> objectList=handler(rs,AutoLog.class);
            for(Object object:objectList){
                AutoLog autoLog=(AutoLog) object;
                System.out.println(autoLog.getCaseID()+"\t"+autoLog.getCaseName()+"\t"
                        +autoLog.getReqType()+"\t"+autoLog.getReqURL()+"\t"
                        +autoLog.getExpResult()+"\t"+autoLog.getActResult()+"\t"
                        +autoLog.getExecResult()+"\t"+autoLog.getExecTime());

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(rs,ps,conn);
        }

    }


    public static int update(AutoLog autoLog,String sql){
        //String sql="insert into autoLog(caseID,caseName,reqType,reqURL,reqParam,expResult,actResult,execResult,execTime) values (?,?,?,?,?,?,?,?,?)";
        int count=0;
        Connection conn=getConnection(url,userName,password);
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement(sql);
            ps.setString(1,autoLog.getCaseID());
            ps.setString(2,autoLog.getCaseName());
            ps.setString(3,autoLog.getReqType());
            ps.setString(4,autoLog.getReqURL());
            ps.setString(5,autoLog.getReqParam());
            ps.setString(6,autoLog.getExpResult());
            ps.setString(7,autoLog.getActResult());
            ps.setString(8,autoLog.getExecResult());
            ps.setString(9,autoLog.getExecTime());
            count=ps.executeUpdate();
            System.out.println("更新了"+count+"条数据");
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            close(null,ps,conn);
        }
        return count;
    }


    public static int[] batchUpdate(List<AutoLog> autoLogList,String sql){
        Connection conn=getConnection(url,userName,password);
        PreparedStatement ps=null;
        int[] updateCounts=null;
        try{
            ps=conn.prepareStatement(sql);
            for(AutoLog autoLog:autoLogList){
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
            updateCounts=ps.executeBatch();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            close(null,ps,conn);
        }
        return updateCounts;

    }

    public static Connection getConnection(String url,String userName,String password){
        Connection conn=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,userName,password);
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(ResultSet rs,PreparedStatement ps,Connection conn){
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
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    public static List<Object> handler(ResultSet rs,Class<?> className) throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException {
        List<Object> objectList=new ArrayList<Object>();
        while(rs.next()){
            ResultSetMetaData rsMetaData=rs.getMetaData();
            int columnCount=rsMetaData.getColumnCount();
            Object object=className.newInstance();
            for(int i=1;i<=columnCount;i++){
                String columnName=rsMetaData.getColumnName(i);
                Field field=className.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(object,rs.getObject(i));
            }
            objectList.add(object);
        }
        return objectList;

    }

    public static void main(String[] args) {
        query("select * from autoLog where reqType=?");
        AutoLog autoLog1=new AutoLog();
        autoLog1.setCaseID("test1001");
        autoLog1.setCaseName("登录1001");
        autoLog1.setReqType("post");
        autoLog1.setReqURL("http://localhost:8888/v1/login/test1");
        autoLog1.setReqParam("{'userName':'test01','password':'test0001'}");
        autoLog1.setExpResult("true");
        autoLog1.setActResult("false");
        autoLog1.setExecResult("failed");
        autoLog1.setExecTime("2019122601");
        AutoLog autoLog2=new AutoLog();
        autoLog2.setCaseID("test1002");
        autoLog2.setCaseName("登录1002");
        autoLog2.setReqType("post");
        autoLog2.setReqURL("http://localhost:8888/v1/login/test2");
        autoLog2.setReqParam("{'userName':'test02','password':'test0002'}");
        autoLog2.setExpResult("true");
        autoLog2.setActResult("false");
        autoLog2.setExecResult("failed");
        autoLog2.setExecTime("2019122602");
        AutoLog autoLog3=new AutoLog();
        autoLog3.setCaseID("test1002");
        autoLog3.setCaseName("登录1003");
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
        String sql="insert into autoLog(caseID,caseName,reqType,reqURL,reqParam,expResult,actResult,execResult,execTime) values (?,?,?,?,?,?,?,?,?)";
        int[] counts=batchUpdate(autoLogList,sql);
        for(int i:counts){
            System.out.println(i);
        }

    }
}
