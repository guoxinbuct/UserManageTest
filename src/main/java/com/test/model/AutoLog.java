package com.test.model;

import lombok.Data;

@Data
public class AutoLog {

    String caseID;
    String caseName;
    String reqType;
    String reqURL;
    String reqParam;
    String expResult;
    String actResult;
    String execResult;
    String execTime;

    public AutoLog(){

    }
    public AutoLog(String caseID,String caseName,String reqType,String reqURL,String reqParam,String expResult,String actResult,String execResult,String execTime){
        this.caseID=caseID;
        this.caseName=caseName;
        this.reqType=reqType;
        this.reqURL=reqURL;
        this.reqParam=reqParam;
        this.expResult=expResult;
        this.actResult=actResult;
        this.execResult=execResult;
        this.execTime=execTime;
    }
}
