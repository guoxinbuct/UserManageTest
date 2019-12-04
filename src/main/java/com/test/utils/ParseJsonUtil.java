package com.test.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ParseJsonUtil {

    private Map<String,String> map=new HashMap<String, String>();

    public static boolean isJsonString(String str){
        if(str.equals("")||str.equals("null")){
            return false;
        }
        try {
            JSON.parseObject(str);
        }catch(JSONException e){
            return false;
        }
        return true;
    }

    public static boolean isJsonArray(String str){
        if(str.equals("")||str.equals("null")){
            return false;
        }
        try{
            JSON.parseArray(str);
        }catch (JSONException e){
            return false;
        }
        return true;
    }


    public  Map<String,String> parseJsonToMap(String str){

        if(isJsonString(str)){
            //System.out.println("jsonString========");
            JSONObject jsonObject=JSON.parseObject(str);
            for(Map.Entry<String,Object> entry:jsonObject.entrySet()){
                //System.out.println(entry.getKey()+"="+entry.getValue());
                map.put(entry.getKey(),entry.getValue().toString());
                parseJsonToMap(entry.getValue().toString());
            }
        }else if(isJsonArray(str)){
            //System.out.println("jsonArray=============");
            JSONArray jsonArray=JSON.parseArray(str);
            for(Object json:jsonArray){
                parseJsonToMap(json.toString());
            }
        }
        return map;
    }


    public static void main(String[] args) {
        String str0="a,b,c,d,e";
        String str1="{'a':'1','b':'2','c':'3'}";
        String str2="{'a':'1','b':'2','c':{'c1':'1','c2':'2'}}";
        String str3="[{'a':'1','b':'2'},{'a':'1','b':'2','c':{'c1':'1','c2':'2'}}]";
        String str4="{'aaa':{'a':'1','b':{'b1':{'b2':'222'}}},'c':'null'}";
        String actResult="{\"status\":1,\"info\":\"\",\"data\":{\"id\":\"1052300\",\"lastlogin\":1575274903,\"logins\":[\"exp\",\"logins+1\"],\"lastip\":\"123.123.222.36\",\"checktype\":1}}";
        String result="{\"logins\":[\"exp\",\"logins+1\"],\"lastip\":\"123.123.222.36\"}}";
        String result2="{\"id\":\"1052300\",\"lastlogin\":1575274903,\"logins\":[\"exp\",\"logins+1\"]}";
        String result3="{\"logins\":[\"exp\",\"logins+1\"]}";


    }
}
