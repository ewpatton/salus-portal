package edu.rpi.tw.calendar.android;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import android.database.Cursor;
import android.provider.CalendarContract.Instances;
import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.Instance;

class AndroidInstance implements Instance {
    public static final String[] INSTANCE_PROJECTION = {
        Instances._ID,
        Instances.BEGIN,
        Instances.END,
        Instances.EVENT_ID
    };
    private static final int BEGIN = 1;
    private static final int END = 2;

    private final WeakReference<Event> event;
    private Calendar start;
    private Calendar end;

    AndroidInstance( Event event, Cursor cursor ) {
        this.event = new WeakReference<Event>(event);
        long dt = cursor.getLong( BEGIN );
        this.start = Calendar.getInstance();
        this.start.setTimeInMillis( dt );
        dt = cursor.getLong( END );
        this.end = Calendar.getInstance();
        this.end.setTimeInMillis( dt );
    }

    @Override
    public Calendar getStart() {
        return (Calendar) this.start.clone();
    }

    @Override
    public Calendar getEnd() {
        return (Calendar) this.end.clone();
    }

    @Override
    public Event getEvent() {
        return event.get();
    }

}
