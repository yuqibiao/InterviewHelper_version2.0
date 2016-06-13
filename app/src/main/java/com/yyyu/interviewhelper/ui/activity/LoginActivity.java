package com.yyyu.interviewhelper.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MySPUtils;
import com.yyyu.interviewhelper.utils.MySnackBar;
import com.yyyu.interviewhelper.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：登录界面
 *
 * @author yyyu
 * @date 2016/5/24
 */
public class LoginActivity extends BaseActivity {

    public static final String LOGIN_SUCCESS="com.interviewHelper.LOGIN_SUCCESS";
    public static final String LOGIN_JSON="LOGIN_JSON";

    private Toolbar tb_login;
    private EditText et_username;
    private EditText et_password;
    private LinearLayout ll_login_root;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        tb_login = getView(R.id.tb_login);
        et_username = getView(R.id.et_username);
        et_password = getView(R.id.et_password);
        ll_login_root = getView(R.id.ll_login_root);
        setSupportActionBar(tb_login);
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.btn_login, R.id.tv_register, R.id.tv_find_psw);
        tb_login.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                String nameStr = et_username.getText().toString();
                String pwdStr = et_password.getText().toString();
                if(TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(pwdStr)){
                    MyToast.showShort(this, "用户名或密码不能为空");
                    break;
                }
                Map<String ,String > params = new HashMap<>();
                params.put("username",nameStr);
                params.put("password" , pwdStr);
                toLogin(params);
                break;
            }
            case R.id.tv_register: {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_find_psw: {
                break;
            }
        }
    }

    private void toLogin(final Map<String, String> params) {
        MyHttpManager.postAsyn(MyHttpUrl.LOGIN_URL, new MyHttpManager.ResultCallback<LoginJson>() {
            @Override
            public void onError(Request request, Exception e) {
                MySnackBar.showLongDef(ll_login_root , "网络连接错误")
                        .setAction("点击重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toLogin(params);
                    }
                });
            }
            @Override
            public void onResponse(LoginJson loginJson) {
                if(loginJson.success){//--登录成功
                    MySPUtils.put(LoginActivity.this ,LOGIN_JSON ,new Gson().toJson(loginJson).toString());
                    //发送登录成功的广播
                    Intent intent = new Intent();
                    intent.setAction(LOGIN_SUCCESS);
                    sendBroadcast(intent);
                    LoginActivity.this.finish();
                }
                MyToast.showShort(LoginActivity.this ,loginJson.msg);
            }
        }, params);
    }
}
