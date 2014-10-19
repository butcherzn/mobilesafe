package com.huyeye.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.ui.Setup2ItemView;

public class Setup2Activity extends BaseSetupActivity {

	//电话服务,可以读取sim卡信洗
	private TelephonyManager tm;
	
	
	// 定义自定组件
	private Setup2ItemView s2iv;

	// 定义SharedPreferences
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		s2iv = (Setup2ItemView) this.findViewById(R.id.s2iv_setup2);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//实例化电话服务
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//得到sim卡序列号
		// 自定义组件选择调整
		saveSimNum();

	}

	/**
	 * 单选框的变化
	 */
	private void saveSimNum() {
		
		boolean isSimBind = sp.getBoolean("is_sim_bind", false);
		if (isSimBind) {
			// 若为真，则表示被绑定，选择则被取消
			s2iv.setChecked(true);
			s2iv.setText("sim卡已经绑定");
		} else {
			s2iv.setChecked(false);
			s2iv.setText("sim卡没有绑定");
		}

		s2iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (s2iv.isChecked()) {
					editor.putBoolean("is_sim_bind", false);
					s2iv.setChecked(false);
					s2iv.setText("sim卡没有绑定");
					//若没有绑定，存空
					editor.putString("sim", null);			
					
				} else {
					editor.putBoolean("is_sim_bind", true);
					s2iv.setChecked(true);
					s2iv.setText("sim卡已经绑定");
					
					String simNum = tm.getSimSerialNumber();
					editor.putString("sim", simNum);
					
				}

				editor.commit();
			}
		});

	}

	/**
	 * 进入第三页面
	 */
	@Override
	public void showNext() {
		//取出是否绑定sim卡,没有绑定的不能下一步
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(Setup2Activity.this, "sim卡没有绑定", 1).show();
			return;
		}
		Intent intent = new Intent(Setup2Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	/**
	 * 进入第一页面
	 */
	@Override
	public void showPre() {
		Intent intent = new Intent(Setup2Activity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

}
