package com.huyeye.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.huyeye.mobilesafe.db.BlackNumberDBOpenHelper;
import com.huyeye.mobilesafe.domain.BlackNumberInfo;

/**
 * 数据库表名叫做blacknum
 * 
 * @author Joker
 * 
 */
public class BlackNumberDao {
	/**
	 * 黑名单数据库操作
	 */
	private BlackNumberDBOpenHelper helper;

	/**
	 * 构造DBHelper
	 * 
	 * @param context
	 */

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * 查找数据库是否存在所写号码
	 * 
	 * @param number
	 * @return
	 */
	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknum where phone=?",
				new String[] { number });
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;

	}
	
	/**
	 * 
	 * @param number 发件人号码
	 * @return 返回模式，1为电话拦截，2为短信拦截，3为全部拦截
	 */
	public String findMode(String number){
		String mode = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select mode from blacknum where phone = ?", new String[]{number});
		if(cursor.moveToNext()){
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		Log.i("DAO", "----------------------------成功"+mode);
		return mode;
		
		
	}
	

	/**
	 * 增加黑名单
	 * @return
	 */
	public long insert(String phone,String mode) {
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		SQLiteDatabase db = helper.getWritableDatabase();
		long result = db.insert("blacknum", null, values);		
		db.close();
		return result;

	}

	/**
	 * 改黑名单
	 * 
	 * @return
	 */
	public void updata(String number, String mode) {
		// boolean result = false;

		ContentValues values = new ContentValues();
		values.put("mode", mode);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.update("blacknum", values, "phone=?",
				new String[] {number});
		db.close();

	}

	/**
	 * 删
	 */
	public void delete(String phone) {
		// boolean result = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknum", "phone=?", new String[] { phone });
		db.close();

	}

	/**
	 * 列表所有黑名单
	 */

	public List<BlackNumberInfo> findAll() {

		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select phone,mode from blacknum order by _id desc", null);
//		Cursor cursor = db.query("blacknum", new String[]{"phone","mode"}, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			// 若不为空
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			String phone = cursor.getString(0);
			String mode = cursor.getString(1);
			blackNumberInfo.setPhone(phone);
			blackNumberInfo.setMode(mode);
			infos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		return infos;

	}

}
