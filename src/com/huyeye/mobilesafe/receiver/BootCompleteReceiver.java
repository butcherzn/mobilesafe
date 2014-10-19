package com.huyeye.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * 第一次写BroadcastReceiver，先注册，写意图过滤器，重写onReceive方法 开机完毕后接收消息
 * 
 * @author KingJoker
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 初始化SharedPreferences
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

		boolean safestates = sp.getBoolean("safestates", false);
		// 得到是否开启手机防护
		if (safestates) {
			// 初始化TelephonyManager

			tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// 得到已经保存的sim序列号，绑定的
			String savedSim = sp.getString("sim", "");
			// 测试版本
//			String savedSim = sp.getString("sim", "")+"huyeye";
			// 得到现在的sim卡序列号
			String nowSim = tm.getSimSerialNumber();
			// 进行比较
			
			if (savedSim.equals(nowSim)) {
				// 若相等

			} else {
				// 若不等
				System.out.println("sim卡已经变更");
				Toast.makeText(context, "sim卡已经变更", 0).show();
				// 发短信给安全号码
				SmsManager smsManager = SmsManager.getDefault();
				String safeNum = sp.getString("phonenum", "");
				smsManager.sendTextMessage(safeNum, null, "sim changed", null, null);
				

			}
		}

	}

}
