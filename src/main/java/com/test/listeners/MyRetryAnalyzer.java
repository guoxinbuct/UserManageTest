package com.test.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MyRetryAnalyzer implements IRetryAnalyzer {
    public int retryMinCount=0;
    public int retryMaxCount=2;
    public boolean retry(ITestResult result) {
        Boolean flag=false;
        if(retryMinCount<retryMaxCount){
            retryMinCount++;
            flag=true;
            System.out.println("当前重跑用例："+result);
            System.out.println("第"+retryMinCount+"次重跑");
        }else{
            resetRetryCount();
        }
        return flag;
    }

    public void resetRetryCount(){
        retryMinCount=0;
    }
}
