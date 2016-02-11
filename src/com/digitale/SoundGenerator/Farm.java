package com.digitale.SoundGenerator;
import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

/**
 * @author Richard Beech
 */
import android.util.Log;

public class Farm {
	private static String tag="Farm";
	/**
	 * Generate farm soundscapes
	 * @param animationView
	 */
	public void farm(AnimationView animationView) {
		if (Lullabye.getSelectedCatagory() == 2) {
			if (Lullabye.deepDebug) Log.d(tag,"entering farm");
			
			}
			float currentVolume = Lullabye.getMasterVolume();
			if (Lullabye.getSelectedSoundscape() == 0) {
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume);
			}else
			if (Lullabye.getSelectedSoundscape() == 1) {
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume);
				
				if(animationView.isMloopedSoundRunning() == false){
				TreeWind myTreeWind = new TreeWind();
				myTreeWind.treewind(8);
				
				}
				}else
			if (Lullabye.getSelectedSoundscape() == 2) {
				if(animationView.isMloopedSoundRunning() == false){
				BubblyRiver myBubblyRiver = new BubblyRiver();
				myBubblyRiver.bubblyRiver(4);
				}
				
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume);
			}else
			if (Lullabye.getSelectedSoundscape() == 3) {
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume);
				if(animationView.isMloopedSoundRunning() == false){
				Softrain mySoftrain = new Softrain();
				mySoftrain.softRain(2);}
			}else
			if (Lullabye.getSelectedSoundscape() == 4) {
				Pigs myPigs = new Pigs();
				myPigs.pigs(animationView, currentVolume);
			}else
			if (Lullabye.getSelectedSoundscape() == 5) {
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume / 2);
				
				Pigs myPigs = new Pigs();
				myPigs.pigs(animationView, currentVolume);
				
				OddsnSods myOddsnSods = new OddsnSods();
				myOddsnSods.oddsnsods(animationView, currentVolume);
			}else
			if (Lullabye.getSelectedSoundscape() == 6) {
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume / 2);
				
				Pigs myPigs = new Pigs();
				myPigs.pigs(animationView, currentVolume);
				
				OddsnSods myOddsnSods = new OddsnSods();
				myOddsnSods.oddsnsods(animationView, currentVolume);
				if(animationView.isMloopedSoundRunning() == false){
				TreeWind myTreeWind = new TreeWind();
				myTreeWind.treewind(8);
				
				}
			}else
			if (Lullabye.getSelectedSoundscape() == 7) {
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume / 2);
				
				Pigs myPigs = new Pigs();
				myPigs.pigs(animationView, currentVolume);
				
				OddsnSods myOddsnSods = new OddsnSods();
				myOddsnSods.oddsnsods(animationView, currentVolume);
				if(animationView.isMloopedSoundRunning() == false){
				Softrain mySoftrain = new Softrain();
				mySoftrain.softRain(2);}
			}else if (Lullabye.getSelectedSoundscape() == 8) {
				if(animationView.isMloopedSoundRunning() == false){
				BubblyRiver myBubblyRiver = new BubblyRiver();
				myBubblyRiver.bubblyRiver(8);
				}
				
				Sheep mySheep = new Sheep();
				mySheep.sheep(animationView, currentVolume / 2);
				
				Pigs myPigs = new Pigs();
				myPigs.pigs(animationView, currentVolume);
				
				OddsnSods myOddsnSods = new OddsnSods();
				myOddsnSods.oddsnsods(animationView, currentVolume);
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
