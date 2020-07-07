package com.lib.javatest;

import android.util.Log;

public class MathOperater {
    private static final String TAG="MathOperater";
    public void testOperator(){
        String str="我的bo ke";
        int z=-4;//11111111 11111111 11111111 11111100
        int a=3;//0011
        int b=4;//0100
        int c=a^b;//0111
        int d=~a;//1100
        int e=b<<2;//010000
        int f=b>>2;//0001
        int g=z>>>2;//00111111 11111111 11111111 11111111
        int h=z<<2;//11111111 11111111 11111111 11110000 //11111111 11111111 11111111 11101111
        //00000000 00000000 00000000 00010000 // -16
        boolean yh=true^false;//true
        //
        Log.d(TAG,"testOperator--"+"c-"+c+"d-"+d+"e-"+e+"f-"+f+"g-"+g+"h-"+h+"yh-"+yh);
        byte[] strBytes=str.getBytes();
        String b2Str=new String(strBytes);
        System.out.println("字符串转为字节数组：");
        for (byte bStr:strBytes){
            System.out.print(bStr+" ");
        }
        System.out.println();
        System.out.println("字节数组转换为字符串："+b2Str);

    }
}
