package com.huyeye.mobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;

import com.huyeye.mobilesafe.R;


public class Setup1Activity extends BaseSetupActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);

	}


	@Override
	public void showPre() {	}

	@Override
	public void showNext() {
		Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();	
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	
}
