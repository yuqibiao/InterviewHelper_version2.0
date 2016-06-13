package com.yyyu.interviewhelper.ui.widget.dialog;

import android.content.Context;
import android.widget.ImageView;

import com.yyyu.interviewhelper.R;

/**
 * 功能：生成二维码后的展示dialog
 *
 * @author yyyu
 * @date 2016/6/5
 */
public class QRCodeDialog extends  BaseDialog{

    public  final ImageView iv_qr_code;

    public QRCodeDialog(Context context) {
        super(context);
        setMyContentView(R.layout.dialog_qr_code);
        lp.width = context.getResources().getDimensionPixelSize(R.dimen.qr_dialog_width);
        lp.height = context.getResources().getDimensionPixelSize(R.dimen.qr_dialog_height);
        getWindow().setAttributes(lp);
        iv_qr_code = getView(R.id.iv_qr_code);
        this.setCanceledOnTouchOutside(true);
    }

}
