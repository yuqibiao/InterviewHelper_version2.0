package com.yyyu.interviewhelper.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.RegisterJson;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.widget.CheckCodeTextView;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：注册界面
 *
 * @author yyyu
 * @date 2016/5/29
 */
public class RegisterActivity extends BaseActivity {

    private Toolbar tb_register;
    private EditText et_username;
    private EditText et_password;
    private EditText et_check_code;
    private CheckCodeTextView cctv_register;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        tb_register = getView(R.id.tb_register);
        et_username = getView(R.id.et_username);
        et_check_code = getView(R.id.et_check_code);
        cctv_register = getView(R.id.cctv_register);
        et_password = getView(R.id.et_password);
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.btn_register);
        tb_register.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register: {
                String pswStr = et_password.getText().toString();
                String nameStr = et_username.getText().toString();
                MyLog.e("=====cctv_register.getCodeValue()=="+cctv_register.getCodeValue());
                if(!cctv_register.getCodeValue().equals(et_check_code.getText().toString())){
                    MyToast.showShort(this , "验证码错误！！");
                    break;
                }
                if(pswStr.length()<6||pswStr.length()>20){
                    MyToast.showShort(this , "密码在6~20位");
                    break;
                }
                if(TextUtils.isEmpty(pswStr) || TextUtils.isEmpty(nameStr)){
                    MyToast.showShort(this,"用户名或密码不能为空");
                    break;
                }
                Map<String ,String> params = new HashMap<>();
                params.put("username" , nameStr);
                params.put("password" , pswStr);
                toRegister(params);
                break;
            }
        }
    }

    private void toRegister(Map<String, String> params) {
        MyHttpManager.postAsyn(MyHttpUrl.REGISTER_URL, new MyHttpManager.ResultCallback<RegisterJson>() {
            @Override
            public void onError(Request request, Exception e) {
                MyLog.e("=======注册失败===="+e.getMessage());
            }
            @Override
            public void onResponse(RegisterJson registerJson) {
                if (registerJson.success){
                    RegisterActivity.this.finish();
                }
                MyToast.showLong(RegisterActivity.this , registerJson.msg);
            }
        } , params);
    }
}
