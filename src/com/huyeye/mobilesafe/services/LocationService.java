package com.huyeye.mobilesafe.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {
	private LocationManager lm;
	private static final String TAG = "LocationService";
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);

		listener = new MyListener();
		// 设置参数,得到最好的位置提供者
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		String provider = lm.getBestProvider(criteria, true);

		lm.requestLocationUpdates(provider, 0, 0, listener);

	}

	private class MyListener implements LocationListener {

		

		// 当位置变化，调用的方法
		@Override
		public void onLocationChanged(Location location) {
			// 纬度
			String latitude = "latitude:" + location.getLatitude() + '\n';
			// 经度
			String longitude = "longitude:" + location.getLongitude() + '\n';
			// 高度
			// "高度："+location.getAltitude();
			// 精确度
			String accuracy = "accuracy:" + location.getAccuracy() + '\n';
			// 速度
			String speed = "speed:" + location.getSpeed();

			
			Log.i(TAG, "-----位置---------"+latitude);
			
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", latitude + longitude + accuracy
					+ speed);
			editor.commit();

		}

		// 当状态变化，没网到有网 是变化 , 从可用到不可用 不可用到可用
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		// 当位置提供者可用的时候
		@Override
		public void onProviderEnabled(String provider) {

		}

		// 当位置提供者不可用
		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
		listener = null;
	}

}
