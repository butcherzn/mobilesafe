package com.huyeye.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huyeye.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {

	private EditText phoneNum;

	// 定义SharedPreferences
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		phoneNum = (EditText) this.findViewById(R.id.et_setup3_phonenum);

		sp = getSharedPreferences("config", MODE_PRIVATE);

		String num = sp.getString("phonenum", null);

		phoneNum.setText(num);

	}

	@Override
	public void showNext() {
		String phoneNumber = phoneNum.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			Toast.makeText(Setup3Activity.this, "您还没有设置安全号码", 0).show();
			return;
		}

		Editor editor = sp.edit();
		// 保存安全号码
		editor.putString("phonenum", phoneNumber);
		editor.commit();
		// 跳转
		Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {

		Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	/**
	 * 选择联系人
	 */
	public void selectContact(View view) {
		Intent intent = new Intent(Setup3Activity.this,
				SelectContactActivity.class);
		startActivityForResult(intent, 100);
		// startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}

		String phone = data.getStringExtra("phone").replace("-", "");
		phoneNum.setText(phone);

	}

}
