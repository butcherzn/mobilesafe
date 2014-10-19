package com.huyeye.mobilesafe.receiver;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.services.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;

/**
 * 短信接收
 * 
 * @author KingJoker
 * 
 */
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	private SharedPreferences sp;
	//设备管理员权限
	private DevicePolicyManager dmp;
	//短信管理
	private SmsManager smsManager;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);

		smsManager = SmsManager.getDefault();
		//初始化设备管理员权限
		dmp = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		boolean safestates = sp.getBoolean("safestates", false);
//		String safeNum = sp.getString("phonenum", null);

		if (safestates) {
			Log.i(TAG, "-----------获得短信指令");

			Object[] objs = (Object[]) intent.getExtras().get("pdus");

			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();
				String body = smsMessage.getMessageBody();

				if ("#*location*#".equals(body)) {
					Log.i(TAG, "-----------获得位置");
					Intent serviceLocation = new Intent(context,LocationService.class);
					context.startService(serviceLocation);
					
					String location = sp.getString("location", null);
					if(TextUtils.isEmpty(location)){
						//位置没得到
						smsManager.sendTextMessage(sender, null, "location", null, null);
					}else{
						smsManager.sendTextMessage(sender, null, location, null, null);
					}
					abortBroadcast();

				} else if ("#*alarm*#".equals(body)) {
					Log.i(TAG, "-----------播放报警音乐");

					MediaPlayer mediaPlayer = MediaPlayer.create(context,
							R.raw.alarm);
					mediaPlayer.setLooping(true);
					mediaPlayer.setVolume(1.0f, 1.0f);
					mediaPlayer.start();
					abortBroadcast();

				} else if ("#*wipedata*#".equals(body)) {
					Log.i(TAG, "-----------消除数据");
					dmp.wipeData(0);
					abortBroadcast();

				} else if ("#*lockscreen*#".equals(body)) {
					

					
					dmp.lockNow();
					dmp.resetPassword("1234", 0);
										
					Log.i(TAG, "-----------一键锁屏");
					
					
					
					
					
					
					
					
					abortBroadcast();

				}

			}

		}

	}

}
