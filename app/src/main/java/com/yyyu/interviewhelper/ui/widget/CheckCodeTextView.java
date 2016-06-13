package com.yyyu.interviewhelper.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yyyu.interviewhelper.R;

import java.util.HashSet;
import java.util.Random;

/**
 * 自定义的验证码View
 * @author yyyu
 */
public class CheckCodeTextView extends View {

	private static final String TAG = "MyTextView";

	private String mText;
	private int mTextColor;
	private int mtextBg;
	private float mTextSize;

	private Paint mPaint;

	private Rect mBounds;

	public CheckCodeTextView(Context context) {
		this(context, null);
	}

	public CheckCodeTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CheckCodeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.myTextView1, defStyleAttr, 0);
		mText = ta.getString(R.styleable.myTextView1_myText);
		if(TextUtils.isEmpty(mText)){
			mText=getRandom();
		}
		mTextColor = ta.getColor(R.styleable.myTextView1_myTextColor,
				Color.BLACK);
		mtextBg = ta.getColor(R.styleable.myTextView1_myTextBg, Color.WHITE);
		mTextSize = ta.getDimensionPixelSize(
				R.styleable.myTextView1_myTextSize, //
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
						getResources().getDisplayMetrics()));

		mPaint = new Paint();
		mBounds = new Rect();
		mPaint.setTextSize(mTextSize);
		mPaint.getTextBounds(mText, 0, mText.length(), mBounds);

		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mText = getRandom();
				postInvalidate();
			}
		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(true){
						Thread.sleep(10*1000);
						mText = getRandom();
						postInvalidate();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 生成随机四位数
	 *
	 * @return
	 */
	private String getRandom() {
		HashSet<Integer> set = new HashSet<Integer>();
		Random random = new Random();
		while (set.size() < 4) {
			int ran = random.nextInt(10);
			set.add(ran);
		}
		StringBuffer sb = new StringBuffer();
		for (Integer ran : set) {
			sb.append(ran);
		}
		return sb.toString();
	}

	/**
	 * 得到验证码数值
	 */
	public String getCodeValue(){
		return mText;
	}

	/**
	 * setMeasuredDimension(measureWidth, measureHeight);
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int measureWidth = 0;
		int measureHeight = 0;

		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		switch (specMode) {
		case MeasureSpec.EXACTLY:
			measureWidth = specSize + getPaddingLeft() + getPaddingRight();
			break;
		case MeasureSpec.AT_MOST:
			measureWidth = mBounds.width() + getPaddingLeft()
					+ getPaddingRight();
			break;
		default:
			break;
		}

		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		switch (specMode) {
		case MeasureSpec.EXACTLY:
			measureHeight = specSize + getPaddingTop() + getPaddingBottom();
			break;
		case MeasureSpec.AT_MOST:
			measureHeight = mBounds.height() + getPaddingLeft()
					+ getPaddingRight();
		default:
			break;
		}

		setMeasuredDimension(measureWidth, measureHeight);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setColor(mtextBg);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
		mPaint.setColor(mTextColor);
		canvas.drawText(mText, getWidth() / 2 - mBounds.width() / 2,
				getHeight() / 2 + mBounds.height() / 2, mPaint);
	}

}
