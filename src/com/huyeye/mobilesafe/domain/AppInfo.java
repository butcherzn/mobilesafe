package com.huyeye.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private String packageName;
	private String appName;
	private Drawable appIcon;
	private boolean isInRom;
	private boolean isUser;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public boolean isInRom() {
		return isInRom;
	}

	public void setInRom(boolean isInRom) {
		this.isInRom = isInRom;
	}

	public boolean isUser() {
		return isUser;
	}

	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	@Override
	public String toString() {
		return "AppInfo [packageName=" + packageName + ", appName=" + appName
				+ ", appIcon=" + appIcon + ", isInRom=" + isInRom + ", isUser="
				+ isUser + "]";
	}

}
