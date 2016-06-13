package com.yyyu.interviewhelper.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.utils.MyLog;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/6/2
 */
public class ActivityTest extends BaseActivity{

    private EditText et_test;
    private Button btn_get;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {

        et_test = getView(R.id.et_test);
        btn_get = getView(R.id.btn_get);

    }

    private String strToHtml(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append("<p>");
        for (int i = 0 ; i<str.length() ; i++){
            switch (str.charAt(i)){
                case 32:{//换行符
                    sb.append("&nbsp;");
                    break;
                }
                case 9:{//table
                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                    break;
                }
                case 10:{//换行
                    sb.append("<br/>");
                    break;
                }
                default:{
                    sb.append(str.charAt(i));
                }
            }
            sb.append("</p>");
        }
        return sb.toString();
    }

    @Override
    protected void initListener() {
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MyLog.e("==="+strToHtml(et_test.getText().toString()));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
