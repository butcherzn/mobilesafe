package com.huyeye.mobilesafe.engines;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.domain.ProcessInfo;

/**
 * 提供手机里进程信息
 * 
 * @author Joker
 * 
 */
public class ProcessInfoProvider {

	public static List<ProcessInfo> getProcessInfo(Context context) {

		List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcessInfos = am
				.getRunningAppProcesses();

		PackageManager pm = context.getPackageManager();
		for (RunningAppProcessInfo processInfo : runningProcessInfos) {
			ProcessInfo info = new ProcessInfo();
			// 进程名为应用的包名
			String packname = processInfo.processName;
			// 占用的内存信息
			MemoryInfo[] memInfos = am
					.getProcessMemoryInfo(new int[] { processInfo.pid });
			long memsize = memInfos[0].getTotalPrivateDirty() * 1024;
			info.setMemSize(memsize);
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(
						packname, 0);
				Drawable icon = applicationInfo.loadIcon(pm);
				String name = applicationInfo.loadLabel(pm).toString();
				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					info.setUserProcess(true);
				} else {
					info.setUserProcess(false);
				}

				info.setPackname(packname);
				info.setName(name);
				info.setIcon(icon);

				processInfos.add(info);

			} catch (NameNotFoundException e) {
				e.printStackTrace();
				info.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
				info.setName(packname);
			}

		}// for
		return processInfos;
	}
}
