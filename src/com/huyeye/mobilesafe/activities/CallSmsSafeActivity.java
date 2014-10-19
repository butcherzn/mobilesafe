package com.huyeye.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.db.dao.BlackNumberDao;
import com.huyeye.mobilesafe.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {
	protected static final String TAG = "CallSmsSafeActivity";
	private ListView list_callsmssafe;

	// DAO
	private BlackNumberDao dao;

	private List<BlackNumberInfo> blackInfos;

	//适配器
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 加载布局文件
		setContentView(R.layout.activity_callsmssafe);

		list_callsmssafe = (ListView) this.findViewById(R.id.list_callsmssafe);

		dao = new BlackNumberDao(this);

		
		adapter = new MyAdapter();
		
		blackInfos = dao.findAll();

		
		list_callsmssafe.setAdapter(adapter);

	}

	private class MyAdapter extends BaseAdapter {

		

		@Override
		public int getCount() {
			return blackInfos.size();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view;
			ViewHolder holder;
			// 减少内存中view对象创建的个数
			if (convertView == null) {
				view = View.inflate(CallSmsSafeActivity.this,
						R.layout.list_blacknum_item, null);
				holder = new ViewHolder();
				holder.tvBlackNum = (TextView) view
						.findViewById(R.id.tv_blacknum);
				holder.tvMode = (TextView) view.findViewById(R.id.tv_mode);
				
				holder.ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
				
				
				// 孩子生出来时找到引用，存放
				view.setTag(holder);

			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();// 效率提高5%
			}

			// 减少子孩子查询的次数
			String phone = blackInfos.get(position).getPhone();
			holder.tvBlackNum.setText(phone);

			String mode = blackInfos.get(position).getMode();
			if ("1".equals(mode)) {
				holder.tvMode.setText("拦截电话");
			} else if ("2".equals(mode)) {
				holder.tvMode.setText("拦截短信");
			} else {
				holder.tvMode.setText("拦截全部");
			}
			
			holder.ivDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i(TAG, "------------删除----------"+position);
					//创建对话框
					AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
					// 创建dialog
					final AlertDialog dialog = builder.create();
					View alertView = View.inflate(CallSmsSafeActivity.this, R.layout.dialog_delete_view, null);
					dialog.setView(alertView);
					//开启对话框
					dialog.show();
					TextView tvDelete = (TextView) alertView.findViewById(R.id.tv_delete_num);
					
					tvDelete.setText(blackInfos.get(position).getPhone());
					Button btDeleteEnter = (Button) alertView.findViewById(R.id.bt_delete_enter);
					Button btDeleteCancel = (Button) alertView.findViewById(R.id.bt_delete_cancel);
				
					btDeleteCancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					btDeleteEnter.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dao.delete(blackInfos.get(position).getPhone());
							//更新list
							blackInfos.remove(position);
							adapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
					
					

					
				}

			
			});
			
			
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

	/**
	 * View对象的容器 记录孩子的内存地址 相当于一个记事本
	 * 
	 * @author KingJoker
	 * 
	 */
	static class ViewHolder {
		TextView tvBlackNum;
		TextView tvMode;
		ImageView ivDelete;
	}

	/**
	 * 对话框的控件
	 */
	private EditText etBlackNum;
	private CheckBox cbPhone;
	private CheckBox cbSMS;
	private Button btEnter;
	private Button btCancel;

	// 添加的点击事件
	public void addBlack(View view) {
		showAddBlackDialog();
	}

	private void showAddBlackDialog() {
		AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
		// 创建dialog
		final AlertDialog dialog = builder.create();
		View view = View.inflate(CallSmsSafeActivity.this,
				R.layout.dialog_blacknum_view, null);

		dialog.setView(view, 0, 0, 0, 0);

		// 初始化控件
		etBlackNum = (EditText) view.findViewById(R.id.et_black);
		cbPhone = (CheckBox) view.findViewById(R.id.cb_phone);
		cbSMS = (CheckBox) view.findViewById(R.id.cb_sms);
		btEnter = (Button) view.findViewById(R.id.bt_enter);
		btCancel = (Button) view.findViewById(R.id.bt_cancel);

		// 确定按钮的点击事件
		btEnter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = etBlackNum.getText().toString().trim();
				String mode;
				//输入校验，若为空
				if(TextUtils.isEmpty(phone)){
					Toast.makeText(getApplicationContext(), "电话不能为空", 1).show();
					return;
				}
				if (cbPhone.isChecked() && cbSMS.isChecked()) {
					mode = "3";
				} else if (cbPhone.isChecked()) {
					// 拦截电话
					mode = "1";
				} else if (cbSMS.isChecked()) {
					mode = "2";
				} else {
					//都没有选择
					Toast.makeText(getApplicationContext(), "请选择拦截模式", 1).show();
					return;
				}
				// 插入操作
				dao.insert(phone, mode);
				BlackNumberInfo info = new BlackNumberInfo();
				info.setMode(mode);
				info.setPhone(phone);
				//添加到最后
//				blackInfos.add(info);
				blackInfos.add(0, info);
				//通知适配器数据改变了
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});

		// 取消按钮的点击事件
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}
	

}
