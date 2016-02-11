package com.digitale.SoundGenerator;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

public class Noise{
	public static void noise(AnimationView animationView) {
		animationView.setMloopedSoundRunning(true);
		if (Lullabye.getSelectedSoundscape() == 0) {
			SoundManager.playLoopedSound(21, 1, Lullabye.getMasterVolume(),
					SoundManager.SEAMLESS_LOOPED);
		} else if (Lullabye.getSelectedSoundscape() == 1) {
			SoundManager.playLoopedSound(20, 1, Lullabye.getMasterVolume(),
					SoundManager.SEAMLESS_LOOPED);
		} else if (Lullabye.getSelectedSoundscape() == 2) {
			SoundManager.playLoopedSound(22, 1, Lullabye.getMasterVolume(),
					SoundManager.SEAMLESS_LOOPED);
		} else if (Lullabye.getSelectedSoundscape() == 3) {
			SoundManager.playLoopedSound(23, 1, Lullabye.getMasterVolume(),
					SoundManager.SEAMLESS_LOOPED);
		} else if (Lullabye.getSelectedSoundscape() == 4) {
			SoundManager.playLoopedSound(24, 1, Lullabye.getMasterVolume(),
					SoundManager.SEAMLESS_LOOPED);
		}
	
	}

}