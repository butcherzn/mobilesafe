package com.huyeye.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程信息的业务Bean
 * @author Joker
 *
 */
public class ProcessInfo {
	private String packname;
	private String name;
	private Drawable icon;
	//true 为用户进程
	private boolean isUserProcess;
	private long memSize;
	
	private boolean checked;
	
	
	
	
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean isUserProcess() {
		return isUserProcess;
	}
	public void setUserProcess(boolean isUserProcess) {
		this.isUserProcess = isUserProcess;
	}
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	@Override
	public String toString() {
		return "ProcessInfo [packname=" + packname + ", name=" + name
				+ ", icon=" + icon + ", isUserProcess=" + isUserProcess
				+ ", memSize=" + memSize + "]";
	}
	
	

}
