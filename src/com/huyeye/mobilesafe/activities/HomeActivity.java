package com.huyeye.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {

	protected static final String TAG = "HomeActivity";
	private GridView list_home_function;
	private MyAdapter adapter;

	private SharedPreferences sp;

	// 列表内容
	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", "缓存清理", "高级工具", "设置中心"

	};

	private static int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// 设置getSharedPreferences
		sp = getSharedPreferences("config", MODE_PRIVATE);
		list_home_function = (GridView) this
				.findViewById(R.id.list_home_function);
		// 设置Adapter
		adapter = new MyAdapter();

		list_home_function.setAdapter(adapter);
		list_home_function.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {

				// 设置中心
				case 8:
					intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);

					break;

				// 高级工具
				case 7:
					intent = new Intent(HomeActivity.this,
							AdvancedToolsActivity.class);
					startActivity(intent);
					break;

				// 通讯卫士
				case 1:
					intent = new Intent(HomeActivity.this,
							CallSmsSafeActivity.class);
					startActivity(intent);
					break;

				// app管理
				case 2:
					intent = new Intent(HomeActivity.this,
							AppManagerActivity.class);
					startActivity(intent);
					break;
					
				case 3:
					intent = new Intent(HomeActivity.this,
							ProcessManagerActivity.class);
					startActivity(intent);
					break;	

				case 0:
					showLostFindDialog();
					break;
				default:
					break;
				}

			}

		});

	}

	// 对话框中的控件
	private EditText et_pwd;
	private EditText et_setpwd_confirm;
	private Button bt_submit;
	private Button bt_cancel;
	private AlertDialog alertDialog;

	/**
	 * 显示对话框，手机防盗，密码
	 */
	protected void showLostFindDialog() {
		if (isSetPwd()) {
			// 若已设置密码，弹出的是输入密码的对话框
			showEnterDialog();
		} else {
			// 若没有设置密码，则弹出设置密码的对话框
			showSetPwdDialog();
		}
	}

	/**
	 * 设置密码对话框 自定义布局文件
	 */
	private void showSetPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_setpwd_view, null);
		et_pwd = (EditText) view.findViewById(R.id.et_setpwd_pwd);
		et_setpwd_confirm = (EditText) view
				.findViewById(R.id.et_setpwd_confirm);
		bt_submit = (Button) view.findViewById(R.id.bt_setpwd_submit);
		bt_cancel = (Button) view.findViewById(R.id.bt_setpwd_cancel);

		// 取消键功能
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		// 确定键功能
		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pwd = et_pwd.getText().toString().trim();
				String confirmpwd = et_setpwd_confirm.getText().toString()
						.trim();
				// 密码框不能为空
				if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirmpwd)) {
					// 确定框是否非空
					Toast.makeText(HomeActivity.this, "不能为空", 0).show();
					return;
				}
				// 判断密码是否相同,确定并进入手机防盗页面
				if (pwd.equals(confirmpwd)) {
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5Encoder(pwd));
					editor.commit();
					alertDialog.dismiss();
					// 进入手机防盗页面
					enterLostFind();

				} else {
					Toast.makeText(HomeActivity.this, "两次输入不正确", 0).show();
					return;
				}

			}

		});

		// builder.setView(view);
		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();
	}

	/**
	 * 进入手机防盗页面
	 */
	protected void enterLostFind() {
		Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
		startActivity(intent);
	}

	/**
	 * 输入密码对话框
	 */
	private void showEnterDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_view,
				null);
		// 输入框
		et_pwd = (EditText) view.findViewById(R.id.et_enter_pwd);

		// 确定键
		bt_submit = (Button) view.findViewById(R.id.bt_enter_submit);

		// 取出取消键
		bt_cancel = (Button) view.findViewById(R.id.bt_enter_cancel);

		// 取消键功能
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		// 确定键功能

		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取出输入框中的值，与sp中的值做比较
				String spPassword = sp.getString("password", null);
				String pwd = et_pwd.getText().toString().trim();
				// 输入框不能为空
				if (TextUtils.isEmpty(pwd)) {
					Toast.makeText(HomeActivity.this, "密码不为空", 0).show();
					return;
				}
				// 比较密码，若相同则进入手机防盗页面
				if (spPassword.equals(MD5Utils.md5Encoder(pwd))) {
					// 相等
					Toast.makeText(HomeActivity.this, "进入页面", 0).show();
					alertDialog.dismiss();
					Log.i(TAG, "-----进入手机防盗页面");
					enterLostFind();
				} else {
					// 不等
					Toast.makeText(HomeActivity.this, "密码不不正确", 0).show();
					return;
				}

			}
		});

		// builder.setView(view);
		alertDialog = builder.create();
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();
	}

	/**
	 * 判断是否有密码
	 * 
	 * @return
	 */

	private boolean isSetPwd() {
		String password = sp.getString("password", null);
		// TextUtils.isEmpty判断字符串是否为空
		// if(TextUtils.isEmpty(password)){
		// return false;
		// }else {
		// return true;
		// }
		// 返回取反
		// 若为空 返回false，若为非空 返回true
		return !(TextUtils.isEmpty(password));

	}

	/**
	 * GridView适配器
	 * 
	 * @author KingJoker
	 * 
	 */
	private class MyAdapter extends BaseAdapter {
		// 得到多少个
		@Override
		public int getCount() {
			return names.length;
		}

		// 得到布局文件
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.item_list_home, null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			tv_item.setText(names[position]);
			iv_item.setImageResource(icons[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
