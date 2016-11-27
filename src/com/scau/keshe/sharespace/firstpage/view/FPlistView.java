package com.scau.keshe.sharespace.firstpage.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scau.keshe.sharespace.R;

public class FPlistView extends ListView implements OnScrollListener {
	private LayoutInflater inflater;
	private View header;
	private int headerHeight;
	private int firstVisibleItem;

	private boolean isFrashable; // �ڽ��涥���������
	private int primaryY; // ������������Y
	private volatile int scrollState; // ��ǰ����״̬

	private TextView tip;
	private ImageView arrow;
	private ProgressBar progress;

	/**
	 * ״̬��ʾ��pullʱ��ʾ������ˢ�� releaseʱ��ʾ�ɿ���ˢ�� refrushingʱ��ʾ����ˢ��
	 * 
	 * @author ShouLun
	 * 
	 */
	private enum Status {
		NONE, PULL, RELEASE, REFLASHING
	}

	private Status status = Status.NONE;

	public FPlistView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public FPlistView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public FPlistView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		inflater = LayoutInflater.from(context);

		header = inflater.inflate(R.layout.fragment_first_header_frush, null);

		measureView(header);
		headerHeight = header.getMeasuredHeight();
		topPadding(-headerHeight);

		this.addHeaderView(header);
		// this.setOnItemClickListener(itemclick);
		this.setOnScrollListener(this);

		tip = (TextView) header.findViewById(R.id.fragment_header_text);

		arrow = (ImageView) header.findViewById(R.id.fragment_header_arrow);
		progress = (ProgressBar) header
				.findViewById(R.id.fragment_header_progress);
	}

	/**
	 * ֪ͨ�����֣�ռ�õĿ��
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams vp = view.getLayoutParams();
		if (vp == null) {
			vp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, vp.width);
		int height;
		int tempHeight = vp.height;
		if (tempHeight > 0) {
			// ��Ϊ��ʱ��䲼��
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/**
	 * ����header�����ϱ߾�
	 * 
	 * @param topPadding
	 */
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		if(listener != null) {
			listener.listScrollStatus(scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// ���firstVisibleItem = 0���ж������
		this.firstVisibleItem = firstVisibleItem;
		int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
		
		if(listener != null) {
			listener.listScrollPosition(firstVisibleItem, visibleItemCount);
		}
	}

	/**
	 * ���ж��Ƿ��ڽ�����ˣ������ж����������������ص�ˢ��
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		reflashlistener.popTabDismiss();
		if (firstVisibleItem != 0) {
			return super.onTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			isFrashable = true;
			primaryY = (int) ev.getY();

			break;

		case MotionEvent.ACTION_MOVE:
			onActionMove(ev);
			break;

		case MotionEvent.ACTION_UP:
			if (status == Status.RELEASE) {
				status = Status.REFLASHING;
				// ��ʾ����ˢ�²�����ˢ�²���
				refreshViewByState();
				reflashlistener.onReflash();

			} else if (status == Status.PULL) {
				status = Status.NONE;
				isFrashable = false;
				refreshViewByState();
			}

			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * ����ˢ����ɺ���
	 */
	public void reflashComplete() {
		status = Status.NONE;
		isFrashable = false;
		refreshViewByState();

		TextView lastupdatetime = (TextView) header
				.findViewById(R.id.fragment_header_lastupdate_time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastupdatetime.setText(time);
	}

	/**
	 * �ƶ��������ж� �Դ���ʾ����������ˢ�¡� ���ǡ��ɿ�ˢ�¡�
	 * 
	 * @param ev
	 */
	private void onActionMove(MotionEvent ev) {
		if (!isFrashable) {
			return;
		}
		int currentY = (int) ev.getY();
		int distance = currentY - primaryY;
		int topPadding = distance - headerHeight;

		switch (status) {
		case NONE:
			if (distance > 0) {
				status = Status.PULL;
				refreshViewByState();
			}
			break;

		case PULL:
			topPadding(topPadding);
			if (distance > (headerHeight + 30)
					&& this.scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				status = Status.RELEASE;
				refreshViewByState();
			}
			break;

		case RELEASE:
			topPadding(topPadding);
			if (distance <= (headerHeight + 30)) {
				status = Status.PULL;
				refreshViewByState();
			} else if (distance <= 0) {
				status = Status.NONE;
				isFrashable = false;
				refreshViewByState();
			}
			break;

		case REFLASHING:
			// ʲô������
			break;
		}
	}

	/**
	 * 
	 */
	private void refreshViewByState() {

		RotateAnimation anim0 = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim0.setDuration(500);
		anim0.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);

		switch (status) {
		case NONE:
			arrow.clearAnimation();
			topPadding(-headerHeight);
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			tip.setText("��������ˢ��!");
			break;
		case RELEASE:
			Log.i("�ɿ�����ˢ��", "");
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			arrow.clearAnimation();
			arrow.setAnimation(anim0);
			tip.setText("�ɿ�����ˢ��!");
			break;
		case REFLASHING:
			topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			arrow.clearAnimation();
			tip.setText("����ˢ��...");
			break;
		}
	}

	private OnItemClickListener itemclick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * ����ˢ�½ӿ���
	 * 
	 * @author ShouLun
	 * 
	 */
	public interface ReflashListener {
		void onReflash();

		void popTabDismiss();
	}

	private ReflashListener reflashlistener;

	public void setReflashListener(ReflashListener reflashlistener) {
		this.reflashlistener = reflashlistener;
	}

	
	/**
	 * �ص��ӿ��࣬����adapter��������
	 */
	private ListScrollStatusListener listener;

	public void setListScrollStatusListener(ListScrollStatusListener listener) {
		this.listener = listener;
	}

	public interface ListScrollStatusListener {
		void listScrollPosition(int start, int end);
		void listScrollStatus(int status);
	}
}
