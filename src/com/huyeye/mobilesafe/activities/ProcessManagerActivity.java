package com.huyeye.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.domain.ProcessInfo;
import com.huyeye.mobilesafe.engines.ProcessInfoProvider;
import com.huyeye.mobilesafe.utils.SystemInfoUtils;

public class ProcessManagerActivity extends Activity {
	// 控件
	private TextView tv_process_count;
	private TextView tv_mem_info;
	private LinearLayout ll_process_loading;
	private ListView lv_process;

	private TextView tv_status;
	// List
	private List<ProcessInfo> taskInfos;
	private List<ProcessInfo> userTaskInfos;
	private List<ProcessInfo> sysTaskInfos;

	// 适配器
	private ProcessListAdapter adapter;

	// 假数据
	private int processCount;
	private long availMem;
	private long totelMem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);

		tv_process_count = (TextView) this.findViewById(R.id.tv_process_count);
		tv_mem_info = (TextView) this.findViewById(R.id.tv_mem_info);

		ll_process_loading = (LinearLayout) this
				.findViewById(R.id.ll_process_loading);

		lv_process = (ListView) this.findViewById(R.id.lv_process);

		fillData();
		// 用户进程系统进程
		tv_status = (TextView) this.findViewById(R.id.tv_status);
		// 设置ListView的滚动
		lv_process.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (userTaskInfos != null && sysTaskInfos != null) {
					if (firstVisibleItem > userTaskInfos.size()) {
						tv_status.setText("系统进程：" + sysTaskInfos.size() + "个");
					} else {
						tv_status.setText("用户进程：" + userTaskInfos.size() + "个");
					}
				}

			}
		});

		lv_process.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProcessInfo taskInfo = new ProcessInfo();

				if (position == 0) {
					return;
				} else if (position == userTaskInfos.size() + 1) {
					return;
				} else if (position <= userTaskInfos.size()) {
					int newPosition = position - 1;
					taskInfo = userTaskInfos.get(newPosition);
				} else {
					int newPosition = position - userTaskInfos.size() - 2;
					taskInfo = sysTaskInfos.get(newPosition);
				}
				// 若为自己
				if (getPackageName().equals(taskInfo.getPackname())) {
					return;
				}
				ViewHolder holder = (ViewHolder) view.getTag();
				if (taskInfo.isChecked()) {
					taskInfo.setChecked(false);
					holder.cb_status.setChecked(false);
				} else {
					taskInfo.setChecked(true);
					holder.cb_status.setChecked(true);
				}
			}

		});

	}

	private void setTitle() {
		processCount = SystemInfoUtils.getRunningProcessCount(this);
		tv_process_count.setText("运行中的进程：" + processCount + "个");
		availMem = SystemInfoUtils.getAvailMem(this);
		totelMem = SystemInfoUtils.getTotelMem(this);
		tv_mem_info.setText("剩余/总内存："
				+ Formatter.formatFileSize(this, availMem) + "/"
				+ Formatter.formatFileSize(this, totelMem));
	}

	/**
	 * 填充数据
	 */

	private void fillData() {
		ll_process_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				// 初始化进程信息，否则空指针异常
				taskInfos = ProcessInfoProvider
						.getProcessInfo(ProcessManagerActivity.this);
				userTaskInfos = new ArrayList<ProcessInfo>();
				sysTaskInfos = new ArrayList<ProcessInfo>();

				for (ProcessInfo tastInfo : taskInfos) {
					if (tastInfo.isUserProcess()) {
						// 真则为用户进程
						userTaskInfos.add(tastInfo);
					} else {
						// 系统进程
						sysTaskInfos.add(tastInfo);
					}
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ll_process_loading.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							// 初始化初始化
							adapter = new ProcessListAdapter();

							lv_process.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						setTitle();
					}
				});

			};
		}.start();
	}

	/**
	 * 数据适配器
	 */
	private class ProcessListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// return taskInfos.size();
			return userTaskInfos.size() + 1 ;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 测试文本框
			// TextView test = new TextView(ProcessManagerActivity.this);
			// test.setText("信息:"+taskInfos.get(position).toString());
			// return test;
			ProcessInfo taskInfo = new ProcessInfo();

			if (position == 0) {
				TextView tv = new TextView(ProcessManagerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户进程：" + userTaskInfos.size() + "个");
				return tv;
			} else if (position == userTaskInfos.size() + 1) {
				TextView tv = new TextView(ProcessManagerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统进程：" + sysTaskInfos.size() + "个");
				return tv;
			} else if (position <= userTaskInfos.size()) {
				int newPosition = position - 1;
				taskInfo = userTaskInfos.get(newPosition);

			} else {
				int newPosition = position - userTaskInfos.size() - 2;
				taskInfo = sysTaskInfos.get(newPosition);
			}

			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(ProcessManagerActivity.this,
						R.layout.list_process_item, null);
				// 在哪初始化是我的问题
				holder = new ViewHolder();
				holder.iv_process_icon = (ImageView) view
						.findViewById(R.id.iv_process_icon);
				holder.tv_process_size = (TextView) view
						.findViewById(R.id.tv_process_size);
				holder.tv_process_name = (TextView) view
						.findViewById(R.id.tv_process_name);

				holder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
				view.setTag(holder);
			}

			holder.iv_process_icon.setImageDrawable(taskInfo.getIcon());
			holder.tv_process_name.setText(taskInfo.getName());
			holder.tv_process_size.setText(String.valueOf(Formatter
					.formatFileSize(ProcessManagerActivity.this,
							taskInfo.getMemSize())));
			// 若为自己则选择狂看不见
			if (getPackageName().equals(taskInfo.getPackname())) {
				holder.cb_status.setVisibility(View.INVISIBLE);
			} else {
				// 必须写，复用？？？？
				holder.cb_status.setVisibility(View.VISIBLE);
			}
			holder.cb_status.setChecked(taskInfo.isChecked());
			return view;

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	static class ViewHolder {
		ImageView iv_process_icon;
		TextView tv_process_size;
		TextView tv_process_name;
		CheckBox cb_status;

	}

	/**
	 * 按钮点击事件
	 */

	/**
	 * 选择全部
	 * 
	 * @param v
	 */
	public void selectAll(View v) {
		for (ProcessInfo taskInfo : userTaskInfos) {
			if (getPackageName().equals(taskInfo.getPackname())) {
				continue;
			}

			taskInfo.setChecked(true);

		}
		for (ProcessInfo taskInfo : sysTaskInfos) {
			taskInfo.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 反选
	 * 
	 * @param v
	 */
	public void selectInverse(View v) {
		for (ProcessInfo taskInfo : userTaskInfos) {
			if (getPackageName().equals(taskInfo.getPackname())) {
				continue;
			}

			taskInfo.setChecked(!taskInfo.isChecked());
		}
		for (ProcessInfo taskInfo : sysTaskInfos) {
			taskInfo.setChecked(!taskInfo.isChecked());
		}

		adapter.notifyDataSetChanged();
	}

	/**
	 * 一键清理
	 * 
	 * @param v
	 */
	public void clean(View v) {

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		List<ProcessInfo> killedTasks = new ArrayList<ProcessInfo>();
		int count = 0;
		long saveMem = 0;
		for (ProcessInfo taskInfo : taskInfos) {
			// if(taskInfo.getPackname().equals(getPackageName())){
			// return;
			// }
			if (taskInfo.isChecked()) {// 被勾选结束
				am.killBackgroundProcesses(taskInfo.getPackname());
				if (taskInfo.isUserProcess()) {
					userTaskInfos.remove(taskInfo);
				} else {
					sysTaskInfos.remove(taskInfo);
				}
				killedTasks.add(taskInfo);
				count++;
				saveMem += taskInfo.getMemSize();
			}

		}
		taskInfos.removeAll(killedTasks);
		adapter.notifyDataSetChanged();
		Toast.makeText(
				ProcessManagerActivity.this,
				"杀死了"
						+ count
						+ "个进程，节省了"
						+ Formatter.formatFileSize(ProcessManagerActivity.this,
								saveMem) + "内存，手机又有活力了", 1).show();

		processCount -= count;
		availMem += saveMem;
		tv_process_count.setText("运行中的进程：" + processCount + "个");
		tv_mem_info.setText("剩余/总内存："
				+ Formatter.formatFileSize(this, availMem) + "/"
				+ Formatter.formatFileSize(this, totelMem));

		// for (ProcessInfo taskInfo : sysTaskInfos) {
		// if (taskInfo.isChecked()) {// 被勾选结束
		// am.killBackgroundProcesses(taskInfo.getPackname());
		// }
		// }
		// 更新界面
		// fillData();
	}

	/**
	 * 设置
	 */
	public void setting(View v) {

	}

}
