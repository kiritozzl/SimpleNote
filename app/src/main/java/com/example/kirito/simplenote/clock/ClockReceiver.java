package com.example.kirito.simplenote.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ClockReceiver extends BroadcastReceiver {
	private static final String TAG = "ClockReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String note = intent.getStringExtra("note");
		Intent i = new Intent(context, AlarmDialog.class);
		i.putExtra("note",note);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
