package com.digitale.SoundGenerator;

import java.util.Random;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

public class Loons {
	public void loons(AnimationView animationView, float currentVolume) {
		Random random = new Random();
		// Log.d(tag,"entering");
		int RandomNumber = random.nextInt(Lullabye.getMscaleRandom());
		// Log.d(tag,"bird number is "+RandomNumber);

		// loons
		if ((RandomNumber >= 60) && (RandomNumber <= 69)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume);
		}
		// duck
		if ((RandomNumber >= 16) && (RandomNumber <= 18)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 10);
		}
		// background sounds
		RandomNumber = random.nextInt(Lullabye.getMscaleRandom() / 2);
		// Log.d(tag,"bird number is "+RandomNumber);
		// loons
		if ((RandomNumber >= 60) && (RandomNumber <= 69)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 8);
		}
		// duck
		if ((RandomNumber >= 16) && (RandomNumber <= 18)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 12);
		}
	}
}
