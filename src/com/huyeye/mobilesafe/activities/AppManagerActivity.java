package com.huyeye.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.domain.AppInfo;
import com.huyeye.mobilesafe.engines.AppInfoProvider;

public class AppManagerActivity extends Activity {

	// 控件
	private TextView tvRomSize;
	private TextView tvSdSize;
	private ListView lvApp;
	private LinearLayout llLoading;

	// 适配器
	private MyAdapter adapter;

	// List
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> sysAppInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		// 初始化控件
		tvRomSize = (TextView) this.findViewById(R.id.tv_app_rom);
		tvSdSize = (TextView) this.findViewById(R.id.tv_app_sd);
		lvApp = (ListView) this.findViewById(R.id.lv_app);
		llLoading = (LinearLayout) this.findViewById(R.id.ll_app_loading);

		// 功能1 显示可用空间
		String sdPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String romPath = Environment.getDataDirectory().getAbsolutePath();
		tvRomSize.setText("内存可用："
				+ Formatter.formatFileSize(this, getAvailableSpace(romPath)));
		tvSdSize.setText("外部存储可用："
				+ Formatter.formatFileSize(this, getAvailableSpace(sdPath)));

		fillData();

	}

	/**
	 * 加载ListView
	 */
	private void fillData() {
		// 功能2 列出手机上所安装的APP
		// 初始化适配器
		adapter = new MyAdapter();
		// 设置ProgressBar可视
		llLoading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				// 耗时操作
				// 初始化List
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				sysAppInfos = new ArrayList<AppInfo>();
				// 遍历appInfos
				for (AppInfo info : appInfos) {
					if (info.isUser()) {
						// 若为用户程序，则添加到用户List中
						userAppInfos.add(info);
					} else {
						// 若为系统程序，则添加到系统用户List中
						sysAppInfos.add(info);
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						lvApp.setAdapter(adapter);
						llLoading.setVisibility(View.INVISIBLE);

					}
				});
			};
		}.start();
	}

	/**
	 * 根据路径得到内存和外部存储的可用空间
	 * 
	 * @param path
	 * @return
	 */
	private long getAvailableSpace(String path) {
		StatFs sf = new StatFs(path);
		return sf.getAvailableBlocks() * sf.getBlockSize();
	}

	// ListView的适配器
	/**
	 * 内部类，适配器
	 * 
	 * @author KingJoker
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// return appInfos.size();
			return userAppInfos.size() + 1 + sysAppInfos.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 测试，先用一个TextView
			// TextView test = new TextView(AppManagerActivity.this);
			// test.setText(appInfos.get(position).getAppName());
			//
			// return test;
			// 将用户程序和系统程序分开，则需要重新定义位置
			AppInfo appInfo;
			if (0 == position) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户应用:" + userAppInfos.size() + "个");
				return tv;

			} else if (position == (userAppInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统应用:" + sysAppInfos.size() + "个");
				return tv;

			} else if (position <= userAppInfos.size()) {
				// ListView上面位置为用户程序
				int newPosition = position - 1;
				appInfo = userAppInfos.get(newPosition);
			} else {
				// 重新定义位置，因为系统List中也是从0开始，但现在position已经到了后面
				int newPosition = position - userAppInfos.size() - 1 - 1;
				appInfo = sysAppInfos.get(newPosition);
			}
			View view;
			ViewHolder holder;
			//不仅要检查是否为空还要检查是否为合适的对象服用
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(AppManagerActivity.this,
						R.layout.list_app_item, null);
				holder = new ViewHolder();
				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				holder.tv_app_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				view.setTag(holder);
			}

			// 设置APP图标
			holder.iv_app_icon.setImageDrawable(appInfo.getAppIcon());
			// 设置APP名称
			holder.tv_app_name.setText(appInfo.getAppName());

			if (appInfo.isInRom()) {
				holder.tv_app_location.setText("内部存储");
			} else {
				holder.tv_app_location.setText("外部存储");
			}

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

	static class ViewHolder {
		ImageView iv_app_icon;
		TextView tv_app_name;
		TextView tv_app_location;
	}

}
