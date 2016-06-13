package com.yyyu.interviewhelper.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.callback.OnModifyUserInfoListener;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/6/3
 */
public class UserSexModifyDialog extends BaseDialog{

    private ImageView iv_female , iv_male;
    private OnModifyUserInfoListener mOnModifyUserInfoListener;

    public UserSexModifyDialog(Context context) {
        super(context);
        setMyContentView(R.layout.dialog_sex_choice);
        setMyClickListener(R.id.ll_male , R.id.ll_female);
        iv_female = getView(R.id.iv_female);
        iv_male = getView(R.id.iv_male);
    }

    public void setOnModifyUserInfoListener(OnModifyUserInfoListener onModifyUserInfoListener) {
        this.mOnModifyUserInfoListener = onModifyUserInfoListener;
    }

    public void setChecked(String str){
        if ("男".equals(str)){
            iv_male.setVisibility(View.VISIBLE);
            iv_female.setVisibility(View.INVISIBLE);
        }else if ("女".equals(str)){
            iv_male.setVisibility(View.INVISIBLE);
            iv_female.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_male:{
                mOnModifyUserInfoListener.onSureModify("男");
                dismiss();
                break;
            }
            case R.id.ll_female:{
                mOnModifyUserInfoListener.onSureModify("女");
                dismiss();
                break;
            }
        }
    }
}
