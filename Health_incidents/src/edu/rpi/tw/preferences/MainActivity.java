package edu.rpi.tw.preferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import tw.rpi.edu.R;
import tw.rpi.edu.alarm.AlarmReceiver;
import tw.rpi.edu.expandablelistview.ExpandableListAdapter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.ical.compat.jodatime.LocalDateIteratorFactory;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.android.AndroidCalendarFactory;
 
public class MainActivity extends Activity {
 
    private static final int RESULT_SETTINGS = 1;
    final static private long ONE_SECOND = 1000;
    final static private long FIVE_MINUTES = ONE_SECOND  * 60 * 3;
    final static String event_uri ="http://tw.rpi.edu/events#";
   long day = 86400000;
    private PendingIntent pendingIntent;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private static final DateTimeFormatter XML_DATE_TIME_FORMAT =
            ISODateTimeFormat.dateTimeNoMillis();

    Model rdf;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
       

//        expListView = (ExpandableListView) findViewById(R.id.lvExp);
//        
//        // preparing list data
//        prepareListData();
// 
//        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
// 
//        // setting list adapter
//        expListView.setAdapter(listAdapter);
// 
        try {
			getInciddents();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

      
    }
    
  

 
    public void getInciddents() throws UnsupportedEncodingException, ParseException{
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        rdf = ModelFactory.createDefaultModel();


        StringBuilder builder = new StringBuilder();
        if (sharedPrefs.getString("calendar", null) != null){
            AndroidCalendarFactory androidCalendarFactory = new AndroidCalendarFactory(this);
            edu.rpi.tw.calendar.Calendar calendar = androidCalendarFactory.getCalendar(sharedPrefs.getString("calendar", null));
            
           List<Event> events = calendar.getPastEvents(300*day);
       		for (Event event: events){
//       			builder.append(event.getOrganizer()+"\n");
//       			builder.append(event.getTitle()+"\n");
//       			builder.append(event.getDescription()+"\n");
//       			builder.append(getDate(event.getStart().getTimeInMillis(),"dd/MM/yyyy hh:mm:ss.SSS"));

       			Calendar cal = new GregorianCalendar();
       			
       			Model model = ModelFactory.createDefaultModel();
              AssetManager assetManager = getAssets();
              InputStream ims;
              try {
      			 ims = assetManager.open("medicalEffects.rdf");
      			 model.read(ims, null);
      			
      		} catch (IOException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
              
              String queryS = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
              		+ "PREFIX me: <http://tw.rpi.edu/medical_effects#> "
      		 		+ "SELECT DISTINCT ?start ?end ?tag ?measurement ?symptom WHERE { "
      		 		+ " ?effect me:hasStart ?start ."
      		 		+ " ?effect me:hasEnd ?end . "
      		 		+ "?event me:generates ?effect ."
      		 		+ "?effect me:affects ?measurement . "
      		 		+ "?event me:hasTag ?tag . "
      		 		+ "?effect me:symptom ?symptom . "
      		 		+ " }";

      	    Query query = QueryFactory.create(queryS);
      	    QueryExecution qExe = QueryExecutionFactory.create(query, model);
      	    ResultSet resultsRes = qExe.execSelect();

      	  while(resultsRes.hasNext()) {
  	    	QuerySolution result = resultsRes.next();
  	    	String tag = result.get("tag").toString().toLowerCase();
  	    	int end = Integer.valueOf(result.get("end").toString());
  	    	int st = Integer.valueOf(result.get("start").toString());
  	    	String measurement = result.get("measurement").toString();
  	    	
  	    	String symptom = result.get("symptom").toString();

  	    	
              if(event.getDescription().toLowerCase().contains(tag)||event.getTitle().toLowerCase().contains(tag)){
       			LocalDate start = new LocalDate(event.getStart());
       			
            		if (event.getRRule() != null){
     				for (LocalDate date :
     			         LocalDateIteratorFactory.createLocalDateIterable("RRULE:"+event.getRRule(), start, true)) {
     					  Resource eventr 
     	                  = rdf.createResource(event_uri+event.getId()+date.hashCode())
     	                          .addProperty(RDF.type, "http://www.w3.org/2002/12/cal/ical#Vevent")
     	                          .addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#uid"),String.valueOf(event.getId()+date.hashCode()));
     					 rdf.createResource(event_uri+event.getId()+date.hashCode())
                          .addProperty(rdf.createProperty("http://tw.rpi.edu/medical_effects#hasTag"),tag);
     					 rdf.addLiteral(eventr, rdf.createProperty("http://tw.rpi.edu/medical_effects#symptom") , symptom);

  					  	cal.setTime(date.plusDays(st).toDate());
     					 rdf.addLiteral(eventr, rdf.createProperty("http://tw.rpi.edu/medical_effects#hasStart") , new XSDDateTime(cal));
     					  //	eventr.addProperty(rdf.createProperty("http://tw.rpi.edu/medical_effects#effect_start"),  new XSDDateTime(cal).toString());
     					  	cal.setTime(date.plusDays(end).toDate());
        					 rdf.addLiteral(eventr, rdf.createProperty("http://tw.rpi.edu/medical_effects#hasEnd") , new XSDDateTime(cal));
    		  	        	rdf.add(eventr,rdf.createProperty("http://tw.rpi.edu/medical_effects#affects"), rdf.createResource(measurement));
     			    }

     			}
     				else {
     					  Resource eventr 
     	                  = rdf.createResource(event_uri+event.getId()+start.hashCode())
     	                          .addProperty(RDF.type, "http://www.w3.org/2002/12/cal/ical#Vevent")
     	                          .addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#uid"),String.valueOf(event.getId()+start.hashCode()));
   					  	cal.setTime(start.plusDays(st).toDate());
    					 rdf.createResource(event_uri+event.getId()+start.hashCode())
                        .addProperty(rdf.createProperty("http://tw.rpi.edu/medical_effects#hasTag"),tag);

     					 rdf.addLiteral(eventr, rdf.createProperty("http://tw.rpi.edu/medical_effects#hasStart") , new XSDDateTime(cal));
     					 rdf.addLiteral(eventr, rdf.createProperty("http://tw.rpi.edu/medical_effects#symptom") , symptom);

     					  //	eventr.addProperty(rdf.createProperty("http://tw.rpi.edu/medical_effects#effect_start"),  new XSDDateTime(cal).toString());
     					  	cal.setTime(start.plusDays(end).toDate());
        					 rdf.addLiteral(eventr, rdf.createProperty("http://tw.rpi.edu/medical_effects#hasEnd") , new XSDDateTime(cal));
    		  	        	rdf.add(eventr,rdf.createProperty("http://tw.rpi.edu/medical_effects#affects"), rdf.createResource(measurement));
  
     				}
     				

     			//builder.append(getDate(event.getStart().getTimeInMillis(),"dd/MM/yyyy hh:mm:ss.SSS")+"\n");
     			//builder.append(getDate(event.getStart().getTimeInMillis()+10*day,"dd/MM/yyyy hh:mm:ss.SSS")+"\n");


            	
  	        	
              }
       		}
       		}
        	//builder.append(calendar.getDisplayName());

        	
        }
       

//       StringWriter out = new StringWriter();
//    	rdf.write(out);
////    	
//          builder.append( out.toString());
////         
//        TextView textView = (TextView) findViewById(R.id.textUserSettings);
////       
//        textView.setText(builder.toString());
//        textView.setMovementMethod(new ScrollingMovementMethod());

        new SparqlRequest().execute("");
       

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
 
    //    StringBuilder builder = new StringBuilder();
        
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,0);
        try {
			getInciddents();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    //    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

//        Model rdf = null;
//       if (sharedPrefs.getString("calendar", null) != null){
//           AndroidCalendarFactory androidCalendarFactory = new AndroidCalendarFactory(this);
//         // androidCalendarFactory.getCalendarNames()
//           edu.rpi.tw.calendar.Calendar calendar = androidCalendarFactory.getCalendar(sharedPrefs.getString("calendar", null));
//           
//         //  builder.append("\n Calendar "+ calendar.hashCode());
//       	builder.append(calendar.getDisplayName() +"\n" + calendar.getAccountName() + "\n" + calendar.getName());
//
//          
//           List<Event> events = calendar.getUpcomingEvents(20*360000*24*1000);
//           builder.append("\n Event " + events.size());
//        //  builder.append("\n Event " +  sharedPrefs.getString("calendar", null));
//           
//           rdf = ModelFactory.createDefaultModel();
//           
//
//           OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
//           AssetManager assetManager = getAssets();
//           InputStream ims;
//           try {
//   			 ims = assetManager.open("ical.owl");
//   			 model.read(ims, null);
//   			
//   		} catch (IOException e) {
//   			// TODO Auto-generated catch block
//   			e.printStackTrace();
//   		}
//
//          
//        	for (Event event: events){
//        		 builder.append("\n Event "
//                         +  getDate( event.getStart().getTimeInMillis() - Long.valueOf(sharedPrefs.getString("notif", null)),"dd/MM/yyyy hh:mm:ss.SSS"));
//        		 
//        	        Resource eventr 
//        	          = rdf.createResource(event_uri+event.getId())
//        	                  .addProperty(RDF.type, "http://www.w3.org/2002/12/cal/ical#Vevent")
//        	                  .addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#uid"),String.valueOf(event.getId()));
//        	        if (event.getDescription() != null)
//        	        	eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#summary"), event.getDescription());
//        	        if (event.getStart() != null)
//        	        	eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#dtstart"), String.valueOf(event.getStart().getTime()));
//        	        	if (event.getRDate() != null)
//        	        		eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#rdate"), event.getRDate());
//        	        if (event.getRDate() != null)
//        	        	eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#rrule"), event.getRDate());
//        	       if (event.getOrganizer() != null)
//        	    	   eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#organizer"), event.getOrganizer());
//        	       if (event.getAvailability() != null)
//        	    	   eventr.addProperty(rdf.createProperty("http://www.w3.org/2002/12/cal/ical#status"), String.valueOf(event.getAvailability()));
//
//
//        		 //builder.append( event.getStart().getTimeInMillis() -  System.currentTimeMillis());
//        		 //if (event.getStart().getTimeInMillis() - System.currentTimeMillis() - FIVE_MINUTES > 0) 
//        		 //am.set( AlarmManager.RTC, System.currentTimeMillis() +  FIVE_MINUTES , pi );
//        	   alarmManager.set(AlarmManager.RTC, event.getStart().getTimeInMillis() - Long.valueOf(sharedPrefs.getString("notif", null)) , pendingIntent);

        //	}
        
          
     //  }
//       StringWriter out = new StringWriter();
//	rdf.write(out);
//	
      // builder.append("\n" + out.toString());

      
       // TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);
 
       // settingsTextView.setText(builder.toString());
        
       // am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +    		FIVE_MINUTES, pi );

    }



	private String getDate(long milliSeconds, String dateFormat) {
		  DateFormat formatter = new SimpleDateFormat(dateFormat);

	        // Create a calendar object that will convert the date and time value in milliseconds to date. 
	         Calendar calendar = Calendar.getInstance();
	         calendar.setTimeInMillis(milliSeconds);
	         return formatter.format(calendar.getTime());
	}
 


private class SparqlRequest extends AsyncTask<String, Void, String> {
	ResultSet resultSet = null;
	@Override
    protected String doInBackground(String... params) {
    	 String queryS = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
		 		+ "PREFIX dcterms: <http://purl.org/dc/terms/> "
		 		+ "SELECT DISTINCT  ?characteristic_label ?characteristic ?symptom ?date WHERE { "
		 		+ "?m <http://mobilehealth.tw.rpi.edu/ontology/health.ttl#ofCharacteristic> ?characteristic . "
		 		+ "?characteristic rdfs:label ?characteristic_label . "
		 		+ "?m 	<http://mobilehealth.tw.rpi.edu/source/mobilehealth-tw-rpi-edu/dataset/bloodwork/vocab/enhancement/1/high_low>  ?symptom . "
		 		+ "?m dcterms:date ?date } "
		 		+ "order by ?characteristic ?symptom";

         Query query = QueryFactory.create(queryS, Syntax.syntaxARQ);

         QueryExecution qe = QueryExecutionFactory.sparqlService("http://mobilehealth.tw.rpi.edu/sparql", query);
           resultSet = qe.execSelect();
         
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
    	expListView = (ExpandableListView) findViewById(R.id.lvExp);
      
      // preparing list data
      prepareListData(resultSet);

      listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);

      // setting list adapter
      expListView.setAdapter(listAdapter);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {
       
    }
}

private String getJustification(String anomaly, String symptom, DateTime dt)
{
	  String queryS = "PREFIX dcterms: <http://purl.org/dc/terms/> "
	  		+ "PREFIX me: <http://tw.rpi.edu/medical_effects#> "
		 		+ "SELECT DISTINCT ?start ?end ?effect_name WHERE { "
		 		+ "?m me:affects <"+ anomaly +"> . "
		 		+ " ?m me:hasStart ?start ."
		 		+ " ?m me:hasEnd ?end ."
		 		+ " ?m me:hasTag ?effect_name  ."
		 		+ " ?m me:symptom '" + symptom +"' }";

	    Query query = QueryFactory.create(queryS);
	    QueryExecution qExe = QueryExecutionFactory.create(query, rdf);
	    ResultSet resultsRes = qExe.execSelect();

	    String justification ="";
	    while(resultsRes.hasNext()) {
	    	QuerySolution next = resultsRes.next();
	    	if (new DateTime(next.get("start").toString().split("\\^")[0]).isBefore(dt) && new DateTime(next.get("end").toString().split("\\^")[0]).isAfter(dt)  ) 
	    	justification += " : " + next.get("effect_name").toString() + " on " + new DateTime(next.get("start").toString().split("\\^")[0]).toString("MM/dd/yyyy");
	    }
	 
	return justification;
}
private void prepareListData(ResultSet resultSet) {
    DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
      listDataHeader = new ArrayList<String>();
    listDataChild = new HashMap<String, List<String>>();

    // Adding child data
    // listDataHeader.add("Now Showing");
    //listDataHeader.add("Coming Soon..");

    // Adding child data

    while (resultSet.hasNext())
    {
    	//DateTime dt = new DateTime(resultSet.next().get("date").toString().split("\\^")[0]);
    	
    	QuerySolution next = resultSet.next();
    	String characteristic_label = next.get("characteristic_label").toString();
    	String symptom = next.get("symptom").toString();
    	String anomaly =  next.get("characteristic").toString();
    	if (symptom.equals("H"))
    	{
    		symptom = "High";
    	}
    	else if (symptom.equals("L"))
    	{
    		symptom = "Low";
    	}
    	
    	characteristic_label += " " +symptom;

		DateTime dt = new DateTime(next.get("date").toString().split("\\^")[0]);
		String date = fmt.print(dt);
		if (!listDataHeader.contains(characteristic_label)){
			listDataHeader.add(characteristic_label);
			listDataChild.put(characteristic_label, new ArrayList<String>());
		}
		listDataChild.get(characteristic_label).add(date + getJustification(anomaly,symptom,dt));
		
		
    	
    }
//    List<String> nowShowing = new ArrayList<String>();
//    nowShowing.add("The Conjuring");
//    nowShowing.add("Despicable Me 2");
//    nowShowing.add("Turbo");
//    nowShowing.add("Grown Ups 2");
//    nowShowing.add("Red 2");
//    nowShowing.add("The Wolverine");
//
//    List<String> comingSoon = new ArrayList<String>();
//    comingSoon.add("2 Guns");
//    comingSoon.add("The Smurfs 2");
//    comingSoon.add("The Spectacular Now");
//    comingSoon.add("The Canyons");
//    comingSoon.add("Europa Report");

   // Header, Child data
//    listDataChild.put(listDataHeader.get(1), nowShowing);
//    listDataChild.put(listDataHeader.get(2), comingSoon);
}
}