package com.huyeye.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.services.CallAddressService;
import com.huyeye.mobilesafe.services.CallSmsSafeService;
import com.huyeye.mobilesafe.ui.CallAddressItem;
import com.huyeye.mobilesafe.ui.SettingItemView;
import com.huyeye.mobilesafe.ui.ShowToastStyleItem;
import com.huyeye.mobilesafe.utils.ServiceUtils;

public class SettingActivity extends Activity {

	private SettingItemView settingUpdate;
	private CallAddressItem callAddress;
	private Intent callAddressServiceIntent;

	private CallAddressItem callSmsSafe;
	private Intent callSmsSafeServiceIntent;
	// 土司风格
	private ShowToastStyleItem showToastStyle;
	private TextView tvToastStyleText;

	// 对话框
	private AlertDialog alertDialog;

	// 对话框文本
	private static final String[] items = { "半透明", "活力橙", "金属灰", "苹果绿", "诱惑美女" };
	// 保存软件数据，是否更新
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 初始化自动更新控件
		settingUpdate = (SettingItemView) this.findViewById(R.id.siv_setting);
		// 初始化开启号码归属地查询控件
		callAddress = (CallAddressItem) this
				.findViewById(R.id.siv_call_address);
		// 初始化自定义土司背景控件
		showToastStyle = (ShowToastStyleItem) this
				.findViewById(R.id.stsi_show_style);

		tvToastStyleText = (TextView) showToastStyle
				.findViewById(R.id.tv_show_change);

		callSmsSafe = (CallAddressItem) this.findViewById(R.id.siv_call_safe);

		/**
		 * 自动更新开启代码
		 */
		boolean update = sp.getBoolean("update", false);
		if (update) {
			// 若为true则说明自动更新已经开启
			settingUpdate.setChecked(true);
			settingUpdate.setText("自动更新已开启");
		} else {
			// 自动更新已经关闭
			settingUpdate.setChecked(false);
			settingUpdate.setText("自动更新已关闭");
		}

		settingUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (settingUpdate.isChecked()) {
					// 若已经被选中，点击以后会变为未被选中
					settingUpdate.setChecked(false);
					settingUpdate.setText("自动更新已关闭");
					editor.putBoolean("update", false);
				} else {
					// 若未被选中，则反之
					settingUpdate.setChecked(true);
					settingUpdate.setText("自动更新已开启");
					editor.putBoolean("update", true);
				}

				editor.commit();

			}

		});

		/**
		 * 设置开启号码归属地电话代码
		 */
		// 设置开启关闭服务意图
		callAddressServiceIntent = new Intent(SettingActivity.this,
				CallAddressService.class);
		// 判断此服务是否开启
		boolean isOpenCallAddress = ServiceUtils.isServiceRunning(this,
				"com.huyeye.mobilesafe.services.CallAddressService");
		// 通过SP记录是否是开启状态
		// 下列代码为点开界面时，按钮所处状态，显示
		// boolean isOpenCallAddress = sp.getBoolean("open_call_address",
		// false);
		if (isOpenCallAddress) {
			callAddress.setChecked(true);
			callAddress.setText("已开启电话归属地查询服务");
		} else {
			callAddress.setChecked(false);
			callAddress.setText("已关闭电话归属地查询服务");
		}
		// 下列代码为开启服务代码
		callAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 开启记录的edit
				// Editor editor = sp.edit();
				if (callAddress.isChecked()) {
					// 如果被点击了，则开启服务
					callAddress.setChecked(false);
					callAddress.setText("已关闭电话归属地查询服务");
					stopService(callAddressServiceIntent);
					// 把状态记录到文件中
					// editor.putBoolean("open_call_address", false);
				} else {
					// 如果没有开启，点击会怎样
					callAddress.setChecked(true);
					callAddress.setText("已开启电话归属地查询服务");
					startService(callAddressServiceIntent);
					// editor.putBoolean("open_call_address", true);
				}
				// editor.commit();
			}
		});

		/**
		 * 更改土司风格代码
		 */
		// 保存后读取风格选项
		int which = sp.getInt("which", 0);
		tvToastStyleText.setText(items[which]);

		// 得到保存风格
		String toastStyle = sp.getString("toast_style", "半透明");
		showToastStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 创建一个对话框
				showToastStyleAlert();
			}
		});

		/**
		 * 开启短信拦截
		 */

		//设置文字
		callSmsSafe.setShowText("黑名单拦截服务");
		// 开启意图
		callSmsSafeServiceIntent = new Intent(SettingActivity.this,
				CallSmsSafeService.class);
		// 判断服务是否开启
		boolean isOpenCallSmsSafe = ServiceUtils.isServiceRunning(this,
				"com.huyeye.mobilesafe.services.CallSmsSafeService");
		if(isOpenCallAddress){
			//若为真，则为开启，则将checkbox设为开启状态
			callSmsSafe.setChecked(isOpenCallSmsSafe);
			callSmsSafe.setText("已激活黑名单服务");
		}else{
			//若为假，则说明服务关闭
			callSmsSafe.setChecked(isOpenCallSmsSafe);
			callSmsSafe.setText("已关闭黑名单服务");
		}
		//设置点击事件
		callSmsSafe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!callSmsSafe.isChecked()){
					//若没有开启，点击则为开启服务
					startService(callSmsSafeServiceIntent);
					callSmsSafe.setChecked(true);
					callSmsSafe.setText("已激活黑名单服务");
				}else{
					//若已开启，点击则为关闭服务
					stopService(callSmsSafeServiceIntent);
					callSmsSafe.setChecked(false);
					callSmsSafe.setText("已关闭黑名单服务");
				}
									
			}
		});
	}

	/**
	 * 选择土司风格的对话框
	 */
	protected void showToastStyleAlert() {
		AlertDialog.Builder builder = new Builder(SettingActivity.this);
		// View view = View.inflate(SettingActivity.this,
		// R.layout.list_toast_style, null);
		// builder.setView(view);
		int where = sp.getInt("which", 0);
		builder.setTitle("归属地提示框风格");
		// 第二个参数为点进去后选择的位置
		builder.setSingleChoiceItems(items, where,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor editor = sp.edit();
						// 保存选择的项目
						String chooseItem = items[which];

						tvToastStyleText.setText(chooseItem);
						editor.putInt("which", which);
						editor.commit();

						// 自带参数,靠
						dialog.dismiss();
					}

				});
		builder.create();
		builder.show();
	}

	/**
	 * 生命周期
	 */
	@Override
	protected void onResume() {
		super.onResume();
		boolean isOpenCallAddress = ServiceUtils.isServiceRunning(this,
				"com.huyeye.mobilesafe.services.CallAddressService");
		// 通过SP记录是否是开启状态
		// 下列代码为点开界面时，按钮所处状态，显示
		// boolean isOpenCallAddress = sp.getBoolean("open_call_address",
		// false);
		if (isOpenCallAddress) {
			callAddress.setChecked(true);
			callAddress.setText("已开启电话归属地查询服务");
		} else {
			callAddress.setChecked(false);
			callAddress.setText("已关闭电话归属地查询服务");
		}
		
		
		//重开的时候点击框，黑名单服务的点击框
		boolean isOpenCallSmsSafe = ServiceUtils.isServiceRunning(this,
				"com.huyeye.mobilesafe.services.CallSmsSafeService");
//		callSmsSafe.setChecked(isOpenCallSmsSafe);
		if(isOpenCallAddress){
			//若为真，则为开启，则将checkbox设为开启状态
			callSmsSafe.setChecked(isOpenCallSmsSafe);
			callSmsSafe.setText("已激活黑名单服务");
		}else{
			//若为假，则说明服务关闭
			callSmsSafe.setChecked(isOpenCallSmsSafe);
			callSmsSafe.setText("已关闭黑名单服务");
		}
	}
}
