package edu.rpi.tw.mobilehealth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class QualitativeDataLogActivity extends Activity {

    private static final String TAG = QualitativeDataLogActivity.class
            .getSimpleName();

    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState
     *            If the activity is being re-initialized after previously being
     *            shut down then this Bundle contains the data it most recently
     *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
     *            is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button saveBtn = (Button)findViewById(R.id.saveBtn);
        final SeekBar painSlider = (SeekBar)findViewById(R.id.painSlider);
        final SeekBar stressSlider = (SeekBar)findViewById(R.id.stressSlider);
        final SeekBar happinessSlider = (SeekBar)findViewById(R.id.happinessSlider);
        final EditText freeText = (EditText)findViewById(R.id.freeText);

        saveBtn.setOnClickListener(new OnClickListener() {

            private final SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            private final SimpleDateFormat xsdDateTimeFormat =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            public void onClick(View v) {
                Date now = new Date();
                File dataDir = Environment.getDataDirectory();
                File dataTxt = new File(dataDir, "data_" + sdf.format(now) +
                        ".txt");
                PrintStream ps;
                try {
                    ps = new PrintStream(new FileOutputStream(dataTxt));
                    ps.println("date,pain,stress,happiness,free_text");
                    ps.print(xsdDateTimeFormat.format(now));
                    ps.print(",");
                    ps.print(painSlider.getProgress());
                    ps.print(",");
                    ps.print(stressSlider.getProgress());
                    ps.print(",");
                    ps.print(happinessSlider.getMax() -
                            happinessSlider.getProgress());
                    ps.print(",");
                    ps.println("\"" + freeText.getText().toString()
                            .replaceAll("\r", "\\r").replaceAll("\n", "\\n") +
                            "\"");
                    ps.close();
                } catch (FileNotFoundException e) {
                    Log.w(TAG, "Unable to open data file.", e);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(edu.rpi.tw.mobilehealth.R.menu.main, menu);
        return true;
    }

}
