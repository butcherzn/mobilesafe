package com.huyeye.mobilesafe.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huyeye.mobilesafe.R;
import com.huyeye.mobilesafe.services.CallAddressService;
import com.huyeye.mobilesafe.utils.Tools;

public class SplashActivity extends Activity {

	private static final String TAG = "SplashActivity";

	protected static final int SHOW_UPDATE_DIALOG = 0;
	protected static final int ENTERHOME = 1;
	protected static final int URL_ERROR = 2;
	protected static final int IO_ERROR = 3;
	protected static final int JSON_ERROR = 4;

	private TextView tv_splash_version;
	private RelativeLayout splash_root;
	private TextView tv_splash_download;

	private String description = null;
	private String apkurl = null;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// 得到控件
		tv_splash_version = (TextView) this
				.findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号："
				+ Tools.getVersionName(SplashActivity.this));

		tv_splash_download = (TextView) this
				.findViewById(R.id.tv_splash_download);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean isCheckUpdate = sp.getBoolean("update", false);



		if (isCheckUpdate) {
			// 检查更新
			checkUpdate();
		} else {
			// 不升级延迟
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}

		splash_root = (RelativeLayout) this.findViewById(R.id.splash_root);
		// 创建动画
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		// 动画时间
		aa.setDuration(500);
		splash_root.setAnimation(aa);
		// 释放资源，将所用的到数据库文件等/data/data/包名/files
		releaseDB();
		
		
//		//测试，开启归属地显示的服务
//		Intent startAddress = new Intent(this, CallAddressService.class);
//		startService(startAddress);

	}

	/**
	 * 将数据库文件 拷贝到 /data/data/包名/files文件下 初始化号码归属地的数据库
	 */
	private void releaseDB() {
		// 得到asset目录
		try {
			File file = new File(getFilesDir(), "address.db");
			if(file.exists()&&file.length()>0){
				Log.i(TAG, "-------------文件已经存在");
			}
			else{
				InputStream is = getAssets().open("address.db");
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}
			

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	// 创建Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Toast.makeText(SplashActivity.this, "需要升级", 0).show();
				// 创建对话框
				showUpdateDialog();

				break;

			case ENTERHOME:
				Toast.makeText(SplashActivity.this, "进入主页面", 0).show();
				enterHome();
				break;

			case URL_ERROR:
				Toast.makeText(SplashActivity.this, "网址出错", 0).show();
				enterHome();
				break;

			case IO_ERROR:
				Toast.makeText(SplashActivity.this, "流出错", 0).show();
				enterHome();
				break;

			case JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON解析出错", 0).show();
				enterHome();
				break;

			default:
				break;
			}

		}

	};

	/**
	 * 检查版本信息，并于服务器版本做比较，判断是否进行升级，返回handler消息，下载
	 */
	private void checkUpdate() {
		// 进行HTTP联网
		// 在子线程中
		new Thread() {

			public void run() {
				// 记录开始时间
				long startTime = System.currentTimeMillis();
				// 得到已有消息，用于handler判断不同状态
				Message msg = Message.obtain();
				try {
					// 获得服务器URL
					URL url = new URL(getString(R.string.serverurl));
					// 获得HTTP连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 请求方式为GET
					conn.setRequestMethod("GET");
					// 四秒时间超时
					conn.setReadTimeout(4000);
					// 得到响应码，若为200，则说明成功，这里是否完善检测网络
					Log.i(TAG, "----这里成功了---");
					int code = conn.getResponseCode();

					Log.i(TAG, "----响应码---");
					if (200 == code) {
						// 获取流
						Log.i(TAG, "----这里成功了2---");
						InputStream is = conn.getInputStream();
						// 解析流，并返回String
						String result = Tools.readFromStream(is);
						Log.i(TAG, "----升级文件url---" + result);
						// Json解析，创建一个JSONObject，使用传入字符串的构造方法
						JSONObject jsonObj = new JSONObject(result);

						String version = (String) jsonObj.get("version");
						description = (String) jsonObj.get("description");
						apkurl = (String) jsonObj.get("apkurl");

						// 比较服务器app版本和已安装版本
						if (Tools.getVersionName(SplashActivity.this).equals(
								version)) {
							// 相等怎进入主页面，发送消息
							msg.what = ENTERHOME;
						} else {
							// 若不等，则弹出对话框，提出升级或者进入主页面，发送消息
							msg.what = SHOW_UPDATE_DIALOG;
						}

					} else {
						// 若未连接到服务器，直接进入主页
						enterHome();
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_ERROR;

				} catch (IOException e) {
					e.printStackTrace();
					msg.what = IO_ERROR;

				} catch (JSONException e) {
					e.printStackTrace();
					msg.what = JSON_ERROR;
				} finally {
					// 结束时间
					long durTime = System.currentTimeMillis() - startTime;

					if (durTime < 2000) {
						try {
							Thread.sleep(2000 - durTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// 发送消息
					handler.sendMessage(msg);
				}

			};
		}.start();

	}

	protected void enterHome() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		// splash页面也取消
		finish();
	}

	protected void showUpdateDialog() {
		// 创建对话框
		AlertDialog.Builder buildAlert = new Builder(SplashActivity.this);
		// 头
		buildAlert.setTitle("提示升级");
		// 描述
		buildAlert.setMessage(description);

		// buildAlert.setCancelable(false); //强制升级

		// 设置返回键监听
		buildAlert.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				Log.i(TAG, "--------取消升级");
				enterHome();
				dialog.dismiss();
			}
		});

		buildAlert.setNegativeButton("直接升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Toast.makeText(SplashActivity.this, "升级", 0).show();
				// 下载
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 下载到SD卡上
					// afinal工具，第三方
					FinalHttp httpDown = new FinalHttp();
					httpDown.download(apkurl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/update.apk", new AjaxCallBack<File>() {

						// 下载失败
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Log.i(TAG, "--------下载失败");
							Toast.makeText(SplashActivity.this, "下载失败", 0)
									.show();
							enterHome();

						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							Log.i(TAG, "------正在下载");
							int progress = (int) (current * 100 / count);

							tv_splash_download.setText("已下载：" + progress + "%");

						}

						@Override
						public void onSuccess(File t) {
							super.onSuccess(t);
							Log.i(TAG, "------下载成功");
							Toast.makeText(SplashActivity.this, "下载成功", 0)
									.show();
							// 自动安装
							Tools.installAPK(SplashActivity.this, t);
						}

					});

				} else {
					// SD卡不存在
					Toast.makeText(SplashActivity.this, "SD卡已被卸载", 0).show();
					enterHome();

				}

			}
		});
		buildAlert.setPositiveButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
				dialog.dismiss();
			}
		});
		// 这个得记得创建
		buildAlert.create();

		buildAlert.show();
	}

}
