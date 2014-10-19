package com.huyeye.mobilesafe.activities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.receiver.MyAdmin;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	
	private TextView tvLostSafeNum;
	private ImageView ivLostLocked;
	private Button btActive;
	private TextView tvAttention;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		
		//得到SharedPreferences
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//得到设置页面配置
		boolean setuped = sp.getBoolean("setuped", false);
		
		boolean safestates = sp.getBoolean("safestates", false);
		
		//判断是否进入设置页面
		if(setuped){
			//若为真，则表示已经设置，直接进入手机防盗主页面
			setContentView(R.layout.activity_lostfind);
			tvLostSafeNum = (TextView) this.findViewById(R.id.tv_lost_safenum);
			
			ivLostLocked = (ImageView) this.findViewById(R.id.iv_lost_locked);
			//初始化激活按钮和文字
			btActive = (Button) this.findViewById(R.id.bt_lost_active);
			//初始化文字
			tvAttention = (TextView) this.findViewById(R.id.tv_lost_attention);
			
			
			
			if(safestates){

				tvLostSafeNum.setText(sp.getString("phonenum", null));
				ivLostLocked.setImageResource(R.drawable.lock);
			}else{
				tvLostSafeNum.setText("未设置");
				ivLostLocked.setImageResource(R.drawable.unlock);
			}
	
		}else {
			//若为假则进入设置向导
			Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
			
		}
		
		
	}
	
	
	public void reSetup(View view) {
		Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
		startActivity(intent);
	}
	
	
	
	public void active(View view){
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        
		ComponentName mDeviceAdminSample = new ComponentName(this, MyAdmin.class);

		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
		
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "激活此项功能，可以开启远程锁屏上密码，远程擦除数据等功能，保护隐私的最终防线");
        
        
        startActivity(intent);
        

        btActive.setVisibility(View.INVISIBLE);
        tvAttention.setVisibility(View.INVISIBLE);
        
	}
	
	
	
}
