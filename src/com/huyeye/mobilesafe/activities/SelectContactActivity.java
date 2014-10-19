package com.huyeye.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.domain.ContactInfo;
import com.huyeye.mobilesafe.engines.ContactInfoProvider;

public class SelectContactActivity extends Activity {
	private ListView lvContacts;
	private List<ContactInfo> infos;
	private LinearLayout llLoading;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		lvContacts = (ListView) this.findViewById(R.id.list_select_contact);
		llLoading = (LinearLayout) findViewById(R.id.ll_loading);
		llLoading.setVisibility(View.VISIBLE);
		
		
		new Thread() {
			public void run() {

				infos = ContactInfoProvider
						.getContactInfos(SelectContactActivity.this);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						llLoading.setVisibility(View.INVISIBLE);
						lvContacts.setAdapter(new ContactAdapter());
					}
				});

			};

		}.start();

		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = infos.get(position).getPhoneNum();
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(100, data);
				finish();

			}

		});

	}

	private class ContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View contactView;
			ViewHolder holder;
			//重复利用的机制，优化联系人
			if(convertView == null){
				contactView = View.inflate(SelectContactActivity.this,
						R.layout.item_list_contacts, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) contactView
						.findViewById(R.id.tv_item_name);
				holder.tvPhone = (TextView) contactView
						.findViewById(R.id.tv_item_phone);
				contactView.setTag(holder);
			}else{
				contactView = convertView;
				holder = (ViewHolder) contactView.getTag();
			}
			
			

			holder.tvName.setText(infos.get(position).getName());
			holder.tvPhone.setText(infos.get(position).getPhoneNum());

			return contactView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}
	
	
	/**
	 * ListView优化
	 */
	
	static class ViewHolder{
		TextView tvName;
		TextView tvPhone;
	}
	
	

}
