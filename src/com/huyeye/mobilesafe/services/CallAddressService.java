package com.huyeye.mobilesafe.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.db.dao.AddressDao;

/**
 * 去电来电 都能关，服务里开广播接收者
 * 
 * @author KingJoker
 * 
 */
public class CallAddressService extends Service {

	protected static final String TAG = "CallAddressService";

	// 窗体管理者
	private WindowManager wm;

	// 定义电话管理，调用监听
	private TelephonyManager tm;
	private MyPhoneListener listener;

	private CallRecevier receiver;

	// 土司窗体
	private TextView textView;

	//
	private View view;

	//
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 创建
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 初始化电话管理
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		listener = new MyPhoneListener();
		// 注册监听电话
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		// 代码里注册广播接收者
		receiver = new CallRecevier();
		// 意图匹配器
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		// filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		// 用代码取消注册广播接收者
		unregisterReceiver(receiver);
		receiver = null;
	}

	private WindowManager.LayoutParams params;
	/**
	 * 自定义土司
	 */
	public void myToast(String location) {
		view = View.inflate(this, R.layout.toast_show, null);
		textView = (TextView) view.findViewById(R.id.tv_toast_address);
		textView.setText(location);
		// 给view设置触摸监听器
		//1、手指按压得到初始位置，2、手指移动得到新的位置，3、算出偏移量，迅速更新view，4、将手指初始位置更新
		view.setOnTouchListener(new OnTouchListener() {
			float startX;
			float startY;		
			float newX;
			float newY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				

				switch (event.getAction()) {
				// 触摸开始
				case MotionEvent.ACTION_DOWN:

					// 手指位置
					startX = event.getRawX();
					startY = event.getRawY();
					Log.i(TAG, "------开始位置-----"+startX+","+startY);
					
					
					
					break;
				// 移动
				case MotionEvent.ACTION_MOVE:
					
					//新的位置
					newX = event.getRawX();
					newY = event.getRawY();
					Log.i(TAG, "------结束位置-----"+newX+","+newY);
					
					//得到偏移量
					float dX = newX - startX;
					float dY = newY - startY;
					
					//更新view，在自定义土司中有一个param.x，距离左边屏幕的位置，param.Y距离右边屏幕的位置，这玩意的参数距离
					params.x += (int) dX;
					params.y += (int) dY;
					//重新添加？？？？
					
					//做一个边界判断
					if(params.x<0){
						params.x=0;
					}
					if(params.y<0){
						params.y=0;
					}
					//窗体-控件
					if(params.x>wm.getDefaultDisplay().getWidth()-view.getWidth()){
						params.x=wm.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if(params.y>wm.getDefaultDisplay().getHeight()-view.getHeight()){
						params.y=wm.getDefaultDisplay().getHeight()-view.getHeight();
					}
					
					
					//更新
					wm.updateViewLayout(v, params);
					startX = event.getRawX();
					startY = event.getRawY();
					break;

				// 结束
				case MotionEvent.ACTION_UP:
					//保存当前位置，便于下次初始化位置
					Editor editor = sp.edit();
					editor.putInt("x", params.x);
					editor.putInt("y", params.y);
					editor.commit();
					
					
					
					break;
				default:
					break;
				}

				// 表示结束触摸事件，事件结束
				return true;
			}
		});

		// "半透明","活力橙","金属灰","苹果绿","诱惑美女"
		int[] which = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_gray,
				R.drawable.call_locate_green, R.drawable.toast_back };

		int where = sp.getInt("which", 0);

		// 设置背景
		view.setBackgroundResource(which[where]);

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// 位置
		params.gravity = Gravity.TOP;
		//初始化，用记录到的位置
		params.x = sp.getInt("x", 0);
		params.y = sp.getInt("y", 0);

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

		params.format = PixelFormat.TRANSLUCENT;
//		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		//电话优先级的权限
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);
	}

	/**
	 * 内部类
	 */
	private class MyPhoneListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {

			// 空闲时状态，来电拒绝，去电挂断
			case TelephonyManager.CALL_STATE_IDLE:
				if (textView != null) {
					wm.removeView(view);
				}

				break;

			// 响铃时状态
			case TelephonyManager.CALL_STATE_RINGING:
				String location = AddressDao.findAddress(incomingNumber);
				if (!(TextUtils.isEmpty(location))) {
					// 若非空
					// 土司
					// Toast.makeText(getApplicationContext(), location,
					// 1).show();
					myToast(location);
				}

				break;

			// 接通电话
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			default:
				break;
			}

		}

	}

	/**
	 * 服务里内部类，用代码注册广播接收者
	 * 
	 * @author KingJoker
	 * 
	 */
	private class CallRecevier extends BroadcastReceiver {

		private static final String TAG = "OutCallRecevier";

		@Override
		public void onReceive(Context context, Intent intent) {
			// 得到拨出去的号码
			String phone = getResultData();
			Log.i(TAG, "---------2342342343-----");
			Log.i(TAG, "--------------" + phone);
			String location = AddressDao.findAddress(phone);
			// 操啊
			if (!TextUtils.isEmpty(location)) {
				// 如果地址不空，土司出去
				// Toast.makeText(context, location + "操啊又是这个真假搞的啊操", 1).show();
				myToast(location);
			}
		}

	}

}
