package com.yyyu.interviewhelper.utils;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：题目相关操作工具类
 *
 * @author yyyu
 * @date 2016/6/7
 */
public class ExeUtils {

    /**
     * 去除回车、换行、空格
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 判断多选是否正确
     */
    public static boolean checkMultipleSelection(String option , String rightAns){
        return option.equalsIgnoreCase(rightAns) ? true : false;
    }

    /**
     * 判断单选是否正确
     */
    public static boolean checkSimpleSlection(int option, String rightAns){
        if(rightAns.equalsIgnoreCase("" + Num2Char(option))){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 把0123456 变成ABCDEFG
     */
    public static char Num2Char(int num){
        char chr ='A';
        switch (num){
            case 0: chr='A'; break;
            case 1: chr='B'; break;
            case 2: chr='C'; break;
            case 3: chr='D'; break;
            case 4: chr='E'; break;
            case 5: chr='F'; break;
            case 6: chr='G'; break;
        }
        return chr;
    }

    /**
     * 把ABCDEFG变成0123456
     */
    public  static int str2Num(String str){
        int num =-1;
        if("a".equalsIgnoreCase(str)){num = 0;}
        else if("b".equalsIgnoreCase(str)){num = 1;}
        else  if("c".equalsIgnoreCase(str)){num = 2;}
        else if("d".equalsIgnoreCase(str)){num = 3;}
        else if("e".equalsIgnoreCase(str)){num = 4;}
        else if("f".equalsIgnoreCase(str)){num = 5;}
        else if("g".equalsIgnoreCase(str)){num = 6;}
        return num;
    }

    /**
     * 把ABCD字符串换成数字集合
     */
    public static List<Integer> str2NumList(String str){
        List<Integer> nums = new ArrayList<>();
        for (int i = 0 ; i<str.length(); i++){
            nums.add(str2Num(str.charAt(i)+""));
        }
        return nums;
    }

}


















