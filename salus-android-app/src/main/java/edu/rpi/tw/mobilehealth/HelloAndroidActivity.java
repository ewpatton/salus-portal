package edu.rpi.tw.mobilehealth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class HelloAndroidActivity extends Activity {

	private static final String TAG = HelloAndroidActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(edu.rpi.tw.mobilehealth.R.menu.main, menu);
	    return true;
    }

}

