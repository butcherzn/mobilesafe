package com.huyeye.mobilesafe.domain;

/**
 * 黑名单bean
 * 
 * @author Joker
 * 
 */
public class BlackNumberInfo {
	private String phone;
	private String mode;

	@Override
	public String toString() {
		return "BlackNumberInfo [phone=" + phone + ", mode=" + mode + "]";
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
