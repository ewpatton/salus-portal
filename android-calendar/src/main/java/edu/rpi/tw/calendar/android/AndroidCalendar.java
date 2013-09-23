package edu.rpi.tw.calendar.android;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.Event;

class AndroidCalendar implements Calendar {
    private static final long DAY_IN_FUTURE = 86400000;
    private final ContentResolver resolver;
    private final long _id;
    private String accountName;
    private String displayName;

    AndroidCalendar( ContentResolver cr, long _id, String accountName,
            String displayName ) {
        this.resolver = cr;
        this._id = _id;
        this.accountName = accountName;
        this.displayName = displayName;
    }

    static class EventIterator implements Iterator<Event> {

        private WeakReference<Calendar> owner;
        private ContentResolver resolver;
        private Cursor cursor;

        EventIterator(Calendar owner, ContentResolver resolver, Cursor cursor) {
            this.owner = new WeakReference<Calendar>(owner);
            this.resolver = resolver;
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            if ( cursor.moveToNext() ) {
                cursor.moveToPrevious();
                return true;
            } else {
                cursor.close();
                return false;
            }
        }

        @Override
        public Event next() {
            if ( !cursor.moveToNext() ) {
                throw new NoSuchElementException();
            }
            return new AndroidEvent( owner.get(), resolver, cursor );
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }

    private Cursor getEventCursor() {
        final Uri uri = Events.CONTENT_URI;
        String sel = "((" + Events.CALENDAR_ID + " = ?))";
        String[] args = new String[] { Long.toString( _id ) };
        return resolver.query(uri, AndroidEvent.EVENT_PROJECTION, sel, args,
                null);
    }

    @Override
    public Iterator<Event> iterator() {
        return new EventIterator( this, resolver, getEventCursor() );
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return getUpcomingEvents( DAY_IN_FUTURE );
    }

    @Override
    public List<Event> getUpcomingEvents(long millisInFuture) {
        final long now = java.util.Calendar.getInstance().getTimeInMillis();
        List<Event> events = new ArrayList<Event>();
        Set<Long> processed = new HashSet<Long>();
        Cursor cursor = Instances.query( resolver, new String[] { 
                Events.CALENDAR_ID, Instances.EVENT_ID }, now,
                now + millisInFuture);
        cursor = new CursorFilter(cursor) {
            @Override
            public boolean filterRow(Cursor cursor) {
                return _id == cursor.getLong( 0 );
            }
        }.filter();
        while(cursor.moveToNext()) {
            long eid = cursor.getLong( 1 );
            if ( !processed.contains( eid ) ) {
                events.add( getEventById( eid ) );
                processed.add( eid );
            }
        }
        cursor.close();
        return events;
    }

    @Override
    public List<Event> getPastEvents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Event> getPastEvents(long millisInPast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Event> getEvents() {
        final Uri uri = Events.CONTENT_URI;
        List<Event> events = null;
        Cursor cur = null;
        try {
            cur = resolver.query(uri, AndroidEvent.EVENT_PROJECTION, null, null,
                    null);
            events = new ArrayList<Event>();
            while ( cur.moveToNext() ) {
                events.add( new AndroidEvent( this, resolver, cur ) );
            }
        } finally {
            if ( cur != null ) {
                cur.close();
            }
        }
        return events;
    }

    @Override
    public Event getEventById(long _id) {
        final Uri uri = Events.CONTENT_URI;
        final String sel = "(" + Events._ID + " = ?)";
        final String[] args = new String[] { Long.toString( _id ) };
        Cursor cur = null;
        Event event = null;
        try {
            cur = resolver.query(uri, AndroidEvent.EVENT_PROJECTION, sel, args,
                    null);
            while ( cur.moveToNext() ) {
                event = new AndroidEvent( this, resolver, cur );
            }
        } finally {
            if ( cur != null ) {
                cur.close();
            }
        }
        return event;
    }

    @Override
    public String getName() {
        return accountName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean isVisible() {
        final Uri uri = Calendars.CONTENT_URI;
        final String[] VISIBILITY = new String[] { Calendars.VISIBLE }; 
        final String sel = "(" + Calendars._ID + " = ?)";
        final String[] args = new String[] { Long.toString(_id) };
        Cursor cur = null;
        boolean visible = false;
        try {
            cur = resolver.query(uri, VISIBILITY, sel, args, null);
            while( cur.moveToNext() ) {
                if ( 1 == cur.getInt( 0 ) ) {
                    visible = true;
                }
            }
        } finally {
            if ( cur != null ) {
                cur.close();
            }
        }
        return visible;
    }

    @Override
    public String toString() {
        return "calendar " + _id + " (" + accountName + "): " + displayName;
    }
}
