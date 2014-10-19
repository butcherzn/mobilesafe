package com.huyeye.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;

public class ShowToastStyleItem extends RelativeLayout {

	private TextView changedText;
	private ImageView showClicked;
	
	public void initCallAddressItem(Context context){
		View.inflate(context, R.layout.address_show_item_view, this);
		changedText = (TextView) this.findViewById(R.id.tv_show_change);
		showClicked = (ImageView) this.findViewById(R.id.iv_show_address);
		
		
		
	}
	
	
	public ShowToastStyleItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initCallAddressItem(context);
	}

	public ShowToastStyleItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCallAddressItem(context);
	}

	public ShowToastStyleItem(Context context) {
		super(context);
		initCallAddressItem(context);
	}
	


	
	/**
	 * 改变文字
	 */
	public void setText(String text){
		changedText.setText(text);
	}

}
