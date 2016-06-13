package com.yyyu.interviewhelper.callback;

import com.yyyu.interviewhelper.bean.MenuDataJson;

import java.util.List;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/6/1
 */
public interface OnMenuClazzLoadedListener {
    public void onMenuClazzLoaded(List<MenuDataJson.ClazzJson> clazzJsonList);
}
