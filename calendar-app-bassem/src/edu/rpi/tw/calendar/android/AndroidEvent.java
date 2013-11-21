package edu.rpi.tw.calendar.android;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.Instance;

class AndroidEvent implements Event {
    public static final String[] EVENT_PROJECTION = {
        Events._ID,
        Events.ORGANIZER,
        Events.TITLE,
        Events.DESCRIPTION,
        Events.EVENT_LOCATION,
        Events.ALL_DAY,
        Events.DTSTART,
        Events.DTEND,
        Events.DURATION,
        Events.RRULE,
        Events.RDATE,
        Events.AVAILABILITY
    };
    private static final int _ID = 0;
    private static final int ORGANIZER = 1;
    private static final int TITLE = 2;
    private static final int DESCRIPTION = 3;
    private static final int EVENT_LOCATION = 4;
    private static final int ALL_DAY = 5;
    private static final int DTSTART = 6;
    private static final int DTEND = 7;
    private static final int DURATION = 8;
    private static final int RRULE = 9;
    private static final int RDATE = 10;
    private static final int AVAILABILITY = 11;
    private static final long DAY_LENGTH = 86400000;

    static class InstanceIterator implements Iterator<Instance> {

        private final WeakReference<Event> owner;
        private final Cursor cursor;

        InstanceIterator(Event owner, Cursor cursor) {
            this.owner = new WeakReference<Event>(owner);
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return !(cursor.isLast() || cursor.isAfterLast());
        }

        @Override
        public Instance next() {
            if ( !cursor.moveToNext() ) {
                throw new NoSuchElementException();
            }
            return new AndroidInstance( owner.get(), cursor );
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }

    private final WeakReference<Calendar> owner;
    private final ContentResolver resolver;
    private final long _id;
    private String organizer;
    private String title;
    private String description;
    private String location;
    private boolean allDay;
    private java.util.Calendar start;
    private java.util.Calendar end;
    private String duration;
    private String rrule;
    private String rdate;
    private Availability availability;

    AndroidEvent( Calendar owner, ContentResolver resolver, Cursor cursor ) {
        this.owner = new WeakReference<Calendar>(owner);
        this.resolver = resolver;
        this._id = cursor.getLong( _ID );
        this.organizer = cursor.getString( ORGANIZER );
        this.title = cursor.getString( TITLE );
        this.description = cursor.getString( DESCRIPTION );
        this.location = cursor.getString( EVENT_LOCATION );
        this.allDay = cursor.getInt( ALL_DAY ) == 1;
        long dt = cursor.getLong( DTSTART );
        this.start = java.util.Calendar.getInstance();
        this.start.setTimeInMillis( dt );
        dt = cursor.getLong( DTEND );
        this.end = java.util.Calendar.getInstance();
        this.end.setTimeInMillis( dt );
        this.duration = cursor.getString( DURATION );
        this.rrule = cursor.getString( RRULE );
        this.rdate = cursor.getString( RDATE );
        int a = cursor.getInt( AVAILABILITY );
        if ( a == Events.AVAILABILITY_BUSY ) {
            this.availability = Availability.BUSY;
        } else if ( a == Events.AVAILABILITY_FREE ) {
            this.availability = Availability.FREE;
        } else {
            this.availability = Availability.TENTATIVE;
        }
    }

    static class EventCursorFilter extends CursorFilter {
        private final long _id;
        private final int ID;

        EventCursorFilter(long _id, Cursor cursor) {
            super(cursor);
            this._id = _id;
            this.ID = cursor.getColumnIndex( Instances.EVENT_ID );
        }

        @Override
        public boolean filterRow(Cursor cursor) {
            return _id == cursor.getLong( ID );
        }        
    }

    private Cursor getInstanceCursor() {
        final long start = java.util.Calendar.getInstance().getTimeInMillis();
        return new EventCursorFilter( _id,
                Instances.query( resolver, AndroidInstance.INSTANCE_PROJECTION,
                        start, start + DAY_LENGTH )).filter();
//        final Uri uri = Instances.CONTENT_URI;
//        final Uri uri = Uri.parse("content://com.android.calendar/instances");
//        String sel = "((" + Instances.EVENT_ID + " = ?))";
//        String[] args = new String[] { Long.toString( _id ) };
//        return resolver.query(uri, AndroidInstance.INSTANCE_PROJECTION, sel,
//                args, null);
    }

    @Override
    public Iterator<Instance> iterator() {
        return new InstanceIterator( this, getInstanceCursor() );
    }

    @Override
    public long getId() {
        return this._id;
    }

    @Override
    public Calendar getCalendar() {
        return owner.get();
    }

    @Override
    public String getOrganizer() {
        return this.organizer;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public boolean isAllDay() {
        return this.allDay;
    }

    @Override
    public java.util.Calendar getStart() {
        return this.start;
    }

    @Override
    public java.util.Calendar getEnd() {
        return this.end;
    }

    @Override
    public String getDuration() {
        return this.duration;
    }

    @Override
    public String getRRule() {
        return this.rrule;
    }

    @Override
    public String getRDate() {
        return this.rdate;
    }

    @Override
    public Availability getAvailability() {
        return this.availability;
    }

    @Override
    public List<Instance> getInstances() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instance> getInstances(long millisInFuture) {
        // TODO Auto-generated method stub
        return null;
    }

}
