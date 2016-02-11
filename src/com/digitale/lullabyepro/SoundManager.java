package com.digitale.lullabyepro;







import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.util.SparseIntArray;

public class SoundManager {

	static private SoundManager _instance;
	private static SoundPool mSoundPool;
	private static SparseIntArray mSoundPoolMap;
	private static AudioManager mAudioManager;
	private static Context mContext;
	private static final String tag = "Sound Manager";
	// 10 entry buffer to deal with looped sounds
	private static int[] mloopedPlayids = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0 };
	// need some flags for special case sounds
	public static final int AUDIO_KEEP_ALIVE = 1;
	public static final int SEAMLESS_LOOPED = 2;
	private static Handler mainHandler;
	private static int[] mloopedFlags = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static int mcurrentid = 0;
	// need to know volume of looped sounds for fades, use this array with
	// mloopedPlaids.
	private static float[] mloopedvolumes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0 };

	private SoundManager() {
	}

	/**
	 * Requests the instance of the Sound Manager and creates it if it does not
	 * exist.
	 * 
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance() {
		if (_instance == null)
			_instance = new SoundManager();
		return _instance;
	}

	/**
	 * Initialises the storage for the sounds
	 * 
	 * @param theContext
	 *            The Application context
	 */
	public static void initSounds(Context theContext) {
		mContext = theContext;
		mSoundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new SparseIntArray(); //HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param Index
	 *            - The Sound Index for Retrieval
	 * @param SoundID
	 *            - The Android ID for the Sound asset.
	 */
	public static void addSound(int Index, int SoundID) {
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}

	/**
	 * Loads the various  sound assets 
	 */
	public static void loadSounds() {
		String soundfile;
		if (Lullabye.debug) Log.d(tag, "loading sound");
		int index;
			String[] temp = mContext.getResources().getStringArray(R.array.Sounds);
		int i = 0;
		if (Lullabye.debug) Log.d(tag, "array size " +temp.length);
		String[] each = null;
		while (i < temp.length) {
		    each = temp[i].split(",");
		    if (Lullabye.debug)  Log.d(tag, "data "+temp[i]+" i "+ i);
		    String loadText = LoadText.createLoadText(temp[i]);
		     SplashScreen.setMloadText(loadText);
		     SplashScreen.mHandler.post(SplashScreen.updateRunnable);
		     
		    index=Integer.parseInt( each[0]);
		    soundfile =each[1];
		    int rawID=mContext.getResources().getIdentifier(soundfile, "raw","com.digitale.lullabyepro");
		    mSoundPoolMap.put(index, mSoundPool.load(mContext, rawID, 1));
		    i++;
		    }//while
		mainHandler.sendEmptyMessage(Lullabye.FINISH_LOAD);
		
		}
	
	
	/**
	 * Plays a Sound
	 * 
	 * @param index
	 *            - The Index of the Sound to be played
	 * @param speed
	 *            - The Speed to play not, not currently used but included for
	 *            compatibility
	 */
	public static void playSound(int index, float speed, float volume) {
		// Log.v(tag," requested sound "+ index);
		int playid = mSoundPool.play(mSoundPoolMap.get(index), volume, volume,
				1, 0, speed);

		// Log.v(tag," short playid "+ playid);
	}

	/**
	 * Stop a Sound
	 * 
	 * @param index
	 *            - index of the sound to be stopped
	 */
	public static void stopSound(int index) {

	}

	public static void cleanup() {
		mSoundPool.release();
		mSoundPool = null;
		mSoundPoolMap.clear();
		mAudioManager.unloadSoundEffects();
		_instance = null;

	}
	public static void setMainHandler(Handler h) {
		mainHandler = h;
	}
	public static void playLoopedSound(int index, float speed, float volume,
			int flag) {

		// soundpool play method returns a stream ID we can use to control the
		// stream once its started playing
		// I dont care about 1-shot,short sounds, but I'm going to track looping
		// sounds
		int playid = mSoundPool.play(mSoundPoolMap.get(index), volume, volume,
				1, -1, speed);
		// Log.v(tag,"1playid "+ playid + " current "+ mcurrentid);

		// store returned stream data in array
		mloopedPlayids[mcurrentid] = playid;
		mloopedvolumes[mcurrentid] = volume;
		mloopedFlags[mcurrentid] = flag;
		// Log.v(tag, "playid " + playid + " current " +
		// mcurrentid+" flag "+flag);
		// increment array index so the next stream id goes in empty array entry
		mcurrentid++;
		// reset counter if it exceeds size of array
		if (mcurrentid > 9) {
			mcurrentid = 0;
		}

	}

	public static void stopAllSound(AnimationView animationView) {
		if (animationView != null){
		animationView.setMloopedSoundRunning(false);
		}
		if (Lullabye.debug)  Log.v(tag,"STOP current "+ mcurrentid);
		// only bother if some looped sounds have been played AND THE INDEX HAS
		// BEEN INCREMENTED
		if (mcurrentid != 0) {
			// reset global playid index, since we are about to clear all looped
			// sounds
			mcurrentid = 0;

			// loop through stored play ids

			for (int i = 10; i >= 0; i--) {
				// if theres a play id entry
				// Log.d(tag,"Array index " + i);
				try {
					if (mloopedPlayids[i] != 0) {
						// stop playing that stream
						// Log.v(tag, "Stopping current " + i + " playid "
						// + mloopedPlayids[i]);
						mSoundPool.stop(mloopedPlayids[i]);
						// reset that entry
						mloopedPlayids[i] = 0;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if (Lullabye.debug)  Log.d(tag,"Array Error: index " + i+"error "+e);
				}
				// reset that entry
				mloopedPlayids[i] = 0;
				mloopedvolumes[i] = 0.00f;
				mloopedFlags[i] = 0;
			}

		}

	}

	public static void changeAllVolume(float volume) {
		// mode is volume is RELATIVE, this method reduces the current volume
		// by the desired amount.
		if (Lullabye.debug) Log.v(tag, "change all volume " + volume);
		// loop through stored play ids
		for (int i = 10; i >= 0; i--) {

			// Log.v(tag, "volume index" + i + " flag " + mloopedFlags[i]
			// + " current stored  volume  " + mloopedvolumes[i]);
			// only touch looping non-keep-alive sounds.
			if (mloopedFlags[i] == SEAMLESS_LOOPED) {

				// change it's volume
				// Log.v(tag, "setting volume current  " + i + " playid "
				// + mloopedPlayids[i]);

				float tempvolume = mloopedvolumes[i] - volume;
				// sanity check: volume can never be negative
				if (tempvolume <= 0.01f) {
					volume = 0.01f;
				}
				if (Lullabye.debug)  Log.d(tag, "setting volume to " +tempvolume);
				mSoundPool.setVolume(mloopedPlayids[i], tempvolume, tempvolume);

			}
		}

	}
}