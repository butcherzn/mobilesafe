package com.huyeye.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;
/**
 * 将checkbox变为不能点，将整个自定义控件变为可点
 * @author KingJoker
 *
 */
public class SettingItemView extends RelativeLayout {

	
	private TextView tv_setting_view_isauto;
	private CheckBox cb_setting_view;
	
	
	private void iniView(Context context) {
		View.inflate(context, R.layout.setting_item_view, this);
		//点击后改变文字
		tv_setting_view_isauto = (TextView) this.findViewById(R.id.tv_setting_view_isauto);
		//checkbox
		cb_setting_view = (CheckBox) this.findViewById(R.id.cb_setting_view);
	}
	
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
	}

	public SettingItemView(Context context) {
		super(context);
		iniView(context);
	}

	/**
	 * 检验组合控件是否有焦点
	 * @return
	 */
	public boolean isChecked(){
		return cb_setting_view.isChecked();
	}
	
	
	/**
	 * 设置组合控件焦点
	 * 传入是否已经被选中
	 */
	public void setChecked(boolean checked)
	{
		cb_setting_view.setChecked(checked);
	}
	
	/**
	 * 动态改变字符串，下面灰字
	 */
	public void setText(String text){
		tv_setting_view_isauto.setText(text);
	}
	
	
	
	
	
	
	
	
	
}
