package com.test.cases;

import com.test.model.AutoLog;
import com.test.utils.DbcpUtil;
import com.test.utils.ExcelUtil;
import com.test.utils.HttpUtil;
import com.test.utils.ParseJsonUtil;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginCase {

    private String excelFilePath;
    private Map<String, Map<String,String>> resultMap;
    private List<AutoLog> autoLogList;
    @BeforeTest
    @Parameters({"testDataFilePath"})
    public void beforeTest(String filePath){
        System.out.println("beforeTest===================");
        this.excelFilePath=filePath;
        resultMap=new HashMap<String, Map<String, String>>();
        autoLogList=new ArrayList<AutoLog>();
    }

    @DataProvider
    public Object[][] testData() throws IOException {
        System.out.println("DataProvider=========================");
        Object[][] testData= ExcelUtil.getExcelValues("/users/guoxin/test/test.xlsx",0);
        return testData;
    }

    @Test(dataProvider = "testData")
    public void login(String caseID, String isExec,String caseName, String reqType, String reqHost,String reqURI, String param, String expected, String actResult, Double execTime, String isDep) throws IOException {
        //输入错误的用户名和密码登录失败
        System.out.println("login==========================");
        String reqURL="http://"+reqHost+reqURI;
        System.out.println(caseID+"\t"+isExec+"\t"+caseName+"\t"+reqType+"\t"+reqURL+"\t"+param+"\t"+expected+"\t"+actResult+"\t"+execTime+"\t"+isDep);
        if(param.contains("${")){
            String replaceString=param.substring(param.indexOf("${")+2,param.indexOf("}"));
            String[] keys=replaceString.split("=");
            String value=resultMap.get(keys[0]).get(keys[1]);
            param=param.replace(param.substring(param.indexOf("$"),param.indexOf("}")+1),value);
            System.out.println("替换后的param值为："+param);

        }
        if(isExec.toLowerCase().equals("yes")){
            Reporter.log("用例ID:"+caseID);
            Reporter.log("用例名称:"+caseName);
            Reporter.log("请求URL："+reqURL);
            Reporter.log("请求参数："+param);
            Reporter.log("请求方式："+reqType);
            Reporter.log("预期结果："+expected);
            if(reqType.toLowerCase().equals("reqType")){
                actResult=HttpUtil.get(reqURL,param);
            }else{
                actResult=HttpUtil.post(reqURL,param);
            }

            if("yes".equals(isDep)){
                System.out.println("该接口被依赖需要存储返回结果=========================");
                System.out.println("actResult="+actResult);
                Map<String,String> tmpMap=new ParseJsonUtil().parseJsonToMap(actResult);
                resultMap.put(reqURI,tmpMap);
                System.out.println("**********"+resultMap.toString()+"************");
                for(Map.Entry<String,String> testMap:tmpMap.entrySet()){
                    System.out.print(testMap.getKey()+"="+testMap.getValue()+"\t");
                }
            }
            //System.out.println("actResult="+actResult);
            Reporter.log("实际结果："+actResult);
            System.out.println("用例"+caseID+"执行完毕！$$$$$$$$$$$$$$$$$$$");
            String[] expectedStrs=expected.split(",");
            for(String str:expectedStrs){
                Assert.assertTrue(actResult.contains(str));
            }
            AutoLog autoLog=new AutoLog(caseID,caseName,reqType,reqURL,param,expected,actResult,"","2019-12-12");
            autoLogList.add(autoLog);

        }else{
            Reporter.log("该用例不需要执行");
        }


    }


    @AfterTest
    public void afterTest(){
        System.out.println("afterTest====================");
        for(Map.Entry<String,Map<String,String>> entry:resultMap.entrySet()){
            System.out.println("resultMap************"+entry.toString()+"*****************");
            String caseID=entry.getKey();
            Map<String,String> tmpMap=entry.getValue();
            System.out.print("测试用例ID："+entry.getKey()+"====>");
            for(Map.Entry<String,String> map:tmpMap.entrySet()){
                System.out.print(map.getKey()+"="+map.getValue()+"\t");
            }
            System.out.println();
        }
        DbcpUtil.insertBatch(autoLogList);
        System.out.println("afterTest====================");
    }
}
