package com.huyeye.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import com.huyeye.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {
	protected static final String TAG = "Setup4Activity";

	private CheckBox checkBox;
	
	
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		checkBox = (CheckBox) this.findViewById(R.id.cb_setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}

	@Override
	public void showNext() {
		// 设置下次是否直接进入导航页面
		setNavi();
		// 跳转
		Intent intent = new Intent(Setup4Activity.this, LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		Intent intent = new Intent(Setup4Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	private void setNavi() {
		Editor editor = sp.edit();
		if(checkBox.isChecked()){
			
			
			editor.putBoolean("setuped", true);
			editor.putBoolean("safestates", true);
			
			Log.i(TAG, "------设置完成--");
		}else{
			editor.putBoolean("setuped", true);
			editor.putBoolean("safestates", false);
		}
		editor.commit();
		
	}

}
