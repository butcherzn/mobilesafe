package com.huyeye.mobilesafe.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.huyeye.mobilesafe.db.dao.AddressDao;

public class TestAddressDao extends AndroidTestCase {
	
	
	private static final String TAG = "TestAddressDao";

	public void test() throws Exception{
		String address = AddressDao.findAddress("15900297373");
		Log.i(TAG, "-----------"+address);
	}

}
