package edu.rpi.tw.preferences;

import java.util.ArrayList;
import java.util.Iterator;

import tw.rpi.edu.R;
import edu.rpi.tw.calendar.Calendar;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.android.AndroidCalendarFactory;

public class UserSettingActivity extends PreferenceActivity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
       
        AndroidCalendarFactory androidCalendarFactory = new AndroidCalendarFactory(this);

        Iterator<Calendar> calendars = androidCalendarFactory.getAllCalendars().iterator();

        ArrayList<CharSequence> entries_list = new ArrayList<CharSequence>();
        ArrayList<CharSequence> entryValues_list = new ArrayList<CharSequence>();

       
        
        while (calendars.hasNext()){
     		Calendar calendar = calendars.next();
     		entries_list.add( calendar.getDisplayName());
     		entryValues_list.add( calendar.getName());
     	}
        
        CharSequence[] entries = new CharSequence[entries_list.size()];
        CharSequence[] entryValues =  new CharSequence[entryValues_list.size()];
        
        entries_list.toArray(entries);
        entryValues_list.toArray(entryValues);
        
        ListPreference lp = (ListPreference)findPreference("calendar");
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
 
    }
}