/**
 * 
 */
package com.digitale.lullabyepro;





import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * @author rich
 * 
 */
public class SplashScreen extends Activity {

	static Handler mHandler;
	private static Handler mainHandler;
	private TextView mProgMessageText;
	public static String mloadText;
	private String tag = "splashScreen";
	protected static final int CLOSE_SPLASH = 0;
	public static Runnable updateRunnable;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Configuration config = getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
	    {
	        setContentView(R.layout.splash_screen_port);
	    }
	     if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
	    {
	    
	        setContentView(R.layout.splash_screen_land );
	    } 
		
		mProgMessageText = (TextView) findViewById(R.id.textViewLoadMessage);
		mHandler = new Handler(new IncomingHandlerCallback());
		 updateRunnable = new Runnable() {
	        public void run() {
	           
	            updateUI();
	        }

			private void updateUI() {
				mProgMessageText.setText(mloadText);
				
			}
	    };
		
	}
	  @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);

	    if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    {
	        setContentView(R.layout.splash_screen_port);
	    }
	     if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
	    {
	    
	        setContentView(R.layout.splash_screen_land );
	    } 
	    }
	@Override
	public void onStart() {
		super.onStart();
		if (mainHandler != null) {
			mainHandler.sendEmptyMessage(Lullabye.START_LOAD);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				mainHandler.sendEmptyMessage(Lullabye.ABORT_LOAD);
			} catch (NullPointerException f) {
				f.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.onKeyDown(keyCode, event);
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

	public static void setMainHandler(Handler h) {
		mainHandler = h;
	}

	public static void sendMessage(Message msg) {
		mHandler.sendMessage(msg);
	}

	public static void sendMessage(int w) {
		mHandler.sendEmptyMessage(w);

	}

	public String getMloadText() {
		return mloadText;
	}

	public static void setMloadText(String mloadText) {
		SplashScreen.mloadText = mloadText;
	}

	class IncomingHandlerCallback implements Handler.Callback {

		@Override
		public boolean handleMessage(Message message) {

			switch (message.what) {
			case CLOSE_SPLASH:

				finish();
			}

			return true;
		}
	}
}