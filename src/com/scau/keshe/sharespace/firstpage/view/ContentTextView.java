package com.scau.keshe.sharespace.firstpage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

public class ContentTextView extends TextView implements TextWatcher{

	private float mspacing = 0;
	private float mlinespacing = 0;
	private float mlineHeight = 0;
	
	private float mtextSize = 0;
	private int mtextColor = 0xffbbbbbb;
	private float mfontHeight = 0;

	private int mpaddingLeft = 0;
	private int mpaddingRight = 0;
	private int mpaddingTop = 0;
	private int mpaddingBottom = 0;
	
	private Context mcontext = null;
	private Paint mpaint = new Paint();
	private int mtextHeight = 1080;
	private int mtextWidth = 1920;

	private String mtext = "";
	
	public ContentTextView(Context context) {
		this(context, null);
	}

	public ContentTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ContentTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mcontext = context;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		init();
		if (mtext == null || mtext.equals("")) {
			return;
		}
		
		float x = mpaddingLeft;
		float y = (float) Math.ceil(mpaddingTop - mpaint.getFontMetrics().top);

		int length = mtext.length();
		int charindex = 0;
		int start = 0;
		int lineCount = 0;
		float lineWidth = 0;

		while (length > charindex) {
			char ch = mtext.charAt(charindex);
			String str = String.valueOf(ch);
			lineWidth += getWidthOfString(str, mpaint);

			if (mtextWidth < lineWidth || (charindex + 1) == length || str.equals("\n")) {
				if ((charindex + 1) == length || str.equals("\n")) {
					charindex++;
				}
				charindex--;
				lineWidth = 0;

				canvas.drawText(mtext.substring(start, charindex + 1), x, y
						+ lineCount * (mlineHeight + mlinespacing), mpaint);
				start = (charindex + 1);
				lineCount++;
			}
			charindex++;
		}
	}
	
	private void init() {
		mtextSize = this.getTextSize();
		mtextColor = this.getCurrentHintTextColor();
		mpaint.setAntiAlias(true); // 抗锯齿
		mpaint.setTextSize(mtextSize);
		mpaint.setColor(mtextColor);

		mtext = this.getText().toString();

		FontMetrics fmetrics = mpaint.getFontMetrics();
		mfontHeight = fmetrics.descent - fmetrics.ascent;
		mspacing = mlinespacing = mfontHeight / 4.0f;
		
		this.setLineSpacing(mspacing, 1.5f);
		
		mlineHeight = fmetrics.bottom - fmetrics.top;

		mpaddingLeft = this.getPaddingLeft();
		mpaddingRight = this.getPaddingRight();
		mpaddingTop = this.getPaddingTop();
		mpaddingBottom = this.getPaddingBottom();
		
		mtextWidth = this.getMeasuredWidth() - mpaddingLeft - mpaddingRight;
	}
	
	public static float getWidthOfString(String str, Paint paint) {
		if (str != null && str.equals("") == false && paint != null) {
			int length = str.length();
			float result = 0;

			float[] widths = new float[length];
			paint.getTextWidths(str, widths);
			for (float l : widths) {
				result += l;
			}
			return result;
		}
		return 0;
	}
	
	public int getTextWidth() {
		return mtextWidth;
	}
	
	private CharNumChangeListener listener;
	
	public void setCharNumChangeListener(CharNumChangeListener listener) {
		this.listener = listener;
	}
	
	public interface CharNumChangeListener {
		void flashCharCountNum();
	}
	
	@Override
	public void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		if(text.length() > 0){
			listener.flashCharCountNum();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 如果有换行符，这一行的字符数为每行最大字符填充数
	 * @return
	 */
	public int getCharNum() {
		return 0;
	}
}
