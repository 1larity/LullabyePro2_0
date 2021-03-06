package com.digitale.SoundGenerator;

import java.util.Random;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

import android.util.Log;
/**
 * @author Richard Beech
 */
public class OddsnSods {
	private String tag="Odds'n Sods";
	/**
	 * Generate random number and play dog/cow/duck as appropriate
	 * @param currentVolume
	 */
	public void oddsnsods(AnimationView animationView, float currentVolume) {
		Random random = new Random();
		int RandomNumber = random.nextInt(Lullabye.getMscaleRandom());

		if ((RandomNumber >= 12) && (RandomNumber <= 18)) {
			if (Lullabye.debug) Log.d(tag,"number is "+RandomNumber);
			SoundManager.playSound(RandomNumber, 1, currentVolume / 4);
		}
	}
}
