package com.digitale.lullabyepro;

import java.util.Calendar;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Lullabye extends Activity {
	//debug flags
	private static final String tag = "Lullabye";
	public static boolean debug = false;
	//debug for spammy outputs
	public static boolean deepDebug = false;
	//UI members
	private static Button mPlaySoundStop;
	private static Button mPlaySoundStart;
	private Spinner mCatagorySpinner;
	private Spinner mSoundscapeSpinner;
	private static AnimationView mAnimationView;
	public static float mSoundscapeVolume = 0.5f;
	private static int selectedCatagory = 0;
	private static int selectedSoundscape = 0;
	private SeekBar mMediaVolumeSeekBar;
	private TextView mMediaVolumeProgressText;
	private SeekBar mSoundscapeVolumeSeekBar;
	private TextView mSoundscapeVolumeProgressText;
	private ArrayAdapter<CharSequence> catagoryAdapter;
	private ArrayAdapter<CharSequence> soundscapeAdapter1;
	private ArrayAdapter<CharSequence> soundscapeAdapter2;
	private ArrayAdapter<CharSequence> soundscapeAdapter3;
	private ArrayAdapter<CharSequence> soundscapeAdapter4;
	public static boolean mPlaying = false;
	public final static boolean mAdvetizing=true;
	public static int mscaleRandom = 5000;
	private static int mtransportBottomEdge;
	private static int mtransportLeftMargin;
	private static int mtransportRightMargin;
	private static Handler mHandler;
	static boolean mplayInBackground = true;
	public static String mhelpText;
	// main message handler constants
	protected static final int FINISH_LOAD = 1;
	protected static final int START_LOAD = 2;
	protected static final int ABORT_LOAD = 3;
	public static final int START_HELP = 4;
	protected static final int CLOSE_HELP = 5;
	protected static final int CLOSE_OPTIONS = 6;
	public static final int START_OPTIONS = 7;
	// menu constants
	private static final int PREFERENCES_GROUP_ID = 0;
	private static final int FEEDBACK_ID = 0;
	private static final int HELP_ID = 1;
	private static final int SETTINGS_ID = 2;
	private static final int ABOUT_ID = 3;
	static final int GONE = 8;
	private static  Locale Locale;
	private SharedPreferences mPrefs;
	private AudioManager maudioManager;
	private boolean mIsRunning = false;
	private static Button mfadeButton;
	private static Button malarmButton;

	// fields for fade operations
	// note when the user set the fade
	private static long fadeTimerSetAt;
	// attenuation, how much to reduce volume by
	static float attenuation = 0.0f;
	// master volume at start of fade operation
	static float mstartVolume;
	// is a fade happening flag
	static boolean mfadeInProgress = false;
	// how many seconds to fade out
	private static int mfadeOut = 3600;

	// fields for alarm operations
	private static int malarm = 0;
	private static boolean malarmRepeat = true;

	private static RelativeLayout rlayout;

	private static RefreshHandler mRedrawHandler = new RefreshHandler();
	private Button mfadeCancelButton;

	private static String mfadeTextString;
	private static String mstartFadeText;


	static class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			updateUI();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	private static void updateUI() {

		mRedrawHandler.sleep(1000);
		updateAlarmText();
		updateFadeText();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Locale = getResources().getConfiguration().locale;
		 if(savedInstanceState == null){
			 mHandler = new Handler(new IncomingHandlerCallback()); 
				startSplash();
				
	        }
		// handler for splash screen/long load
		
	}
	 
	// start splash screen activity
	private void startSplash() {
		Intent intent = new Intent(this, SplashScreen.class);
		SplashScreen.setMainHandler(mHandler);
		startActivity(intent);
	}

	private void initActivity() {
		Configuration config = getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
	    {
	        setContentView(R.layout.main_screen_port);
	    }
	     if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
	    {
	    
	        setContentView(R.layout.main_screen_land );
	    } 
		mAnimationView = (AnimationView) this.findViewById(R.id.aview);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();

				long start_time = android.os.SystemClock.uptimeMillis();
				initApp();
				long duration = android.os.SystemClock.uptimeMillis()
						- start_time;
				// force splash screen to stay up at least 3 seconds
				
				if (duration <= 3000) {
					try {
						synchronized(this){
						
						sleep(3000 - duration);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Looper.loop();

				}
		
				mHandler.sendEmptyMessage(FINISH_LOAD);
			}
		}.start();
	
		
		mPrefs = getPreferences(MODE_PRIVATE);
		// init from prefs
		prefsLoad(mPrefs);
		initUI();
		initAdView();
		updateUI();

		mfadeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fade myFade=new Fade(getApplicationContext(), "fade_out", mfadeOut);
				myFade.fadeout();
			//	Fade.Fade(getApplicationContext(), "fade_out", mfadeOut);
				Calendar calendar = Calendar.getInstance();

				Long currenttime = (calendar.getTimeInMillis() / 1000);
				fadeTimerSetAt = currenttime;
				updateUI();
			}
		});
		mfadeCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// need to delete all fade alarms and reset to unfaded state
				fadeTimerSetAt = 0l;
				mfadeInProgress = false;
				attenuation = 0.0f;
				// TimerTask.TimerTaskcancel();
				// reset master volume to value recorded at beginning of fade
				mSoundscapeVolume = mstartVolume;
				updateUI();
			}
		});
		mPlaySoundStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startplaying();

			}
		});
		// onchange catagory spinner
		mCatagorySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						selectedCatagory = pos; 
						SoundManager.stopAllSound(mAnimationView);
						mPlaying = false;
						AnimationView.mloopedSoundRunning=false;
						setsoundscapefromcategory();
					}

					// auto generated
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		// onchange soundscape spinner
		mSoundscapeSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						selectedSoundscape = pos;
						mPlaying = false;
						AnimationView.mloopedSoundRunning=false;
						SoundManager.stopAllSound(mAnimationView);

					}

					// auto generated
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		mPlaySoundStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlaying = false;
				SoundManager.stopAllSound(Lullabye.mAnimationView);
				AnimationView.mloopedSoundRunning=false;
			}
		});
		
		// mSeekBar.getBackground().setAlpha(128);
		mSoundscapeVolumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int volume,
					boolean fromTouch) {
				if (mfadeInProgress == false) {
					mSoundscapeVolume = (volume / 100f);
					mSoundscapeVolumeProgressText.setText(" " + (volume) + " ");
					SoundManager.changeAllVolume(mSoundscapeVolume);
				} else {
					Toast msg = Toast
							.makeText(
									Lullabye.this,
									"Volume cannot be changed while fade is in progress.",
									Toast.LENGTH_SHORT);
					msg.show();
					seekBar.setProgress((int) (mSoundscapeVolume * 100));

				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});
		
		// mSeekBar.getBackground().setAlpha(128);
		mMediaVolumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int volume,
					boolean fromTouch) {
				
				AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						volume, AudioManager.FLAG_VIBRATE);
				mMediaVolumeProgressText.setText(" " + (volume) + " ");

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});

	}
	 /**
	 * 
	 */
	private void initApp() {
		if (Lullabye.debug) Log.v( tag, "setup start");
		// Create, Initialise and then load the Sound manager
	
		try {
			SoundManager.getInstance();
			SoundManager.initSounds(this);
			SoundManager.loadSounds();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(tag,"sound manager exception  "+e );
		}
	
		if (Lullabye.debug) Log.v( tag, "sound setup done");
	}

	private void initAdView() {
//		 if(mAdvetizing){
//		 
//			AdRequest adRequest = new AdRequest.Builder()
//		    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
//		    .addTestDevice("44639833DE44D2A5776E41EC613B72D8") // My test phone
//		    .build();
//			 adView.loadAd(adRequest);
//		 }else{
//			 
//			 adView.setVisibility(GONE);
//		 }
	}
private void initUI(){
	mfadeTextString = getString(R.string.fadeOutText);
	mstartFadeText = getString(R.string.startFadeText);
	maudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	rlayout = (RelativeLayout) findViewById(R.id.mainlayout);
	
	mPlaying = true;
	mPlaySoundStart = (Button) findViewById(R.id.StartSound);
	mPlaySoundStart.getBackground().setAlpha(128);

	mfadeButton = (Button) findViewById(R.id.buttonFadeOut);
	mfadeButton.getBackground().setAlpha(128);

	mfadeCancelButton = (Button) findViewById(R.id.buttonFadeCancel);
	mfadeCancelButton.getBackground().setAlpha(128);

	malarmButton = (Button) findViewById(R.id.buttonAlarm);
	malarmButton.getBackground().setAlpha(128);

	mPlaySoundStop = (Button) findViewById(R.id.StopSound);
	mPlaySoundStop.getBackground().setAlpha(128);
	
	mSoundscapeVolumeSeekBar = (SeekBar) findViewById(R.id.seekBarVolume);
	mSoundscapeVolumeProgressText = (TextView) findViewById(R.id.textViewVolume);
	
	mMediaVolumeSeekBar = (SeekBar) findViewById(R.id.seekBarMediaVolume);
	mMediaVolumeProgressText = (TextView) findViewById(R.id.textViewMediaVolume);
	
	mCatagorySpinner=(Spinner) findViewById(R.id.spinnerCatagory);
	mSoundscapeSpinner=(Spinner) findViewById(R.id.spinnerSoundscape);
	mstartVolume = mSoundscapeVolume;
	initsoundscapeslider();
	initcategoryspinner();
	initsoundscapespinner();
	initmediavolumeslider();
}
	@Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);

	    if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    {
	        setContentView(R.layout.main_screen_port);
	    }
	     if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
	    {
	    
	        setContentView(R.layout.main_screen_land );
	    } 
	     prefsLoad(mPrefs);
			initUI();
			initAdView();
			updateUI();
	    }
	/**
	 * 
	 */
	public void showchangeLog() {
		if (setmIsRunning(true)) {
			try {
				LayoutInflater li = LayoutInflater.from(this);
				View view = li.inflate(R.layout.changelog, null);
				new AlertDialog.Builder(Lullabye.this)
						.setTitle("What's New")
						.setIcon(android.R.drawable.ic_menu_info_details)
						.setView(view)
						.setNegativeButton("Close",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int whichButton) {
										//
									}
								}).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private void initsoundscapespinner() {
		// set up and populate soundscape spinner

		mSoundscapeSpinner.getBackground().setAlpha(128);
		// create adaptors for each of our categories
		soundscapeAdapter1 = ArrayAdapter.createFromResource(this,
				R.array.birdsong, android.R.layout.simple_spinner_item);
		soundscapeAdapter2 = ArrayAdapter.createFromResource(this,
				R.array.nature, android.R.layout.simple_spinner_item);
		soundscapeAdapter3 = ArrayAdapter.createFromResource(this,
				R.array.farm, android.R.layout.simple_spinner_item);
		soundscapeAdapter4 = ArrayAdapter.createFromResource(this,
				R.array.noise, android.R.layout.simple_spinner_item);

		soundscapeAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		soundscapeAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		soundscapeAdapter3
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		soundscapeAdapter4
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSoundscapeSpinner.setAdapter(soundscapeAdapter1);

		int spinnerPosition = 0;
		// convert prefs string to adaptor index based on category selection
		if (Lullabye.debug)  Log.v( tag, "setting soundscape");
		if (selectedCatagory == 1) {
			// spinnerPosition = soundscapeAdapter1(listPreference);
			if (Lullabye.debug)  Log.v( tag, "catagory1");

		}
		if (selectedCatagory == 2) {
			// spinnerPosition = soundscapeAdapter3.getPosition(listPreference);
			if (Lullabye.debug)  Log.v( tag, "catagory2");
		}
		if (selectedCatagory == 3) {
			// spinnerPosition = soundscapeAdapter4.getPosition(listPreference);
			if (Lullabye.debug)  Log.v( tag, "catagory3");
		}
		if (selectedCatagory == 4) {
			// spinnerPosition = soundscapeAdapter4.getPosition(listPreference);
			if (Lullabye.debug)  Log.v( tag, "catagory4");
		}
		if (Lullabye.debug)  Log.v( tag, "spinner pos"+spinnerPosition);
		
		mSoundscapeSpinner.setSelection(selectedSoundscape);
	}

	/**
	 * 
	 */
	private void initmediavolumeslider() {
		// set media slider max setting based on device max volume
		// get handle to system media volume
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		// set max value of media slider to match system max volume
		mMediaVolumeSeekBar.setMax(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

		// set media volume slider to current system volume
		mMediaVolumeSeekBar.setProgress(mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		// set media volume text display
		mMediaVolumeProgressText.setText(" "
				+ mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
				+ " ");
	}
	/**
	
	/**
	 * @param mPrefs
	 */
	private void initcategoryspinner() {
		// set up and populate category spinner
		mCatagorySpinner.getBackground().setAlpha(128);
		// create adaptor from category string array
		catagoryAdapter = ArrayAdapter.createFromResource(this,
				R.array.catagories, android.R.layout.simple_spinner_item);
		catagoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// bind array adaptor to spinner
		mCatagorySpinner.setAdapter(catagoryAdapter);
		mCatagorySpinner.setSelection(selectedCatagory);
	}

	/**
	 * @param mPrefs
	 */
	private void initsoundscapeslider() {
		
		mSoundscapeVolumeProgressText.setText(" " + (int) (mSoundscapeVolume * 100));
		mSoundscapeVolumeSeekBar.setProgress((int) (mSoundscapeVolume * 100));
	}

	public void handleMessage(Message msg) {
		if (Lullabye.debug) Log.d(tag, "handle message " + msg.what);
		switch (msg.what) {
		// if loading finished ok
		case CLOSE_OPTIONS:
			// close the help screen
			Options.sendMessage(Options.CLOSE_OPTIONS);
			// leave switch
			break;
		case START_LOAD:
			initActivity();
			break;
	
		// if user pressed back key
		default:
			if (mplayInBackground == false) {
				mAnimationView.setRunning(false);
				mAnimationView.setMloopedSoundRunning(false);
				SoundManager.stopAllSound(mAnimationView);
			}
		}
	}

	@Override
	public void onDestroy() {
		prefsSave();
		mPlaying = false;
		SoundManager.stopAllSound(mAnimationView);
		mAnimationView.setRunning(false);
	
		mAnimationView.mdoDraw = false;
		super.onDestroy();

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (Lullabye.debug)  Log.d(tag, "on save instance " + mHandler);
		handleMessage(mHandler.obtainMessage());
		if (mAnimationView != null)
			mAnimationView.mdoDraw = false;
		prefsSave();
		savedInstanceState.putBoolean("mplayInBackground", mplayInBackground);
		savedInstanceState.putBoolean("mplaying", mPlaying);
		savedInstanceState.putFloat("masterVolume", mSoundscapeVolume);
		// do the normal savedinstance code
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();
	

		if (Lullabye.debug) Log.d(tag,"Lullabye On Pause Called");
		if (mplayInBackground == false) {
			mAnimationView.setRunning(false);
			mAnimationView.setMloopedSoundRunning(false);
			SoundManager.stopAllSound(mAnimationView);
		}
		if (mAnimationView !=null)
			mAnimationView.mdoDraw = false;
		setmIsRunning(false);
		prefsSave();
	}

	@Override
	protected void onResume() {
		// AnimationThread.onResume();
		if (Lullabye.debug)  Log.d(tag,"lullabye On Resume Called");
		Object mPauseLock = new Object();
		synchronized (mPauseLock) {
			if (mAnimationView != null)	mAnimationView.setRunning(true);
			mPauseLock.notifyAll();
			setmIsRunning(true);
			
		}
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Lullabye.debug)  Log.d(tag,"menu activated");
		switch (item.getItemId()) {
		case SETTINGS_ID:
			startOptions();
			break;
		case HELP_ID:
			startHelp();
			break;
		case FEEDBACK_ID:
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			String aEmailList[] = { "rp.beech@gmail.com" };
			emailIntent.setType("plain/text");
			emailIntent
					.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Lullabye Feedback");
			startActivity(emailIntent);
			break;
		case ABOUT_ID:
			startAbout();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	
		menu.add(PREFERENCES_GROUP_ID, SETTINGS_ID, 0, R.string.menu_settings)
				.setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(PREFERENCES_GROUP_ID, HELP_ID, 0, R.string.menu_help).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(PREFERENCES_GROUP_ID, FEEDBACK_ID, 0, R.string.menu_feedback)
				.setIcon(android.R.drawable.ic_menu_send);
		menu.add(PREFERENCES_GROUP_ID, ABOUT_ID, 0, R.string.menu_about)
				.setIcon(android.R.drawable.ic_menu_info_details);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			maudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			// Raise the Volume Bar on the Screen
			mMediaVolumeSeekBar.setProgress(maudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC)
					+ AudioManager.ADJUST_RAISE);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			// Adjust the Volume
			maudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			// Lower the Volume Bar on the Screen
			mMediaVolumeSeekBar.setProgress(maudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC)
					+ AudioManager.ADJUST_LOWER);
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	
	}

	/**
	 * Save Preferences
	 */
	public void prefsSave() {
		SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor ed = mPrefs.edit();
		ed.putInt("scalerandom", (getMscaleRandom()));
		// save play in background flag
		ed.putBoolean("playinbackground", mplayInBackground);
		// save current volume
		ed.putFloat("soundscapevolume", mSoundscapeVolume);
		// save currently set soundscpe
		ed.putInt("soundscapePref", selectedSoundscape);
		// save currently selected catagory
		ed.putInt("categoryPref", selectedCatagory);
		// save fade out timer duration
		ed.putInt("fadeoutPref", mfadeOut);
		// save alarm repeat flag
		ed.putBoolean("alarmrepeatPref", malarmRepeat);
		// save alarm interval
		ed.putInt("alarmPref", malarm);
		ed.commit();
		
	}
	/**
	 * Load Preferences
	 */
	public void prefsLoad(SharedPreferences mPrefs) {
		mPrefs = getPreferences(MODE_PRIVATE);
		
		//get prefs lifeform activity
		mscaleRandom=mPrefs.getInt("scalerandom",5000);
		if (debug) Log.d(tag, "prefs random= " + mscaleRandom);
		
		// get prefs play in background flag
		mplayInBackground=mPrefs.getBoolean("playinbackground", true);
		if (debug) Log.d(tag, "prefs play in background= " + mplayInBackground);
		
		// get prefs soundscape volume
		mSoundscapeVolume = mPrefs.getFloat("soundscapevolume", 0.5f);
		if (debug) Log.d(tag, "prefs soundscape volume= " + mSoundscapeVolume);
		
		
		// get currently set soundscpe
		selectedSoundscape=mPrefs.getInt("soundscapePref",1);
		if (debug) Log.d(tag, "prefs soundscape= " + selectedSoundscape);
		
		// get currently selected catagory
		selectedCatagory=mPrefs.getInt("categoryPref",1);
		if (debug) Log.d(tag, "prefs catagory= " + selectedCatagory);
		
		// get fade out timer duration
		mfadeOut=mPrefs.getInt("fadeoutPref",3600);
		if (debug) Log.d(tag, "prefs fadeout= " + mfadeOut);
		
		// get alarm repeat flag
		//ed.putBoolean("alarmrepeatPref", malarmRepeat);
		
		// get alarm interval
		//ed.putInt("alarmPref", malarm);
		//ed.commit();
	}
	// help screen activity
	private void startHelp() {
		if (debug)  Log.d(tag,"starting help");
		mhelpText = getString(R.string.help_text);
		Intent intent = new Intent(this, HelpScreen.class);
		startActivity(intent);
	}

	// About screen activity
	private void startAbout() {
		//reuse help screen with different text
		if (debug) Log.d(tag,"starting about");
		mhelpText = getString(R.string.about_text);
		Intent intent = new Intent(this, HelpScreen.class);
		startActivity(intent);
	}

	// Options screen activity 
	private void startOptions() {
		if(debug) Log.d(tag,"starting options");
		Intent intent = new Intent(this, Options.class);
		startActivity(intent);
	}

	/**
	 * 
	 */
	public static void startplaying() {
		// get the dimensions of the space between stop and play buttons
		// for showing play icons
	
		mtransportBottomEdge = (rlayout.getBottom());
		//Log.d(tag,"bottom margin "+mtransportBottomEdge);
	
		mtransportRightMargin = (rlayout.getRight() - (mPlaySoundStop
				.getWidth()));
		//Log.d(tag,"rt margin "+mtransportRightMargin);
	
		mtransportLeftMargin = (mPlaySoundStart.getWidth());
		//Log.d(tag,"left margin "+mtransportLeftMargin);
	
		mPlaying = true;
		if (mAnimationView !=null) mAnimationView.setRunning(true);
		// SoundManager.changeAllVolume(masterVolume);
	}

	public void checkchangelogshow() {
		// evaluate if we will show changelog
		try {
			// current version
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			
			// version where changeLog has been viewed
	
			int viewedChangeLogVersion = mPrefs.getInt("changelogverviewed", 0);
	
			if ( viewedChangeLogVersion < versionCode) {
				Editor editor = mPrefs.edit();
				editor.putInt("changelogverviewed", versionCode);
				editor.commit();
				showchangeLog();
			}
		} catch (NameNotFoundException e) {
			if (Lullabye.debug)  Log.w("Unable to get version code. Will not show change//Log", e);
		}
	}

	// update the time until fade textbox when appropriate
	public static void updateFadeText() {
		Calendar calendar = Calendar.getInstance();
	
		Long currenttime = calendar.getTimeInMillis() / 1000;
		if (mfadeOut > 0 && ((mfadeOut + fadeTimerSetAt) - currenttime) > 0) {
			mfadeButton.setText(mfadeTextString + "  "
					+
					// hours
					(((mfadeOut + fadeTimerSetAt) - currenttime) / 3600) + "h"
					+
					// mins
					((((mfadeOut + fadeTimerSetAt) - currenttime) / 60) % 60)
					+ "m" +
					// secs
					((mfadeOut + fadeTimerSetAt) - currenttime) % 60 + "s");
		} else if (mfadeOut > 0 && mfadeInProgress == true) {
			String msg = String.format(Locale,"Fading: Volume %2.0f",
					mSoundscapeVolume * 100);
			mfadeButton.setText(msg);
		} else {
			mfadeButton.setText(mstartFadeText + "  " + // hours
					((mfadeOut) / 3600) + "h" +
					// mins
					((mfadeOut / 60) % 60) + "m" +
					// secs
					((mfadeOut) % 60) + "s");
	
		}
	}

	public static void updateAlarmText() {
		Calendar calendar = Calendar.getInstance();
	
		Long currenttime = calendar.getTimeInMillis() / 1000;
		if (mfadeOut > 0 && ((mfadeOut + fadeTimerSetAt) - currenttime) > 0) {
			malarmButton.setText("Alarm in " +
			// hours
					(((mfadeOut + fadeTimerSetAt) - currenttime) / 3600) + "h" +
					// mins
					(((mfadeOut + fadeTimerSetAt) - currenttime) / 60) + "m" +
					// secs
					((mfadeOut + fadeTimerSetAt) - currenttime) + "s");
		} else if (mfadeOut > 0 && mfadeInProgress == true) {
			malarmButton.setText("Fading Soundscape");
		} else {
			malarmButton.setText("Set alarm");
	
		}
	
	}

	/**
	 * @return the masterVolume
	 */
	public static float getMasterVolume() {
		return mSoundscapeVolume;
	}

	/**
	 * @param masterVolume
	 *            the masterVolume to set
	 */
	public static void setMasterVolume(float masterVolume) {
		Lullabye.mSoundscapeVolume = masterVolume;
	}

	/**
	 * @return the selectedCatagory
	 */
	public static int getSelectedCatagory() {
		return selectedCatagory;
	}

	/**
	 * @return the selectedSoundscape
	 */
	public static int getSelectedSoundscape() {
		return selectedSoundscape;
	}

	/**
	 * @return the mtransportRightMargin
	 */
	public static int getMtransportRightMargin() {
		return mtransportRightMargin;
	}

	/**
	 * @return the mtransportLeftMargin
	 */
	public static int getMtransportLeftMargin() {
		return mtransportLeftMargin;
	}

	/**
	 * @return the mtransportBottomEdge
	 */
	public static int getMtransportBottomEdge() {
		return mtransportBottomEdge;
	}

	public static AnimationView getmAnimationView() {
		return mAnimationView;
	}

	public static boolean getMplayInbackground() {
		// TODO Auto-generated method stub
		return mplayInBackground;
	}

	/**
	 * @return the mPlaySoundStop
	 */
	public Button getmPlaySoundStop() {
		return mPlaySoundStop;
	}

	/**
	 * @return the mhelpText
	 */
	public static String getMhelpText() {
		return mhelpText;
	}

	public static int getMscaleRandom() {
		return mscaleRandom;
	}

	/**
	 * @return the mPlaySoundStart
	 */
	public Button getmPlaySoundStart() {
		return mPlaySoundStart;
	}

	public static int getmfadeOut() {
		// Log.d(tag,"get  fade out "+mfadeOut);
		return mfadeOut;
	}

	public static boolean getmalarmRepeat() {
		// TODO Auto-generated method stub
		return malarmRepeat;
	}

	/**
	 * @return the mPlaying
	 */
	public static boolean ismPlaying() {
		return mPlaying;
	}

	public boolean ismIsRunning() {
		return mIsRunning;
	}

	/**
	 * @param mPlaying
	 *            the mPlaying to set
	 */
	public static boolean setmPlaying(boolean mPlaying) {
		Lullabye.mPlaying = mPlaying;
		return mPlaying;
	}

	/**
	 * @param mPlaySoundStart
	 *            the mPlaySoundStart to set
	 */
	public void setmPlaySoundStart(Button mPlaySoundStart) {
		Lullabye.mPlaySoundStart = mPlaySoundStart;
	}

	/**
		 * 
		 */
	public void setsoundscapefromcategory() {
		if (selectedCatagory == 0) {
			mSoundscapeSpinner.setAdapter(soundscapeAdapter1);
		}
		if (selectedCatagory == 1) {

			mSoundscapeSpinner.setAdapter(soundscapeAdapter2);
		}
		if (selectedCatagory == 2) {
			mSoundscapeSpinner.setAdapter(soundscapeAdapter3);
		}
		if (selectedCatagory == 3) {
			mSoundscapeSpinner.setAdapter(soundscapeAdapter4);
		}

		if (selectedSoundscape > mSoundscapeSpinner.getCount()) {
			mSoundscapeSpinner.setSelection(0);
		} else {
			mSoundscapeSpinner.setSelection(selectedSoundscape);
		}

	}

	public static void setMplayInbackground(boolean b) {
		mplayInBackground = b;

	}

	public static void setmfadeOut(int timeoutInSeconds) {
		// TODO Auto-generated method stub
		mfadeOut = timeoutInSeconds;
	}

	public static void setmalarm(int timeoutInSeconds) {
		// TODO Auto-generated method stub
		malarm = timeoutInSeconds;
	}

	public static void setfadeTimerSetAt(Long currenttime) {
		fadeTimerSetAt = currenttime;

	}

	public boolean setmIsRunning(boolean mIsRunning) {
		this.mIsRunning = mIsRunning;
		return mIsRunning;
	}
	public static  void setMscaleRandom(int mscaleRandomness) {
		mscaleRandom = mscaleRandomness;
	}
	class IncomingHandlerCallback implements Handler.Callback{

	    @Override
	    public boolean handleMessage(Message message) {

	    	switch (message.what) {
			// if loading finished ok
			case FINISH_LOAD:
				// close the splash screen
				SplashScreen.sendMessage(SplashScreen.CLOSE_SPLASH);
				checkchangelogshow();
				
				// leave switch
				break;
			case START_LOAD:
				SoundManager.setMainHandler(mHandler);
				initActivity();
				break;
			// if user pressed back key
			case ABORT_LOAD:
				if (Lullabye.debug)  Log.d(tag,"pressed back closing");
				// terminate app

				finish();

			}

	        return true;
	    }
}
}
