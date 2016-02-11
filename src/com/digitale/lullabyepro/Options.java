package com.digitale.lullabyepro;

import java.util.Calendar;




import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TimePicker;


public class Options extends Activity {
	private static Handler mHandler;
	private static Handler mainHandler;
	private Button moptionsClose;
	private CheckBox mcheckBoxPlayInBackground;
	private SeekBar mseekBarLifeformActivity;
	protected static final int CLOSE_OPTIONS = 100;
	private int  myHour, myMinute;
	private Button mtimePickerButton;
	static final int ID_TIMEPICKER = 0;
	static final int ID_ALARMPICKER = 1;
	protected int mfadeAlarmHours;
	protected int mfadeAlarmMins;
	private Button mAlarmPickerButton;
	protected String fromWhere;
	private TextView mfadeText;
	private TextView malarmText;
	private TextView mactivityText;
	private CheckBox mcheckBoxRepeatAlarm;
	private String mfadeTextString;
	private static boolean malarmRepeat;

	private static final String tag = "lullabye";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Configuration config = getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
	    {
	        setContentView(R.layout.options_screen_port);
	    }
	     if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
	    {
	    
	        setContentView(R.layout.options_screen_land );
	    } 
		initAdView();

		
		initUI();
		mtimePickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final Calendar c = Calendar.getInstance();
				myHour = c.get(Calendar.HOUR_OF_DAY);
				myMinute = c.get(Calendar.MINUTE);
				fromWhere = "timer";
				showDialog(ID_TIMEPICKER);

			}
		});
		mAlarmPickerButton = (Button) findViewById(R.id.alarmpickerbutton);
		mAlarmPickerButton.setOnClickListener(new OnClickListener() {
			 
			 
			@Override
			public void onClick(View v) {

				final Calendar c = Calendar.getInstance();
				myHour = c.get(Calendar.HOUR_OF_DAY);
				myMinute = c.get(Calendar.MINUTE);
				// timpicker dialog needs to know where it was called from to
				// perform approprate action once user has selected time
				fromWhere = "alarm";
				showDialog(ID_ALARMPICKER);

			}
		});
		
		moptionsClose = (Button) this.findViewById(R.id.buttonOptionsClose);
		moptionsClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Lullabye.debug) Log.d(tag ,"close help pressed");
				finish();
			}
		});
	
		
		mcheckBoxPlayInBackground.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					Lullabye.setMplayInbackground(true);
					
				} else {
					Lullabye.setMplayInbackground(false);

				}
				SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
				SharedPreferences.Editor ed = mPrefs.edit();
				ed.putBoolean("playinbackground", Lullabye.mplayInBackground);
				ed.commit();
			}
		});

		mseekBarLifeformActivity
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int randomness, boolean fromTouch) {
						if (randomness <= 1000) {
							randomness = 1000;
							mseekBarLifeformActivity.setProgress(1000);
						};
						initActivity(randomness);
						Lullabye.setMscaleRandom(randomness);
						if (Lullabye.debug) Log.d(tag,"randomness is "+ (randomness));
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
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
    {
        setContentView(R.layout.options_screen_port);
    }
     if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
    {
    
        setContentView(R.layout.options_screen_land );
    } 

	initAdView();
	initUI();
    }
	private void initUI(){
		mfadeTextString = getString(R.string.fadeOutText);
		mtimePickerButton = (Button) findViewById(R.id.timepickerbutton);
		malarmText = (TextView) findViewById(R.id.textViewAlarmTime);
		mfadeText = (TextView) findViewById(R.id.textViewFadeOut);
		mactivityText=(TextView) findViewById(R.id.textViewLifeformActivityTitle);
		mcheckBoxRepeatAlarm = (CheckBox) findViewById(R.id.checkBoxRepeatAlarm);
		mcheckBoxRepeatAlarm.setChecked(isMalarmRepeat());
		mseekBarLifeformActivity = (SeekBar) findViewById(R.id.seekBarLifeformActivity);
		mcheckBoxPlayInBackground = (CheckBox) this
				.findViewById(R.id.checkBoxPlayInBackground);
		//set Life form activtiy text
		initActivity(Lullabye.mscaleRandom);
		//set alarm repeat
		setMalarmRepeat(Lullabye.getmalarmRepeat());
		//set Life form activity slider
		mseekBarLifeformActivity.setProgress((Lullabye.getMscaleRandom()));
		//set play in background checkbox
		mcheckBoxPlayInBackground.setChecked(Lullabye.getMplayInbackground());
		if (Lullabye.debug) Log.d(tag, "loaded fade out " + (Lullabye.getmfadeOut()));
		//set fade out text
		mfadeText.setText(mfadeTextString +
		// hours
				((Lullabye.getmfadeOut()) / 3600) + "h" +
				// mins
				(((Lullabye.getmfadeOut()) / 60) % 60) + "m" +
				// secs
				(Lullabye.getmfadeOut()) % 60 + "s");
	}
	private void initActivity(int randomness){
		if (randomness <= 3000) {
			mactivityText.setText("Lifeform Activity: Busy");
		} else if (randomness >= 3000 && randomness <= 6000) {
			mactivityText.setText("Lifeform Activity: Normal");
		} else if (randomness >= 3000) {
			mactivityText.setText("Lifeform Activity: Slow");
		}	
	}
	 private void initAdView() {
//		 if(Lullabye.mAdvetizing){
//		 adView=(AdView)this.findViewById(R.id.adView);
//			AdRequest adRequest = new AdRequest.Builder()
//		    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
//		    .addTestDevice("44639833DE44D2A5776E41EC613B72D8") // My test phone
//		    .build();
//			 adView.loadAd(adRequest);
//		 }else{
//			 adView=(AdView)this.findViewById(R.id.adView);
//			 adView.setVisibility(Lullabye.GONE);
//		 }
	}
	 @Override
	  public void onPause() {
	  
	    super.onPause();
	  }

	  @Override
	  public void onResume() {
	    super.onResume();
	 
	  }

	  @Override
	  public void onDestroy() {
	  
	    super.onDestroy();
	  }

	@Override
	protected Dialog onCreateDialog(int id, Bundle bundle) {

		switch (id) {
		case ID_ALARMPICKER:
			return new TimePickerDialog(this, myTimeSetListener, myHour,
					myMinute, false);
		case ID_TIMEPICKER:
			return new TimePickerDialog(this, myTimeSetListener, 0, 0, true);

		default:
			return null;
		}
	};

	private TimePickerDialog.OnTimeSetListener myTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (Lullabye.debug)  Log.d(tag,"time set is " + hourOfDay+" "+ minute);


			if (fromWhere == "timer") {
				if (Lullabye.debug)  Log.d(tag,"set timer " + hourOfDay+" "+ minute);

				// set options fadeout text to reflect time specified
				mfadeText.setText(mfadeTextString + hourOfDay + "h" + minute
						+ "m");
				// reset any countdown
				Lullabye.setfadeTimerSetAt(0l);
				// set lullabye fade parameter
				Lullabye.setmfadeOut((hourOfDay * 3600) + (minute * 60));

			} else if (fromWhere == "alarm") {
				// must put in check that set time is not in the past
				// TO DO
				if (Lullabye.debug)  Log.d(tag,"set alarm " + (hourOfDay+myHour)+" "+
				 ((minute-myMinute)));
				new Alarm(getApplicationContext(), "alarm", (hourOfDay - myHour)
						* 3600 + (minute - myMinute) * 60);

				malarmText.setText("Alarm Time " + hourOfDay + ":" + minute);
				Lullabye.setmalarm((hourOfDay * 3600) + (minute * 60));
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		if (mainHandler != null) {
			mainHandler.sendEmptyMessage(Lullabye.START_OPTIONS);
		}
	}

	public static void setMainHandler(Handler h) {
		mainHandler = h;
	}

	public static void sendMessage(Message msg) {
		mHandler.sendMessage(msg);
	}

	public static void sendMessage(int w) {
		mHandler.sendEmptyMessage(w);
	}

		
	public static boolean isMalarmRepeat() {
		return malarmRepeat;
	}

	public static void setMalarmRepeat(boolean malarmRepeat) {
		Options.malarmRepeat = malarmRepeat;
	}


}