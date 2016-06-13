package com.yyyu.interviewhelper.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.UserDictionary;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.ui.activity.LoginActivity;

/**
 * 功能：业务逻辑相关Utils
 *
 * @author yyyu
 * @date 2016/5/29
 */
public class LogicUtils {

    public  static final String LOGOUT_ACTION="com.yyyu.interviewHelper.LOGOUT_ACTION";

    /**
     * 得到登录用户的信息
     * 返回为空说明没有登录
     */
    public static LoginJson.UserInfo getUserInfo(Context context) {
        String userInfo = (String) MySPUtils.get(context, LoginActivity.LOGIN_JSON, "");
        if (TextUtils.isEmpty(userInfo)) return null;
        Gson gson = new Gson();
        LoginJson loginJson = gson.fromJson(userInfo, LoginJson.class);
        return loginJson.userInfo;
    }
    /**
     * 判断是否登录
     */
    public static boolean isLogin(Context context) {
        return getUserInfo(context) == null ? false : true;
    }
    /**
     * 注销用户
     */
    public static void logout(Context context){
        MySPUtils.remove(context , LoginActivity.LOGIN_JSON);
        //--发送用户注销广播
        Intent intent = new Intent();
        intent.setAction(LOGOUT_ACTION);
        context.sendBroadcast(intent);
    }

}
