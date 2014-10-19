package com.huyeye.mobilesafe.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class SystemInfoUtils {

	/**
	 * 获取正在运行的进程数目
	 * 
	 * @param context
	 * @return
	 */
	public static int getRunningProcessCount(Context context) {
		// PackageManager //包管理器 相当于程序管理器 静态的
		// ActivityManager //进程管理器，管理活动的进程
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processinfos = am.getRunningAppProcesses();
		return processinfos.size();

	}

	/**
	 * 获取剩余可用内存
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailMem(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();

		am.getMemoryInfo(outInfo);

		return outInfo.availMem;
	}

	/**
	 * 获取总内存
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotelMem(Context context) {
		// ActivityManager am = (ActivityManager) context
		// .getSystemService(Context.ACTIVITY_SERVICE);
		// MemoryInfo outInfo = new MemoryInfo();
		//
		// am.getMemoryInfo(outInfo);
		//
		// return outInfo.totalMem;

		File file = new File("/proc/meminfo");
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			// MemTotal: 513492 kB
			String line = br.readLine();
			StringBuffer sb = new StringBuffer();
			for (char c : line.toCharArray()) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString())*1024;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

}
