package com.scau.keshe.sharespace.welcome.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyInputEditText extends EditText implements TextWatcher {

	public MyInputEditText(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public MyInputEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
		// TODO Auto-generated constructor stub
	}

	public MyInputEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.addTextChangedListener(this);
	}
	
	@Override
	public void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		if(listener == null) {
			return;
		}
		if(text.length() > 0) {
			listener.buttonIsFocusable();
		}
		else {
			listener.buttonUnFocusable();
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
	
	private ButtonFocusableListener listener;
	
	public void setButtonFCListener(ButtonFocusableListener listener) {
		this.listener = listener;
	}
	public interface ButtonFocusableListener {
		void buttonIsFocusable();
		void buttonUnFocusable();
	}

}
