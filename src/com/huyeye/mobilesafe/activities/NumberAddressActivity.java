package com.huyeye.mobilesafe.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.db.dao.AddressDao;

public class NumberAddressActivity extends Activity {
	private EditText edNumberInput;
	private TextView tvNumberOutput;

	// 振动服务
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address);

		edNumberInput = (EditText) this.findViewById(R.id.et_address_inputnum);
		tvNumberOutput = (TextView) this.findViewById(R.id.tv_address_output);
		// 加权限
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		// 文本变化监听
		edNumberInput.addTextChangedListener(new TextWatcher() {
			/**
			 * 文本发生变化的时候，回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当文本大于等与3时查询数据库
				if (s != null && s.length() >= 3) {
					String location = AddressDao.findAddress(s.toString());
					if (!(TextUtils.isEmpty(location))) {
						tvNumberOutput.setTextColor(Color.BLACK);
						tvNumberOutput.setText("号码归属地为:" + location);
					} else {
						tvNumberOutput.setTextColor(Color.GREEN);
						tvNumberOutput.setText("暂未收录");
					}
				}
			}

			/**
			 * 文本发生变化前都回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			/**
			 * 变化后
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 点击查询
	 * 
	 * @param view
	 */
	public void numberAddressQurey(View view) {
		// 得到输入的号码
		String inputPhone = edNumberInput.getText().toString().trim();
		if (TextUtils.isEmpty(inputPhone)) {
			// 若输入为空
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			// shake.setInterpolator(new Interpolator() {
			// //函数 input = x getInterpolation = y
			// @Override
			// public float getInterpolation(float input) {
			// // TODO Auto-generated method stub
			// return 0;
			// }
			// });

			edNumberInput.startAnimation(shake);
			tvNumberOutput.setTextColor(Color.RED);

			tvNumberOutput.setText("号码为空，查你妹啊");

			// 当电话号码为空要震动,震动2秒
			vibrator.vibrate(2000);
			//振动参数,-1 不重复 0 一直循环 1从1循环
//			long[] pattern = {100,200,300,400};
//			vibrator.vibrate(pattern, 1);
//			vibrator.cancel();
			return;
		} else {
			// 若不为空
			// 查询数据库
			// 查询网络
			String location = AddressDao.findAddress(inputPhone);
			if (!(TextUtils.isEmpty(location))) {
				tvNumberOutput.setTextColor(Color.BLACK);
				tvNumberOutput.setText("号码归属地为:" + location);
			} else {
				tvNumberOutput.setTextColor(Color.GREEN);
				tvNumberOutput.setText("暂未收录");
			}

		}
	}

}
