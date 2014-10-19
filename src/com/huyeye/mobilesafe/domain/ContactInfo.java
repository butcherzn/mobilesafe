package com.huyeye.mobilesafe.domain;
/**
 * 联系人，实体类
 * @author KingJoker
 *
 */
public class ContactInfo {
	private String name;
	private String phoneNum;
	
	
	
	@Override
	public String toString() {
		return "ContactInfo [name=" + name + ", phoneNum=" + phoneNum + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	
	
	
}
