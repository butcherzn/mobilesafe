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
public class Setup2ItemView extends RelativeLayout {

	private TextView changedText;
	private CheckBox showChecked;
	
	private void initSetup2Item(Context context) {
		View.inflate(context, R.layout.setup2_item_view, this);
		changedText = (TextView) this.findViewById(R.id.tv_setup2_view_isbind);
		showChecked = (CheckBox) this.findViewById(R.id.cb_setup2_view);
	
	}
	
	public Setup2ItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initSetup2Item(context);
	}

	

	public Setup2ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSetup2Item(context);
	}

	public Setup2ItemView(Context context) {
		super(context);
		initSetup2Item(context);
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
	
	
	
	
	
	
}
