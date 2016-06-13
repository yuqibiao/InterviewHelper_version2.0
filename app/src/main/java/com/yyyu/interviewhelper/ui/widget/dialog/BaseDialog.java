package com.yyyu.interviewhelper.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.utils.DimensChange;


/**
 * 自定义Dialog的base类
 * 
 *@date 2016-5-25
 *@author yyyu
 */
public abstract  class BaseDialog extends Dialog implements View.OnClickListener{
	
	private Context ctx ;
	protected WindowManager.LayoutParams lp;
	private View view;
	public BaseDialog(Context context) {
		this(context , R.style.dialog);
	}
	
	public BaseDialog(Context context, int theme) {
		super(context, R.style.dialog);
		this.ctx = context;
		lp = getWindow().getAttributes();
		this.setCanceledOnTouchOutside(false);//默认点击外面不可消失
	}
	
	/**
	 * 设置dialog界面
	 * @param layoutId
	 */
	protected void setMyContentView(int layoutId){
		view = LayoutInflater.from(ctx).inflate(layoutId, null);
		setContentView(view);
	};
	
	/**
	 * 得到Dialog界面上的控件
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends View>T  getView(int viewId){
		return (T) view.findViewById(viewId);
	}
	
	/**
	 * 得到需要点击的View(简化每次设置监听操作)
	 */
	@SuppressWarnings("unchecked")
	protected<T extends View>T getClickView(int viewId){
		T chilidView = (T) view.findViewById(viewId);
		chilidView.setOnClickListener(this);
		return chilidView;
	}
	
	/**
	 * 设置控件的监听
	 * @param viewIds
	 */
	protected void setMyClickListener(int ... viewIds){
		for(int viewId : viewIds){
			View chilidView =  view.findViewById(viewId);
			chilidView.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		
	};
	
}
