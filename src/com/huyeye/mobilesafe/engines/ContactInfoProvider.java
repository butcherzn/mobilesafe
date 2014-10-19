package com.huyeye.mobilesafe.engines;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.huyeye.mobilesafe.domain.ContactInfo;

public class ContactInfoProvider {

	private static final String TAG = "ContactInfoProvider";

	public static List<ContactInfo> getContactInfos(Context context) {

		List<ContactInfo> contactList = new ArrayList<ContactInfo>();

		// 得到内容解析
		ContentResolver resolver = context.getContentResolver();

		Uri rawUri = Uri.parse("content://com.android.contacts/raw_contacts");
		// 容易出错这里，字符串,最好复制
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		// 得到所有联系人的id信息
		Cursor cursor = resolver.query(rawUri, new String[] { "contact_id" },
				null, null, null);
		Log.i(TAG, "---------------data1------");

		// 遍历cursor，若非空
		while (cursor.moveToNext()) {
			// 得到第0列的所有数据

			ContactInfo contactInfo = new ContactInfo();
			String id = cursor.getString(0);
			if (id != null) {
				Log.i(TAG, "---------------id------" + id);
				// 通过得到的id信息从data表中得到mimetype和data两列
				Cursor dataCursor = resolver.query(dataUri, new String[] {
						"data1", "mimetype" }, "contact_id=?",
						new String[] { id }, null);
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						String name = data1;
						contactInfo.setName(name);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						String phoneNum = data1;
						contactInfo.setPhoneNum(phoneNum);
					}

					Log.i(TAG, "---------------data1------" + data1);
					Log.i(TAG, "---------------mimetype------" + mimetype);
				}
				contactList.add(contactInfo);
				dataCursor.close();
			}
		}
		cursor.close();

		return contactList;
	}

}
