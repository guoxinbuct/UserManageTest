package com.test.listeners;

import org.testng.*;

import java.util.*;

public class MyTestNGListener implements ITestListener {
    public List<ITestResult> failedRepeatResults=new ArrayList<ITestResult>();
    public Set<Integer> failedHashCodes=new HashSet<Integer>();
    public Set<Integer> passHashCodes=new HashSet<Integer>();

    public void onTestStart(ITestResult result) {

    }

    public void onTestSuccess(ITestResult result) {

    }

    public void onTestFailure(ITestResult result) {

    }

    public void onTestSkipped(ITestResult result) {


    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    public void onStart(ITestContext context) {

    }

    public void onFinish(ITestContext context) {
        /*for(ITestResult passedResult:context.getPassedTests().getAllResults()){
            System.out.println("passedResult----->>>>>>"+passedResult);
        }

        for(ITestResult skippedResult:context.getSkippedTests().getAllResults()){
            System.out.println("skippedResult"+skippedResult);
        }

        for(ITestResult failedResult: context.getFailedTests().getAllResults()){
            System.out.println("failedResult---->>>>>>"+failedResult);
            int hashcode=getHashCode(failedResult);
            if(failedHashCodes.contains(hashcode)||passHashCodes.contains(hashcode)){
                failedRepeatResults.add(failedResult);
            }else{
                failedHashCodes.add(hashcode);
            }
        }

        for(ITestResult failedRepeatResult:failedRepeatResults){
            System.out.println("failedRepeatResult===========>>>>>>"+failedRepeatResult);
        }
        removeRepeatResult(context);*/
        removeRepeatTestResult(context);

    }

    public int getHashCode(ITestResult testResult){
        System.out.println("***************"+testResult.getTestClass().getName());
        int class_hashcode=testResult.getTestClass().getName().hashCode();
        int method_hashcode=testResult.getMethod().getMethodName().hashCode();
        int reqParam_hashcode=0;
        if(testResult.getParameters()!=null){
            reqParam_hashcode=Arrays.hashCode(testResult.getParameters());
        }
        return class_hashcode+method_hashcode+reqParam_hashcode;
    }

    public void removeRepeatTestResult(ITestContext context){
        Iterator<ITestResult> failedIt=context.getFailedTests().getAllResults().iterator();
        while(failedIt.hasNext()){
            ITestResult failedResult=failedIt.next();
            ITestNGMethod method=failedResult.getMethod();
            if(context.getFailedTests().getResults(method).size()>1){
                failedIt.remove();
            }else if(context.getPassedTests().getResults(method).size()>0){
                failedIt.remove();
            }
        }

    }

    public void removeRepeatResult(ITestContext context){
        Iterator<ITestResult> it=context.getFailedTests().getAllResults().iterator();
        System.out.println(it.hasNext());
          /*for(int i=0;i<failedRepeatResults.size();i++){
              System.out.println("@@@@@@@@@@"+failedRepeatResults.get(i));
              while(it.hasNext()){
                  ITestResult testResult=it.next();
                  System.out.println("testResult.getMethod()===================>>>>>>>>> "+testResult.getMethod());
                  if(failedRepeatResults.get(i)==testResult){
                      it.remove();
                      failedRepeatResults.remove(i);
                      System.out.println("it.remove()执行************");
                      continue;
                  }
              }*/
          while(it.hasNext()){
              ITestResult iTestResult=it.next();
              if(failedRepeatResults.contains(iTestResult)){
                  it.remove();
              }

          }
        Iterator<ITestResult> it2=context.getFailedTests().getAllResults().iterator();
         while (it2.hasNext()){
             System.out.println("%%%%%%%%%%%%%%%%"+it2.next().getName());
         }
    }
}
