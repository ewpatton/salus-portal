package edu.rpi.tw.preferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.*;


import tw.rpi.edu.R;
import tw.rpi.edu.alarm.AlarmReceiver;
import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.android.AndroidCalendarFactory;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Editable.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
 
    private static final int RESULT_SETTINGS = 1;
    final static private long ONE_SECOND = 1000;
    final static private long FIVE_MINUTES = ONE_SECOND  * 60 * 3;
    final static String event_uri ="http://tw.rpi.edu/events#";
   
    private PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
       

        showUserSettings();
      
    }
 

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.menu_settings:
            Intent i = new Intent(this, UserSettingActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;
 
        }
 
        return true;
    }
   
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SETTINGS:
            showUserSettings();
            break;
 
        }
 
    }
 
    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
 
        StringBuilder builder = new StringBuilder();
        
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Model rdf = null;
       if (sharedPrefs.getString("calendar", null) != null){
           AndroidCalendarFactory androidCalendarFactory = new AndroidCalendarFactory(this);
         // androidCalendarFactory.getCalendarNames()
           edu.rpi.tw.calendar.Calendar calendar = androidCalendarFactory.getCalendar(sharedPrefs.getString("calendar", null));
           
         //  builder.append("\n Calendar "+ calendar.hashCode());

          
           List<Event> events = calendar.getUpcomingEvents(20*360000*24*1000);
           builder.append("\n Event " + events.size());
        //  builder.append("\n Event " +  sharedPrefs.getString("calendar", null));
           
           rdf = ModelFactory.createDefaultModel();
           

           OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
           AssetManager assetManager = getAssets();
           InputStream ims;
           try {
   			 ims = assetManager.open("ical.owl");
   			 model.read(ims, null);
   			
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}

          
        	for (Event event: events){
        		 builder.append("\n Event "
                         +  getDate( event.getStart().getTimeInMillis() - Long.valueOf(sharedPrefs.getString("notif", null)),"dd/MM/yyyy hh:mm:ss.SSS"));
        		 
        	        Resource eventr 
        	          = rdf.createResource(event_uri+event.getId())
        	                  .addProperty(RDF.type, "http://www.w3.org/2002/12/cal/ical#Vevent")
        	                  .addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#uid"),String.valueOf(event.getId()));
        	        if (event.getDescription() != null)
        	        	eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#summary"), event.getDescription());
        	        if (event.getStart() != null)
        	        	eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#dtstart"), String.valueOf(event.getStart().getTime()));
        	        	if (event.getRDate() != null)
        	        		eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#rdate"), event.getRDate());
        	        if (event.getRDate() != null)
        	        	eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#rrule"), event.getRDate());
        	       if (event.getOrganizer() != null)
        	    	   eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#organizer"), event.getOrganizer());
        	       if (event.getAvailability() != null)
        	    	   eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#status"), String.valueOf(event.getAvailability()));


        		 //builder.append( event.getStart().getTimeInMillis() -  System.currentTimeMillis());
        		 //if (event.getStart().getTimeInMillis() - System.currentTimeMillis() - FIVE_MINUTES > 0) 
        		 //am.set( AlarmManager.RTC, System.currentTimeMillis() +  FIVE_MINUTES , pi );
        	   alarmManager.set(AlarmManager.RTC, event.getStart().getTimeInMillis() - Long.valueOf(sharedPrefs.getString("notif", null)) , pendingIntent);

        	}
          
       }
       StringWriter out = new StringWriter();
	rdf.write(out);
	
       builder.append("\n" + out.toString());
      
        TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);
 
        settingsTextView.setText(builder.toString());
        
       // am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +    		FIVE_MINUTES, pi );

    }



	private String getDate(long milliSeconds, String dateFormat) {
		  DateFormat formatter = new SimpleDateFormat(dateFormat);

	        // Create a calendar object that will convert the date and time value in milliseconds to date. 
	         Calendar calendar = Calendar.getInstance();
	         calendar.setTimeInMillis(milliSeconds);
	         return formatter.format(calendar.getTime());
	}
 
}