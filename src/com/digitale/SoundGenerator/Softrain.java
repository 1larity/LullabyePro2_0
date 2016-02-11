package com.digitale.SoundGenerator;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

import android.util.Log;

public class Softrain {
	private String tag="SoftRain";

	/**
	 * @param currentVolume
	 * @param animationView
	 */
	public void softRain(final int multiplier) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			Random random = new Random();
			 public void run() {
				 int RandomNumber = random.nextInt(10);
				 if(Lullabye.debug) Log.d("roarfire","new fire "+ RandomNumber);
					SoundManager.playSound(RandomNumber + 250, 1, Lullabye.mSoundscapeVolume/multiplier);
				if (Lullabye.mPlaying == false) {
					
					this.cancel();
				}
			}
		}, 0, 3000);

	}
}
