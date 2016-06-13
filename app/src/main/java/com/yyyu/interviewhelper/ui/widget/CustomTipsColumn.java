package com.yyyu.interviewhelper.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yyyu.interviewhelper.R;


/**
 * 自定义控件学习七：
 * 工作需求自定义一个提示栏：
 *@date 2016-1-7 
 *@author yyyu
 */
public class CustomTipsColumn extends View{
	
	private static final String TAG="CustomTipsColumn";
	
	/**
	 * 提示内容
	 */
	private String tipsText;
	
	/**
	 * 提示字体大小
	 */
	private float tipsTextSize;
	
	/**
	 * 提示字体的颜色
	 */
	private int tipsColor;
	
	/**
	 * 线的颜色
	 */
	private int lineColor;
	
	/**
	 * 线宽
	 */
	private float lineHeight;
	
	/**
	 * 线的默认长度
	 */
	private float dftLineWdith=TypedValue.applyDimension
			(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
	
	/**
	 * 线与字的间距
	 */
	private float textSpace=TypedValue.applyDimension
			(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
	
	private static final int defaultColor=0xff000000;
	
	private Rect tBound;

	private Paint paint;
	
	public CustomTipsColumn(Context context) {
		this(context,null);
	}
	public CustomTipsColumn(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public CustomTipsColumn(Context context, AttributeSet attrs,
							int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//----得到自定义属性
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.myTipsColumn);
		tipsText = ta.getString(R.styleable.myTipsColumn_tipsText);
		tipsTextSize = ta.getDimension(R.styleable.myTipsColumn_tipsTextSize, //
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));
		tipsColor = ta.getColor(R.styleable.myTipsColumn_tipsColor, defaultColor);
		lineColor = ta.getColor(R.styleable.myTipsColumn_lineColor, defaultColor);
		lineHeight = ta.getDimension(R.styleable.myTipsColumn_lineHeight, 
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1 , getResources().getDisplayMetrics()));
		ta.recycle();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(tipsTextSize);
		tBound = new Rect();
		paint.getTextBounds(tipsText, 0, tipsText.length(), tBound);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int wdithMesureType = MeasureSpec.getMode(widthMeasureSpec);
		int heightMeasureType = MeasureSpec.getMode(heightMeasureSpec);
		int measueWdith = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
		int atMostWdith =0;
		int atMostHeight = 0;
		if(wdithMesureType==MeasureSpec.EXACTLY){
			dftLineWdith =(measueWdith-tBound.width()-2*textSpace) /2;
			//Log.e(TAG, "dftLineWdith : "+dftLineWdith);
		}
		atMostWdith = (int) (2*dftLineWdith+tBound.width()+getPaddingLeft()+getPaddingRight() +dftLineWdith) ;
		atMostHeight =(int) (Math.max(lineHeight, tBound.height())+getPaddingTop()+getPaddingBottom()) ;
		setMeasuredDimension(wdithMesureType==MeasureSpec.EXACTLY?measueWdith:atMostWdith,
				heightMeasureType==MeasureSpec.EXACTLY? measureHeight:atMostHeight);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//---画tips的字
		paint.setColor(tipsColor);
		canvas.drawText(tipsText, getWidth() / 2 - tBound.width() / 2,
				getHeight() / 2 + tBound.height() / 2 ,paint);
		//--画线
		paint.setColor(lineColor);
		float startX = getPaddingLeft();
		float startY = getHeight()/2;
		float stopX=startX+dftLineWdith;
		float stopY=startY;
		paint.setStrokeWidth(lineHeight);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
		startX=getWidth()-dftLineWdith;
		stopX = getWidth();
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}


}














