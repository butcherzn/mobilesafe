package com.huyeye.mobilesafe.ui;

import com.huyeye.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CallAddressItem extends RelativeLayout {

	private TextView changedText;
	private CheckBox showChecked;
	private TextView showText;
	
	public void initCallAddressItem(Context context){
		View.inflate(context, R.layout.call_item_view, this);
		changedText = (TextView) this.findViewById(R.id.tv_call_change);
		showChecked = (CheckBox) this.findViewById(R.id.cb_call_view);
		showText = (TextView) this.findViewById(R.id.tv_call_view);
		
		
	}
	
	
	public CallAddressItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initCallAddressItem(context);
	}

	public CallAddressItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCallAddressItem(context);
	}

	public CallAddressItem(Context context) {
		super(context);
		initCallAddressItem(context);
	}
	

	/**
	 * 是否已被选中
	 */
	public boolean isChecked(){		
		return showChecked.isChecked();
	}
	
	/**
	 * 选择
	 */
	public void setChecked(boolean checked){
		showChecked.setChecked(checked);	
	}
	
	/**
	 * 改变文字
	 */
	public void setText(String text){
		changedText.setText(text);
	}
	
	/**
	 * 设置演示文字
	 */
	public void setShowText(String text){
		showText.setText(text);
	}

}
