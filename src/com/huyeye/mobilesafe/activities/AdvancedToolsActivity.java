package com.huyeye.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huyeye.mobilesafe.R;

public class AdvancedToolsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced);
	
	}

	/**
	 * 点击事件
	 * @param v
	 */
	
	
	public void numberQuery(View v){
		Intent intent = new Intent(AdvancedToolsActivity.this,NumberAddressActivity.class);
		startActivity(intent);
	}
	
	
	
	
	public void smsBackup(View v){
		
	}
	
	
	public void smsRestore(View v){
		
	}
	
	
	
	
}
