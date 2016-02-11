package com.digitale.SoundGenerator;

import java.util.Random;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

import android.util.Log;
/**
 * @author Richard Beech
 */
public class Sheep {
	private String tag="Sheep";
	/**
	 * Generate random number and play sheep as appropriate
	 * @param currentVolume
	 */
	public void sheep(AnimationView animationView, float currentVolume) {
		Random random = new Random();
		int RandomNumber = random.nextInt(Lullabye.getMscaleRandom());
		if (RandomNumber < 10) {
			if (Lullabye.debug) Log.d(tag,"number is "+RandomNumber);
			SoundManager.playSound(RandomNumber, 1, currentVolume);
		}
		RandomNumber = random.nextInt(Lullabye.getMscaleRandom());
		if (RandomNumber < 10) {
			if (Lullabye.debug) Log.d(tag,"number is "+RandomNumber);
			SoundManager.playSound(RandomNumber, 1, currentVolume / 4);
		}
		RandomNumber = random.nextInt(Lullabye.getMscaleRandom());
		if (RandomNumber < 10) {
			if (Lullabye.debug) Log.d(tag,"number is "+RandomNumber);
			SoundManager.playSound(RandomNumber, 1, currentVolume / 8);
		}
	}
}
