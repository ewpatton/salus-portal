package edu.rpi.tw.calendar.android;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.CalendarFactory;

public class AndroidCalendarFactory implements CalendarFactory {

    private WeakReference<Context> context;
    private Map<String, Calendar> calendars;
    private final String[] PROJECTION = new String[] {
        Calendars._ID,
        Calendars.ACCOUNT_NAME,
        Calendars.ACCOUNT_TYPE,
        Calendars.OWNER_ACCOUNT,
        Calendars.CALENDAR_DISPLAY_NAME
    };

    public AndroidCalendarFactory( Context context ) {
        if ( context == null ) {
            throw new IllegalArgumentException( "Context cannot be null" );
        }
        this.context = new WeakReference<Context>( context );
    }

    private String esc(String s) {
        return s.replaceAll(":", "\\:");
    }

    @Override
    public Collection<String> getCalendarNames() {
        if ( calendars == null ) {
            Uri uri = Calendars.CONTENT_URI;
            ContentResolver cr = context.get().getContentResolver();
            Cursor cur = null;
            try {
                cur = cr.query(uri, PROJECTION, null, null, null);
                calendars = new HashMap<String, Calendar>();
                while( cur.moveToNext() ) {
                    Calendar c = new AndroidCalendar( cur.getLong( 0 ),
                            cur.getString( 1 ), cur.getString( 4 ) );
                    String name = "ical:" + esc( cur.getString( 1 ) ) + ":" +
                            esc( cur.getString( 2 ) ) + ":" +
                            esc( cur.getString( 3 ) );
                }
            } finally {
                if ( cur != null ) {
                    cur.close();
                }
            }
        }
        return calendars == null ? null : calendars.keySet();
    }

    @Override
    public Calendar getCalendar( String name ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Calendar> getAllCalendars() {
        // TODO Auto-generated method stub
        return null;
    }

}
