package com.huyeye.mobilesafe.engines;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.huyeye.mobilesafe.domain.AppInfo;

/**
 * 得到所有APP信息
 * 
 * @author KingJoker
 * 
 */
public class AppInfoProvider {

	public static List<AppInfo> getAppInfos(Context context) {

		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);

		for (PackageInfo packInfo : packInfos) {
			// 空指针异常
			AppInfo info = new AppInfo();
			String packName = packInfo.packageName;
			String appName = packInfo.applicationInfo.loadLabel(pm).toString();
			Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);

			info.setPackageName(packName);
			info.setAppName(appName);
			info.setAppIcon(appIcon);

			int flags = packInfo.applicationInfo.flags;
			//Value for flags: if set, this application is installed in the device's system image.
			if((flags & packInfo.applicationInfo.FLAG_SYSTEM) == 0){
				//为0则不为系统应用,则是用户应用
				info.setUser(true);
			}else{
				//则为系统应用
				info.setUser(false);
			}
			if((flags & packInfo.applicationInfo.FLAG_EXTERNAL_STORAGE) == 0){
				//若为0则存储在rom中
				info.setInRom(true);
			}else{
				info.setInRom(false);
			}
			
			
			
			
			
			appInfos.add(info);

		}

		return appInfos;
	}

}
