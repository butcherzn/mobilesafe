package com.huyeye.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 电话归属地数据库查询
 * 
 * @author KingJoker
 * 
 */
public class AddressDao {

	private final static String PATH = "/data/data/com.huyeye.mobilesafe/files/address.db";

	public static String findAddress(String number) {
		String location = null;
		SQLiteDatabase addressDb = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		// 手机正则表达式
		if (number.matches("^1[34568]\\d{9}$")) {

			Cursor cursor = addressDb
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				location = cursor.getString(0);

			}
			cursor.close();

		} else {
			switch (number.length()) {
			case 3:
				if ("110".equals(number))
					location = "你懂的";
				break;
			case 4:

				break;
			case 7:

				break;
			case 8:

				break;

			default:
				if (number.length() >= 10 && number.startsWith("0")) {
					Cursor cursor = addressDb.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 3) });
					if (cursor.moveToNext()) {
						String tempLocation = cursor.getString(0);
						location = tempLocation.substring(0, tempLocation.length()-2);
						

					}
					cursor.close();
					cursor = addressDb.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						String tempLocation = cursor.getString(0);
						location = tempLocation.substring(0, tempLocation.length()-2);

					}
					cursor.close();

				}

				break;
			}
		}
		addressDb.close();
		return location;

	}

}
