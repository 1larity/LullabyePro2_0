package com.digitale.SoundGenerator;

import java.util.Random;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

public class BirdSongNoCrows {
	public void birdsongNoCrows(AnimationView animationView, float currentVolume) {
		Random random = new Random();
		// Log.d(tag,"entering birdsong no crows");

		int RandomNumber = random.nextInt(Lullabye.getMscaleRandom());
		// Log.d(tag,"bird number is "+RandomNumber);

		// nightingale
		if ((RandomNumber >= 30) && (RandomNumber <= 39)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 2);
		}

		// thrush
		if ((RandomNumber >= 50) && (RandomNumber <= 59)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume);
		}
		// cookoo
		if ((RandomNumber >= 10) && (RandomNumber <= 11)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume);

		}

		// background sounds
		RandomNumber = random.nextInt(Lullabye.getMscaleRandom() / 2);
		// Log.d(tag,"bird number is "+RandomNumber);

		// nightingale
		if ((RandomNumber >= 30) && (RandomNumber <= 39)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 8);
		}

		// thrush
		if ((RandomNumber >= 50) && (RandomNumber <= 59)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 8);
		}

		if ((RandomNumber >= 10) && (RandomNumber <= 11)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 6);
		}
	}
}
