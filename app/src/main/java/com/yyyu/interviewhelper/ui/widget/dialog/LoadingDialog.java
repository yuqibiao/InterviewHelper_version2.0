package com.yyyu.interviewhelper.ui.widget.dialog;


import android.content.Context;
import android.widget.TextView;

import com.yyyu.interviewhelper.R;


/**
 * 使用自定义VipProgressbar的Dialog
 *
 * @author yyyu
 * @date 2016-1-11
 */
public class LoadingDialog extends BaseDialog {

    public LoadingDialog(Context context) {
        super(context);
        setMyContentView(R.layout.dialog_lodding);
        lp.width = context.getResources().getDimensionPixelSize(R.dimen.loading_dialog_width); // 宽度
        lp.height = context.getResources().getDimensionPixelSize(R.dimen.loading_dialog_height);// 高度
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
    }

    /**
     * 设置加载的提示
     *
     * @param tips
     */
    public void setTips(String tips) {
        ((TextView) getView(R.id.tv_tip)).setText(tips);
    }
}
