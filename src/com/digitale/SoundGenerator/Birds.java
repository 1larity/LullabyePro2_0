package com.digitale.SoundGenerator;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

/**
 * @author Richard Beech
 */
public class Birds {
	/**
	 * Generate bird soundscapes
	 * @param animationView
	 */
	 public void birds(AnimationView animationView) {

		float currentVolume = Lullabye.getMasterVolume();
		// create soundscapes based on catogory/subcat selected
		if (Lullabye.getSelectedSoundscape() >= 1
				&& Lullabye.getSelectedSoundscape() <= 4) {
			loons(animationView, currentVolume);
		}else

		if (Lullabye.getSelectedSoundscape() >= 5
				&& Lullabye.getSelectedSoundscape() <= 8) {
			loonsNoSDucks(animationView, currentVolume);
		}else 

		if (Lullabye.getSelectedSoundscape() == 0
				|| (Lullabye.getSelectedSoundscape() >= 0 && Lullabye
						.getSelectedSoundscape() <= 11)) {
			birdSong(animationView, currentVolume);
		}else

		// no crows
		if (Lullabye.getSelectedSoundscape() >= 12
				&& Lullabye.getSelectedSoundscape() <= 15) {

			birdSongNoCrows(animationView, currentVolume);
		}
	}

	/**
	 * Generate birds soundscape with no crows
	 *  
	 * @param animationView
	 * @param currentVolume
	 */
	private static void birdSongNoCrows(AnimationView animationView,
			float currentVolume) {
		BirdSongNoCrows myBirdSongNoCrows = new BirdSongNoCrows();
		myBirdSongNoCrows.birdsongNoCrows(animationView, currentVolume);

		if (Lullabye.getSelectedSoundscape() == 13&& animationView.isMloopedSoundRunning() == false) {
			TreeWind myTreeWind = new TreeWind();
			myTreeWind.treewind(8);
		}else
		if (Lullabye.getSelectedSoundscape() == 14&& animationView.isMloopedSoundRunning() == false) {
			BubblyRiver myBubblyRiver = new BubblyRiver();
			myBubblyRiver.bubblyRiver(6);
		}else
		if (Lullabye.getSelectedSoundscape() == 15 && animationView.isMloopedSoundRunning() == false) {
			Softrain mySoftrain = new Softrain();
			mySoftrain.softRain(2);

		}
		
		if (animationView.isMloopedSoundRunning() == false) {
			// play keep alive sample to stop audio shutting down with a
			// 'click'
			SoundManager.playLoopedSound(21, 1, .01f,
					SoundManager.AUDIO_KEEP_ALIVE);
			animationView.setMloopedSoundRunning(true);
		}
	}

	/**
	 * Generate birds soundscape 
	 *  
	 * @param animationView
	 * @param currentVolume
	 */
	private static void birdSong(AnimationView animationView,
			float currentVolume) {
		BirdSong myBirdSong = new BirdSong();
		myBirdSong.birdsong(animationView, currentVolume);

		if (Lullabye.getSelectedSoundscape() == 9 && animationView.isMloopedSoundRunning() == false) {
			TreeWind myTreeWind = new TreeWind();
			myTreeWind.treewind(8);
		}else
		if (Lullabye.getSelectedSoundscape() == 10 && animationView.isMloopedSoundRunning() == false) {
			BubblyRiver myBubblyRiver = new BubblyRiver();
			myBubblyRiver.bubblyRiver(6);
		}else
		if (Lullabye.getSelectedSoundscape() == 11 && animationView.isMloopedSoundRunning() == false) {
			Softrain mySoftrain = new Softrain();
			mySoftrain.softRain( 2);
		}
		
		if (animationView.isMloopedSoundRunning() == false) {
			// play keep alive sample to stop audio shutting down with a
			// 'click'
			SoundManager.playLoopedSound(21, 1, .01f,
					SoundManager.AUDIO_KEEP_ALIVE);
			animationView.setMloopedSoundRunning(true);
		}
	}

	/**
	 * Generate loons soundscape with no ducks
	 * 
	 * @param animationView
	 * @param currentVolume
	 */
	private static void loonsNoSDucks(AnimationView animationView,
			float currentVolume) {
		LoonsNoDucks myLoons = new LoonsNoDucks();
		myLoons.loonsNoDucks(animationView, currentVolume);

		if (Lullabye.getSelectedSoundscape() == 6 && animationView.isMloopedSoundRunning() == false) {
			Lakeside myLakeside = new Lakeside();
			myLakeside.lakeside(4);

		}else
		if (Lullabye.getSelectedSoundscape() == 7 && animationView.isMloopedSoundRunning() == false) {
			Softrain mySoftrain = new Softrain();
			mySoftrain.softRain(4);

		}else
		if (Lullabye.getSelectedSoundscape() == 8 && animationView.isMloopedSoundRunning() == false) {
			SoftRainlakside mySoftRainlakside = new SoftRainlakside();
			mySoftRainlakside.softRainlakside(currentVolume / 4, animationView);
		}
		
		if (animationView.isMloopedSoundRunning() == false) {
			// play keep alive sample to stop audio shutting down with a
			// 'click'
			SoundManager.playLoopedSound(21, 1, .01f,
					SoundManager.AUDIO_KEEP_ALIVE);
			animationView.setMloopedSoundRunning(true);
		}
	}

	/**
	 * Generate loons soundscape with ducks
	 * 
	 * @param animationView
	 * @param currentVolume
	 */
	private static void loons(AnimationView animationView, float currentVolume) {
		Loons myLoons = new Loons();
		myLoons.loons(animationView, currentVolume);

		if (Lullabye.getSelectedSoundscape() == 2 && animationView.isMloopedSoundRunning() == false) {
			Lakeside myLakeside = new Lakeside();
			myLakeside.lakeside(4);
		}else
		if (Lullabye.getSelectedSoundscape() == 3 && animationView.isMloopedSoundRunning() == false) {
			Softrain mySoftrain = new Softrain();
			mySoftrain.softRain(4);

		}else
		if (Lullabye.getSelectedSoundscape() == 4 && animationView.isMloopedSoundRunning() == false) {
			SoftRainlakside mySoftRainlakside = new SoftRainlakside();
			mySoftRainlakside.softRainlakside(currentVolume / 4, animationView);
		}
		
		if (animationView.isMloopedSoundRunning() == false) {
			// play keep alive sample to stop audio shutting down with a
			// 'click'
			SoundManager.playLoopedSound(21, 1, .01f,
					SoundManager.AUDIO_KEEP_ALIVE);
			animationView.setMloopedSoundRunning(true);
		}
	}
}
