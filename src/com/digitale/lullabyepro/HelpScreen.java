package com.digitale.lullabyepro;




import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * @author Richard Beech
 */
public class HelpScreen extends Activity {
	private Button mhelpClose;
	private TextView mhelpText;
	protected static final int CLOSE_HELP = 101;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		initAdView();
		mhelpText = (TextView) this.findViewById(R.id.textViewHelp);
		mhelpText.setText(Lullabye.getMhelpText());
		mhelpClose = (Button) this.findViewById(R.id.buttonHelpClose);
		mhelpClose.setOnClickListener(new OnClickListener() {
			private String tag ="Helpscreen";

			@Override
			public void onClick(View v) {
				if (Lullabye.debug) Log.d(tag ,"close help pressed");
				finish();
			}
		});
	};
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
}