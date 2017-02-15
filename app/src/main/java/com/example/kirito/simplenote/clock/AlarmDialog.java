package com.example.kirito.simplenote.clock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;

import com.example.kirito.simplenote.MainActivity;
import com.example.kirito.simplenote.R;


public class AlarmDialog extends Activity {
	private MediaPlayer mp;
	private String note;
	private long pattern [] = {0,1000,1000,1000,1000,1000,1000};
	private Vibrator vibrator;
	private static final String TAG = "AlarmDialog";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		note = getIntent().getStringExtra("note");

		setWake();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		//根据指定的模式进行震动
		//第一个参数：该数组中第一个元素是等待多长的时间才启动震动，
		//之后将会是开启和关闭震动的持续时间，单位为毫秒
		//第二个参数：重复震动时在pattern中的索引，如果设置为-1则表示不重复震动
		vibrator.vibrate(pattern,-1);
		playMusic();
		showAlarmDialog();
	}

	//唤醒屏幕
	private void setWake(){
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				| PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,"TAG");
		wl.acquire();

		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock kl = km.newKeyguardLock("TAG");
		kl.disableKeyguard();
	}

			private void showAlarmDialog() {
				AlertDialog.Builder builder = new AlertDialog.Builder(this)
									.setMessage(note)
									.setTitle("note notification!")
									.setCancelable(true)
									.setPositiveButton("OK", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											mp.stop();
											mp.release();
											dialog.dismiss();
											AlarmDialog.this.finish();
										}
									});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		    
			private void playMusic() {
				mp = MediaPlayer.create(AlarmDialog.this, R.raw.b);
				mp.start();
			}

			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Intent intent = new Intent(this, MainActivity.class);
					startActivity(intent);
				}
				return super.onKeyDown(keyCode, event);
			}
}
