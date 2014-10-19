package com.huyeye.mobilesafe.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import com.huyeye.mobilesafe.db.BlackNumberDBOpenHelper;
import com.huyeye.mobilesafe.db.dao.BlackNumberDao;
import com.huyeye.mobilesafe.domain.BlackNumberInfo;

public class TestBlackDB extends AndroidTestCase {
	public void test() throws Exception {
		BlackNumberDBOpenHelper blackNumberDBOpenHelper = new BlackNumberDBOpenHelper(
				getContext());
		blackNumberDBOpenHelper.getWritableDatabase();
	}

	public void testInsert() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		Random random = new Random();
		for (int i = 10; i < 100; i++) {
			long result = dao.insert("139123456" + i,
					String.valueOf(random.nextInt(3) + 1));
			System.out.println("-----------resultInsert------"
					+ String.valueOf(result));
		}

	}

	public void testFind() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.find("13912345611");
		System.out.println("-----------result------" + String.valueOf(result));
	}
	
	
	public void testUpdate() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.updata("110", "2");
		
		
		
		
	}
	
	public void testFindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		infos = dao.findAll();
		for(BlackNumberInfo info:infos){
			System.out.println("----------------黑名单"+info.toString());
		}
		System.out.println("-------------长度"+infos.size());
	}
	
	
	public void testFindMode() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		String mode = dao.findMode("444");
		if(TextUtils.isEmpty(mode)){
			System.out.println("-----------------");
		}else{
			System.out.println("-----------------"+mode);
		}
		
		
		
	}

}
