package com.huyeye.mobilesafe.services;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.huyeye.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSmsSafeService extends Service {

	private static final String TAG = "CallSmsSafeService";

	private InnoSmsReceiver innoSmsReceiver;
	private BlackNumberDao dao;

	private TelephonyManager tm;

	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 没有初始化dao啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊
		dao = new BlackNumberDao(this);

		// 初始化电话
		listener = new MyListener();
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 电话监听
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		// 初始化内部广播接收者
		innoSmsReceiver = new InnoSmsReceiver();
		// 代码注册广播接收者，调用registerReceiver
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(innoSmsReceiver, filter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销
		unregisterReceiver(innoSmsReceiver);
		innoSmsReceiver = null;
		// 关闭电话监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);

	}

	/**
	 * 广播接收者，内部，动态注册
	 */

	private class InnoSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "----------检查黑名单短信是否到来----");
			// 接收短信一定要记住这个
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {

				SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
				String sender = message.getOriginatingAddress();
				Log.i(TAG, "----------sender----" + sender);
				String mode = dao.findMode(sender);
				if ("2".equals(mode) || "3".equals(mode)) {
					Log.i(TAG, "----------拦截短信----");
					// 禁止广播
					abortBroadcast();
				}
				// 通过短信内容拦截,测试
				String body = message.getMessageBody();
				if (body.contains("fapiao")) {
					Log.i(TAG, "-------------发票短信拦截");
					abortBroadcast();
				}
			}

		}

	}

	/**
	 * 监听电话
	 * 
	 * @author KingJoker
	 * 
	 */
	private class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// 判断
				String mode = dao.findMode(incomingNumber);
				if ("1".equals(mode) || "3".equals(mode)) {
					Log.i(TAG, "----------挂断电话");
					// 删除呼叫记录
					// 联系人数据库
					// deleteCallLog(incomingNumber);
					
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,
							new CallLogObserver(incomingNumber, new Handler()));
					//挂断电话
					endCall();

				}
				break;

			default:
				break;
			}

		}

	}

	private class CallLogObserver extends ContentObserver {

		private String incomingNumber;
		public CallLogObserver(String incomingNumber, Handler handler) {
			super(handler);
			
			
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			
			Log.i(TAG, "----------数据库发生变化");
			deleteCallLog(incomingNumber);
			//注销
			getContentResolver().unregisterContentObserver(this);
			
			super.onChange(selfChange);
			
		}

	}

	public void endCall() {
		// ServiceManager.getService();
		try {
			// 加载ServiceManager的字节码
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass(
					"android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(binder).endCall();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 利用内容提供者删除呼叫记录
	 */
	public void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://call_log/calls");

		resolver.delete(uri, "number=?", new String[] { incomingNumber });
	}

}
