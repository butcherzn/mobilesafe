package com.huyeye.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	/**
	 * 校验某个服务是否还活着
	 */
	
	public static boolean isServiceRunning(Context context,String serviceName){
		//校验服务是否还活着，ActivityManager可以管理Activity和Service，服务是一个没有界面的Act
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		//得到正在运行的服务
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for(RunningServiceInfo info : infos){
			String name = info.service.getClassName();
			if(name.equals(serviceName)){
				return true;
			}
		}
		return false;
	}
	
	
	
}
