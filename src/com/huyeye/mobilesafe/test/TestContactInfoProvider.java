package com.huyeye.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.huyeye.mobilesafe.domain.ContactInfo;
import com.huyeye.mobilesafe.engines.ContactInfoProvider;

public class TestContactInfoProvider extends AndroidTestCase {
	
	private final static String TAG = "TestContactInfoProvider";
	
	public void test() throws Exception{
		List<ContactInfo> infos = ContactInfoProvider.getContactInfos(getContext());
		
		for(ContactInfo info :infos){
			
			
			Log.i(TAG, "-----------"+info.toString());
		}
		
		
		
	}
	
	
}
