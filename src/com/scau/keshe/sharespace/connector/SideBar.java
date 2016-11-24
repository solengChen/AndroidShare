package com.scau.keshe.sharespace.connector;


import com.scau.keshe.sharespace.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {

	private static String[] bar = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "#" };

	private int choose = -1;

	private Paint paint = new Paint();

	
	//��ĸ��ͼ��
	private TextView mtextDialog;
	
	

	public SideBar(Context context) {
		super(context);
		
		//set default textView
//		mtextDialog = (TextView) findViewById(R.id.dialog);
		
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public TextView getTextView() {
		return mtextDialog;
	}

	/**
	 * ΪSideBar������ʾ��ĸ��TextView
	 * 
	 * @param mTextDialog
	 */

	public void setTextView(TextView mtextDialog) {
		this.mtextDialog = mtextDialog;
	}
	
	/*
	 * dispatch����
	 * (non-Javadoc)
	 * @see android.view.View#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		final int action = event.getAction();
		
		final float ypot = event.getY();
		
		final int oldchoose = choose;
		
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		
		//��õ�ǰ���е���ĸλ��
		final int c = (int) ((ypot / this.getHeight() ) * bar.length);
		
		switch (action) {  
        case MotionEvent.ACTION_UP:  
            setBackgroundDrawable(new ColorDrawable(0x00000000));  
            choose = -1;//  
            invalidate();  
            if (mtextDialog != null) {  
                mtextDialog.setVisibility(View.INVISIBLE);  
            }  
            break;  
  
        default:  
            setBackgroundResource(com.scau.keshe.sharespace.R.drawable.sidebar_background);  
            if (oldchoose != c) {  
                if (c >= 0 && c < bar.length) {  
                    if (listener != null) {  
                        listener.onTouchingLetterChanged(bar[c]);  
                    }  
                    if (mtextDialog != null) {  
                        mtextDialog.setText(bar[c]);  
                        mtextDialog.setVisibility(View.VISIBLE);  
                    }  
                      
                    choose = c;  
                    invalidate();  
                }  
            }  
  
            break;  
        }  
		
		return true;
	}
	
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;  
	 /** 
    * ���⹫���ķ��� 
    *  
    * @param onTouchingLetterChangedListener 
    */  
   public void setOnTouchingLetterChangedListener(  
           OnTouchingLetterChangedListener onTouchingLetterChangedListener) {  
       this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;  
   }  
 
   /** 
    * �ӿ� 
    *  
    * @author coder 
    *  
    */  
   public interface OnTouchingLetterChangedListener {  
       public void onTouchingLetterChanged(String s);  
   }  
	
	
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 * setTypeface����������ʽ
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		int singleHeight = height / bar.length;
		
		for(int i = 0; i < bar.length; i ++) {
			paint.setColor(Color.rgb(28, 28, 28));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(20);
			
			//ѡ�����ĸ״̬
			if(i == choose) {
				paint.setColor(Color.parseColor("#ffffff"));
				paint.setFakeBoldText(true);	//���ô���
			}
			/*
			 * ��������x������� =��View���width - �����ȣ� / 2
			 */
			float xpot = ( width - paint.measureText(bar[i])) / 2;
			float ypot = (i + 1) * singleHeight;
			
			canvas.drawText(bar[i], xpot, ypot, paint);
			
			paint.reset();
		}
	}
}
