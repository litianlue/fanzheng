package com.dyl.base_lib;

/**
 * Created by dengyulin on 2018/4/8.
 */

public class JavaTest {
    public JavaTest(String str){

    }
    public void test(){
        OBJ.INSTANCE.a();
        
    }
    static class OBJ{
        static OBJ INSTANCE=new OBJ();
        void a(){}
    }
}
