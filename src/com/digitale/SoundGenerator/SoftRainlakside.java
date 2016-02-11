package com.digitale.SoundGenerator;

import java.util.Random;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.SoundManager;

public class SoftRainlakside {
	public void softRainlakside(float currentVolume, AnimationView animationView) {
		Random random = new Random();

		if ((animationView.getMtimeTenths() >= 15)
				&& (animationView.getMtimeTenths() <= 25) && animationView.isMspecialLoop() == false
				&& (animationView.getMtimeSeconds() % 3) == 1) {
			// Log.d(tag,"one second");
			// get a new random between 0 and 9
			int RandomNumber = random.nextInt(10);
			SoundManager.playSound(RandomNumber + 40, 1, currentVolume / 2);
			RandomNumber = random.nextInt(10);
			SoundManager.playSound(RandomNumber + 250, 1, currentVolume);
			// do once per first tenth of a second
			animationView.setMspecialLoop(true);
		} else {
			animationView.setMspecialLoop(false);
		}
	}
}
