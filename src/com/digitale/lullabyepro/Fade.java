package com.digitale.lullabyepro;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


	public class Fade extends BroadcastReceiver {
		private static String tag="fade";
		private final static String REMINDER_BUNDLE = "MyReminderBundle";
		public void onReceive(Context context, Intent intent) {
			// here you can get the extras you passed in when creating the alarm
			Bundle extras = intent.getExtras();
			String flags = extras != null ? extras.getString(REMINDER_BUNDLE)
					: "nothing returned";
			if (Lullabye.debug)  Log.d(tag,"alarm received "+flags+" intent "+intent);
		 if (flags.equals("fade_out")) {
				Log.d(tag, "fade recieved ");
				fadeout();
			}
		}
		
		
		 void fadeout() {
			// fields for fade operations
			// note when the user set the fade
//			long fadeTimerSetAt;
			
			// master volume at start of fade operation
			final float mstartVolume;
			
			// how many seconds to fade out
//			int mfadeOut = 3600;
			mstartVolume = Lullabye.mSoundscapeVolume;
			Lullabye.mfadeInProgress = true;
			new Timer().scheduleAtFixedRate(new TimerTask() {
				// attenuation, how much to reduce volume by
				float attenuation = 0.0f;
				 public void run() {
					
					
					SoundManager.changeAllVolume(attenuation);
					// increase level of attenuation
					attenuation = attenuation + 0.01f;
					// set master volume temporarily so new sounds are created with
					// correct faded volume
					Lullabye.mSoundscapeVolume = mstartVolume - attenuation;
					if (Lullabye.debug)  Log.d(tag,"fading out: master volume "+(Lullabye.mSoundscapeVolume));
					if (attenuation >= 1.0 || (Lullabye.mSoundscapeVolume) <= 0) {
						Lullabye.mfadeInProgress = false;
						Lullabye.mPlaying = false;
						attenuation = 0.0f;
						SoundManager.stopAllSound(Lullabye.getmAnimationView());
						// reset mater volume to value recorded at beginning of fade
						Lullabye.mSoundscapeVolume = mstartVolume;
						this.cancel();

					}

				}
			}, 0, 1000);
		}
		 public Fade(Context context, String flag, int timeoutInSeconds) {

			 if (Lullabye.debug)  Log.d(tag, "creating alarm: flag " + flag + "starting in "
						+ (timeoutInSeconds));
				// get handle to alarm service
				AlarmManager alarmMgr = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				// create intent targeting my fade class
				Intent fadeintent = new Intent(context, Fade.class);
				// bundle extra data
				fadeintent.putExtra(REMINDER_BUNDLE, flag);
				// create pending intent from my intent, that creates or updates any
				// existing alarm.
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,
						fadeintent, PendingIntent.FLAG_UPDATE_CURRENT);

				// get a time instance
				Calendar time = Calendar.getInstance();
				// set time instance to now
				time.setTimeInMillis(System.currentTimeMillis());
				// add the fade timer to the time object
				time.add(Calendar.SECOND, timeoutInSeconds);
				// finally set alarm
				alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
						pendingIntent);

			}

	}

