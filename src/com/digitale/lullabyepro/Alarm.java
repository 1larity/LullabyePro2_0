package com.digitale.lullabyepro;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

	public class Alarm extends BroadcastReceiver {
		private final static String REMINDER_BUNDLE = "MyReminderBundle";
		private static String tag="Alarm";
		
		public void onReceive(Context context, Intent intent) {
			// here you can get the extras you passed in when creating the alarm
			Bundle extras = intent.getExtras();
			String flags = extras != null ? extras.getString(REMINDER_BUNDLE)
					: "nothing returned";
			if (Lullabye.debug) Log.d(tag,"alarm received "+flags+" intent "+intent);
			if (flags.equals("alarm")) {
				if (Lullabye.debug) Log.d(tag, "starting automatically ");
				// if repeat is true, set new alarm in 24 h (86 400seconds)
				if (Options.isMalarmRepeat()) {
					new Alarm(context, "alarm", (86400));
				}
				Lullabye.startplaying();
			} 
		}
		
		public Alarm(Context context, String flag, int timeoutInSeconds) {

			Log.d(tag, "creating alarm: flag " + flag + "starting in "
					+ (timeoutInSeconds));
			AlarmManager alarmMgr = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			PendingIntent pendingIntent = null;
			Intent alarmintent = new Intent(context, Alarm.class);

			

			alarmintent.putExtra(REMINDER_BUNDLE, flag);
			pendingIntent = PendingIntent.getBroadcast(context, 0, alarmintent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Calendar time = Calendar.getInstance();
			time.setTimeInMillis(System.currentTimeMillis());
			time.add(Calendar.SECOND, timeoutInSeconds);
			alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
					pendingIntent);

		}

	}

