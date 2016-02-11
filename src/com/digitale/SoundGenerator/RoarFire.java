package com.digitale.SoundGenerator;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.util.Log;

import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

public class RoarFire {
	public void roarfire(final int multiplier) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			Random random = new Random();
			 public void run() {
				 int RandomNumber = random.nextInt(8);
				 if(Lullabye.debug) Log.d("roarfire","new fire "+ RandomNumber);
					SoundManager.playSound(RandomNumber + 80, 1, Lullabye.mSoundscapeVolume/multiplier);
				if (Lullabye.mPlaying == false) {
					
					this.cancel();
				}
			}
		}, 0, 3000);
	
	}
}
