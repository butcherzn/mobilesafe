package com.huyeye.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * 面向对象真牛逼
 * 
 * @author KingJoker
 * 
 */
public abstract class BaseSetupActivity extends Activity {

	private GestureDetector detector;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 实例化手势识别
		// 实例化手势识别器
		detector = new GestureDetector(this, new SimpleOnGestureListener() {

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				if (e1.getRawX() - e2.getRawX() >= 100) {
					showNext();
					return true;
				}
				if (e2.getRawX() - e1.getRawX() >= 100) {
					showPre();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});

	}

	// 抽象方法，换页面
	public abstract void showPre();

	public abstract void showNext();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * 页面下一个
	 */
	public void next(View view) {
		showNext();
	}

	/**
	 * 一定要带一个View参数
	 * 
	 * @param view
	 */
	public void pre(View view) {
		showPre();
	}

}
