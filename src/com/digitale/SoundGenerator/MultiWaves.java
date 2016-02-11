package com.digitale.SoundGenerator;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

public class MultiWaves {
	private int mbreakersLoop;
	/**
	 * @param currentVolume
	 */
	public void multiwaves(final int multiplier) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			Random random = new Random();
			 public void run() {
				 int RandomNumber = random.nextInt(10);
				 if(Lullabye.debug) Log.d("roarfire","new fire "+ RandomNumber);
					SoundManager.playSound(RandomNumber + 260, 1, Lullabye.mSoundscapeVolume/(multiplier+RandomNumber));
				if (Lullabye.mPlaying == false) {
					
					this.cancel();
				}
			}
		}, 0, 837);
		
		/*int RandomNumber = random.nextInt(10);
		// Log.d(tag,"rand 10 "+ RandomNumber);
		int timer = (int) (animationView.mnow / 100);
		// Log.d(tag,"time  "+
		// do main waves
		if (timer >= animationView.getMwavesLoop()) {
			// Log.d(tag,"main wave");
			// get a new random between 0 and 9
			RandomNumber = random.nextInt(10);
			// scale number generated to -5..+5
			RandomNumber = 5 - RandomNumber;
			// do once per 6seconds+some random offset
			animationView.setMwavesLoop(timer + 60 + RandomNumber);

			// get a new random between 0 and 9
			RandomNumber = random.nextInt(10);
			// play the appropriate sound
			SoundManager.playSound(RandomNumber + 260, 1, currentVolume);

		}
		// do secondary waves twice as frequently, and quieter
		if (timer >= mbreakersLoop) {
			// Log.d(tag,"sub wave");
			mbreakersLoop = timer + 15 + RandomNumber;

			// get a new random between 0 and 9
			RandomNumber = random.nextInt(10);
			// scale number generated to -5..+5
			RandomNumber = 5 - RandomNumber;

			// get a new random between 0 and 9
			RandomNumber = random.nextInt(10);
			// play the appropriate sound
			SoundManager.playSound(RandomNumber + 260, 1,
					(float) (currentVolume / 1.75));
*/
		}
	}

