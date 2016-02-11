package com.digitale.SoundGenerator;

import java.util.Random;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

public class LoonsNoDucks {
	public void loonsNoDucks(AnimationView animationView,float currentVolume) {
		Random random = new Random();
		// Log.d(tag,"entering loons no ducks");
		int RandomNumber = random.nextInt(Lullabye.getMscaleRandom());
		// Log.d(tag,"bird number is "+RandomNumber);

		// loons
		if ((RandomNumber >= 60) && (RandomNumber <= 69)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume);
		}
		// background sounds
		RandomNumber = random.nextInt(Lullabye.getMscaleRandom() / 2);
		// Log.d(tag,"bird number is "+RandomNumber);
		// loons
		if ((RandomNumber >= 60) && (RandomNumber <= 69)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 8);
		}

	}
}
