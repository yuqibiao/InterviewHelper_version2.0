package com.yyyu.interviewhelper.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.callback.OnModifyUserInfoListener;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MyToast;

/**
 * 功能：输入用户信息的dialog
 *
 * @author yyyu
 * @date 2016/6/3
 */
public class UserInfoInputDialog extends BaseDialog {

    private Context mContext;
    private EditText et_modify_info;
    private OnModifyUserInfoListener mOnModifyUserInfoListener;

    public UserInfoInputDialog(Context context) {
        super(context);
        this.mContext = context;
        setMyContentView(R.layout.dialog_info_input);
        et_modify_info = getView(R.id.et_modify_info);
        setMyClickListener(R.id.btn_ok, R.id.btn_cancel);
    }

    public void setOnModifyUserInfoListener(OnModifyUserInfoListener onModifyUserInfoListener) {
        this.mOnModifyUserInfoListener = onModifyUserInfoListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                if (mOnModifyUserInfoListener != null) {
                    String content = et_modify_info.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        MyToast.showShort(mContext, "输入不能为空");
                    }
                    mOnModifyUserInfoListener.onSureModify(content);
                }
                break;
            }
            case R.id.btn_cancel: {
                this.dismiss();
            }
        }
    }

}
