package com.digitale.SoundGenerator;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

/**
 * @author Richard Beech
 */
public class Nature {

	/**
	 * Generate nature soundscapes
	 * 
	 * @param animationView
	 */
	public void nature(AnimationView animationView) {

			//done
		if (Lullabye.getSelectedSoundscape() == 0
				&& animationView.isMloopedSoundRunning() == false) {
			Waves myWaves = new Waves();
			myWaves.waves(1);

			BubblyRiver myBubblyRiver = new BubblyRiver();
			myBubblyRiver.bubblyRiver(12);
			//done 
		} else	if (Lullabye.getSelectedSoundscape() == 1
				&& animationView.isMloopedSoundRunning() == false) {
			Lakeside myLakeside = new Lakeside();
			myLakeside.lakeside(1);
			
		} else if (Lullabye.getSelectedSoundscape() == 2&& animationView.isMloopedSoundRunning() == false
				) {
			MultiWaves myMultiWaves = new MultiWaves();
			myMultiWaves.multiwaves(1);
			//done
		} else if (Lullabye.getSelectedSoundscape() == 3
				&& animationView.isMloopedSoundRunning() == false) {
			TreeWind myTreeWind = new TreeWind();
			myTreeWind.treewind(1);
			
			//done
		} else if (Lullabye.getSelectedSoundscape() == 4
				&& animationView.isMloopedSoundRunning() == false) {
			BubblyRiver myBubblyRiver = new BubblyRiver();
			myBubblyRiver.bubblyRiver(6);
			 //done
		} else if (Lullabye.getSelectedSoundscape() == 5
				&& animationView.isMloopedSoundRunning() == false) {
			Softrain mySoftrain = new Softrain();
			mySoftrain.softRain(1);
			//done
		} else if (Lullabye.getSelectedSoundscape() == 6
				&& animationView.isMloopedSoundRunning() == false) {
			HeavyRain myHeavyRain = new HeavyRain();
			myHeavyRain.heavyRain(1);
			
			//done
		} else if (Lullabye.getSelectedSoundscape() == 7
				&& animationView.isMloopedSoundRunning() == false) {
			RoarFire myRoarFire = new RoarFire();
			myRoarFire.roarfire(1);
			//done
		} else if (Lullabye.getSelectedSoundscape() == 8
				&& animationView.isMloopedSoundRunning() == false) {
			SoftFire mySoftFire = new SoftFire();
			mySoftFire.softfire(1);
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
