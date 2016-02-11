package com.digitale.SoundGenerator;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

public class Lakeside {
	public void lakeside(final int multiplier) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			Random random = new Random();
			 public void run() {
				 int RandomNumber = random.nextInt(10);
					SoundManager.playSound(RandomNumber + 40, 1, Lullabye.mSoundscapeVolume/multiplier);
				if (Lullabye.mPlaying == false) {
					
					this.cancel();
				}
			}
		}, 0, 4000);
		
		
		
		
		/*if ((animationView.getMtimeTenths() >= 20)
				&& (animationView.getMtimeTenths() <= 25)
				&& animationView.isMspecialLoop() == false
				&& (animationView.getMtimeSeconds() % 4) == 1) {
			// Log.d(tag,"one second");
			// get a new random between 0 and 9
			int RandomNumber = random.nextInt(10);
			SoundManager.playSound(RandomNumber + 40, 1, currentVolume);
			// do once per first tenth of a second
			animationView.setMspecialLoop(true);
		} else {
			animationView.setMspecialLoop(false);
		}*/
	}
}
