package com.huyeye.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class MD5Utils {

	public static String md5Encoder(String password){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			
			StringBuffer buffer = new StringBuffer();
			
			for(byte b : result){
				//每个字节与ff做与运算，返回int，转换成0x型
				int coder = b & 0xff;
				String str = Integer.toHexString(coder);
				//若str长度为1则在之前添加0
				if(str.length() == 1){
					buffer.append("0");
				}
				buffer.append(str);
			}
			//返回toString
			return buffer.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		
		
		
		
	}





}