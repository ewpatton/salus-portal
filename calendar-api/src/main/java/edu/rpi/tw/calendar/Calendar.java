package edu.rpi.tw.calendar;

import java.util.List;

public interface Calendar extends Iterable<Event> {
    /**
     * 
     * @return
     */
    public List<Event> getUpcomingEvents();
    public List<Event> getUpcomingEvents(long millisInFuture);
    public List<Event> getPastEvents();
    public List<Event> getPastEvents(long millisInPast);
    public List<Event> getEvents();
    public Event getEventById(long _id);
    public String getName();
    public String getDisplayName();
    public boolean isVisible();
}
