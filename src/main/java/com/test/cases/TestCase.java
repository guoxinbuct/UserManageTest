package com.test.cases;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCase {

    @Test
    public void zzzz() {
        System.out.println("testzzzz***********************");
        System.out.println(System.currentTimeMillis());
        Assert.assertEquals(1,2);
        System.out.println(System.currentTimeMillis());

    }

    @Test
    public void test2() {
        System.out.println("test2***********************");
        System.out.println(System.currentTimeMillis());
        String testStr="'status':1";
        String[] strs=testStr.split(",");
        for(String str:strs){
            System.out.println(str);
        }
        Assert.assertEquals(1,1);
        System.out.println(System.currentTimeMillis());

    }
}
