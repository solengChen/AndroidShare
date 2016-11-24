package com.scau.keshe.sharespace.firstpage;

import com.scau.keshe.sharespace.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class SearchEditText extends EditText implements OnFocusChangeListener,
		TextWatcher {

	private Drawable drawable;

	public SearchEditText(Context context) {
		this(context, null);
	}

	public SearchEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public SearchEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {

		drawable = this.getCompoundDrawables()[2];

		if (drawable == null) {
			drawable = this.getResources().getDrawable(
					R.drawable.emotionstore_progresscancelbtn);
		}
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		setClearIconVisiable(false);
		this.setOnFocusChangeListener(this);
		this.addTextChangedListener(this);
		this.clearFocus();

	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */

	private void setClearIconVisiable(boolean visiable) {
		Drawable right = null;
		if (visiable) {
			right = drawable;
		}
		this.setCompoundDrawables(this.getCompoundDrawables()[0],
				this.getCompoundDrawables()[1], right,
				this.getCompoundDrawables()[3]);
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore,
			int lengthAfter) {
		this.setClearIconVisiable(text.length() > 0);
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		this.setCursorVisible(true);

		if (this.getCompoundDrawables()[2] != null
				&& event.getAction() == MotionEvent.ACTION_UP) {
			float xpot = event.getX();

			float drawablepositionXL = this.getWidth() - this.getPaddingLeft()
					- drawable.getIntrinsicWidth();

			float drawablepositionXR = this.getWidth() - this.getPaddingLeft();

			boolean isIconTouch = ((xpot > drawablepositionXL) && (xpot < drawablepositionXR));

			if (isIconTouch) {
				this.setText("");
			}
		}
		String text = this.getText().toString();
		if (event.getAction() == MotionEvent.ACTION_UP && text != null
				&& text.length() != 0) {
			//开始搜索分享
			Log.i("search by text---------->", "input content = " + text);
			this.selectAll();
		}
		showPopitem.show();

		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			this.setClearIconVisiable(this.getText().length() > 0);
		} else {
			this.setClearIconVisiable(false);
		}
	}

	private showPopSortItemListener showPopitem;

	public void setShowPopSortItemListener(showPopSortItemListener listener) {
		this.showPopitem = listener;
	}

	public interface showPopSortItemListener {
		void show();

		void dismiss();
	}
}
