package com.huyeye.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class Tools {

	/**
	 * 检查应用版本
	 * 
	 * @param context
	 * @return 版本号
	 */

	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			String versionName = pi.versionName;
			return versionName;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 输入输入流，并读出返回String
	 * 
	 * @param is
	 *            输入流
	 * @return
	 * @throws IOException
	 */
	public static String readFromStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		// 切忌关掉，操这错误
		is.close();
		// 写在输出流中了，不是buffer，javaIO是个问题
		String result = baos.toString();
		baos.close();

		return result;
	}

	/**
	 * 自动安装已下载的安装包
	 * 
	 * @param context
	 * @param t
	 */
	public static void installAPK(Context context, File t) {

		// 安装需要意图
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(t),
				"application/vnd.android.package-archive");
		context.startActivity(intent);

	}



}
