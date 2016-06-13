package com.yyyu.interviewhelper.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.yyyu.interviewhelper.MyApplication;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.utils.PicChoiceUtils;
import com.yyyu.interviewhelper.utils.SDcardUtils;

import java.io.File;

public class PicChoiceWithPop implements OnClickListener{

	private String popAction;
	private View popView;
	private Activity mActivity;
	private Button btn_camera, btn_photo, btn_cancel;
	private PopupWindow window;
	private View popLocView ;

	public PicChoiceWithPop(Activity activity, View popLocView) {
		mActivity = activity;
		this.popLocView=popLocView;
		initView();
		initListener();
	}

	public void initView() {
		popView = View.inflate(mActivity, R.layout.pop_pic_select, null);
		btn_camera = (Button) popView.findViewById(R.id.item_popupwindows_camera);
		btn_photo = (Button) popView.findViewById(R.id.item_popupwindows_Photo);
		btn_cancel = (Button) popView.findViewById(R.id.item_popupwindows_cancel);
	}
	private void initListener() {
		btn_camera.setOnClickListener(this);
		btn_photo.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}
	public void showPop(){
		popView.measure(0, 0); // 一定要测量一下，才能得到宽高
		window = new PopupWindow(popView,
				WindowManager.LayoutParams.MATCH_PARENT,
				popView.getMeasuredHeight()){
			@Override
			public void dismiss() {
				super.dismiss();
				//当pop dismiss后主窗体背景恢复
				WindowManager.LayoutParams lp=mActivity.getWindow().getAttributes();
				lp.alpha = 1f;
				mActivity.getWindow().setAttributes(lp);
			}
		};
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 显示位置
		if(popLocView!=null){
			window.showAtLocation(popLocView, Gravity.BOTTOM, 0, 0);
		}
		//设置主窗体背景变暗
		WindowManager.LayoutParams lp=mActivity.getWindow().getAttributes();
		lp.alpha = 0.4f;
		mActivity.getWindow().setAttributes(lp);
	}

	/*
	 * 与头像设置有关的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.item_popupwindows_camera: { // 从相机获取
				window.dismiss();
				PicChoiceUtils.toCamera(mActivity);
				break;
			}
			case R.id.item_popupwindows_Photo: { // 从相册获取
				window.dismiss();
				PicChoiceUtils.toGallery(mActivity);
				break;
			}
			case R.id.item_popupwindows_cancel: {// 取消
				window.dismiss();
				Toast.makeText(mActivity, "取消", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}



	public String getPopAction() {
		return popAction;
	}

	public void setPopAction(String popAction) {
		this.popAction = popAction;
	}

}
