package edu.rpi.tw.calendar.android;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import android.database.Cursor;
import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.Event;

class AndroidCalendar implements Calendar {

    private long _id;
    private String accountName;
    private String displayName;

    AndroidCalendar( long _id, String accountName, String displayName ) {
        this._id = _id;
        this.accountName = accountName;
        this.displayName = displayName;
    }

    static class EventIterator implements Iterator<Event> {

        private Cursor cursor;

        EventIterator(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return !cursor.isLast();
        }

        @Override
        public Event next() {
            if ( !cursor.moveToNext() ) {
                throw new NoSuchElementException();
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }

    @Override
    public Iterator<Event> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Event> getUpcomingEvents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Event> getUpcomingEvents(long millisInFuture) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Event getEventById(long _id) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return false;
    }

}
