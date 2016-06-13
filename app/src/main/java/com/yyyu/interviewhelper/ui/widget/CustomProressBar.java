package com.yyyu.interviewhelper.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yyyu.interviewhelper.R;


/**
 * 自定义View学习八：
 * 仿唯品会进度条
 * 
 * 遇到的坑：
 * 1.当画完圆环和圆弧后drawText时要把Paint reset一下，不然即使invalidate 任出现重叠
 * 所以以后最好是一种draw对应一种paint（也不知道什么原理，查了半天不查出来，以后来填坑）
 * 2.还有丫的数学忘光了，一个抛物线搞了半天
 *@date 2016-1-11 
 *@author yyyu
 */
@SuppressLint("DrawAllocation")
public class CustomProressBar extends View {

	private static final String TAG = "CustomVipProgresssBar";

	/**
	 * 圆环第一层的颜色
	 */
	private int firstColor;

	/**
	 * 圆环第二层的颜色
	 */
	private int secondColor;

	/**
	 * 圆环中间的logo内容
	 */
	private String centerText;

	/**
	 * logo字体颜色
	 */
	private int centerTextColor;
	
	/**
	 * logo字体大小
	 */
	private  int centerTextSize;

	/**
	 * 圆环的宽
	 */
	private float ringWdith;

	private float sweepAngle;

	private float acceleration = 0.2f;

	private float defRingWdith = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());

	private int firstDefColor = 0xFFEF9A9A;

	private int secondDefColor = 0xFFEF5350;

	private int defCenTextColor = 0xFFEF9A9A;

	private Paint paint;

	private float defTextSize = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

	private boolean isNext = false;

	public CustomProressBar(Context context) {
		this(context, null);
	}

	public CustomProressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomProressBar(Context context, AttributeSet attrs,
							int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
		update();
	}

	private void init(Context context, AttributeSet attrs) {
		// 得到自定义的属性值
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.pg_vip);
		firstColor = ta.getColor(R.styleable.pg_vip_vipFristColor,
				firstDefColor);
		secondColor = ta.getColor(R.styleable.pg_vip_vipSecondColor,
				secondDefColor);
		centerText = ta.getString(R.styleable.pg_vip_centerText);
		if (centerText == null) {
			centerText = "VICKY";
		}
		centerTextColor = ta.getColor(R.styleable.pg_vip_centerTextColor,
				defCenTextColor);
		centerTextSize = ta.getDimensionPixelSize(R.styleable.pg_vip_centerTextSize, (int)defTextSize);
		ringWdith = ta.getDimension(R.styleable.pg_vip_vipRingWdith,
				defRingWdith);
		ta.recycle();
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	private void update() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int progress = 0;
				while (true) {
					try {
						Thread.sleep(50);
						progress++;
						if (sweepAngle < 180) {
							sweepAngle = acceleration * progress * progress / 2;
							if (sweepAngle > 180)
								sweepAngle = 180;
						} else if (sweepAngle >= 180 && sweepAngle <= 360) {
							sweepAngle += acceleration
									* (progress - Math.sqrt(180 / acceleration))
									* (progress - Math.sqrt(180 / acceleration))
									/ 2;
						} else if (sweepAngle > 360) {
							isNext = !isNext;
							sweepAngle = 0;
							progress = 0;
						}
						postInvalidate();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Log.e(TAG, "onDraw----------");
		float pg_x = getWidth() / 2;// --中心点
		float pg_y = getHeight() / 2;
		float radius = Math.min(getWidth() / 2 - ringWdith, getHeight() / 2
				- ringWdith);
		// ---画背景圆环
		canvas.save();
		paint.setStyle(Style.STROKE);
		if (!isNext) {
			paint.setColor(firstColor);
		} else {
			paint.setColor(secondColor);
		}
		paint.setAntiAlias(true);
		paint.setStrokeWidth(ringWdith);
		canvas.drawCircle(pg_x, pg_y, radius, paint);
		if (isNext) {
			paint.setColor(firstColor);
		} else {
			paint.setColor(secondColor);
		}
		// ---画进度圆弧
		RectF oval = new RectF(pg_x - radius, pg_y - radius, pg_x + radius,
				pg_y + radius);
		canvas.drawArc(oval, -90, sweepAngle, false, paint);
		canvas.restore();
		// ---画中间的logo
		paint.reset();// --------重置
		paint.setTextSize(centerTextSize);
		Rect tBound = new Rect();
		paint.getTextBounds(centerText, 0, centerText.length(),tBound);
		paint.setColor(centerTextColor);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(centerText, pg_x, pg_y+tBound.height()/2, paint);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

}
