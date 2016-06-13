package com.yyyu.interviewhelper.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能：时间相关的工具类
 *
 * @author yyyu
 * @date 2016/6/1
 */
public class TimeUtils {
    public static String getSysTime(){
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd_HH-mm-ss");
        return format.format(new Date());
    }
}
