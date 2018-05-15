package com.base.project.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/9/20.
 */

public class KeywordMatching {


    public static String isHave(String[] strOne,String[] strTwo, String s) {
        //此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串

        for (int i = 0; i < strOne.length; i++) {
            Log.i("qaz", "isHave:--1--" +strOne[i]);
            if (s.contains(strOne[i])) {//循环查找字符串数组中的每个字符串中是否包含所有查找的内容
                for (int j = 0 ; j< strTwo.length ; j++){
                   // Log.i("qaz", "isHave:---2--- " +strTwo.length);
                    if (s.contains(strTwo[j])){
                        //Log.i("qaz ", "isHave: " + strOne[i]);
                       // Log.i("qaz", "isHave: ----3---" +strTwo[j]+strOne[i]);
                       // Log.i("qaz", "isHave:匹配的命令 " +strTwo[j]+strOne[i]);
                        return strTwo[j]+strOne[i];
                    }
                }
               //查找到了就返回真，不在继续查询
            }

        }
        return "";//没找到返回false
    }

}
