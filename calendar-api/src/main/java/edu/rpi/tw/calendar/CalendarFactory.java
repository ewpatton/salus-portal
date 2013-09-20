package edu.rpi.tw.calendar;

import java.util.Collection;

public interface CalendarFactory {
    public Collection<String> getCalendarNames();
    public Calendar getCalendar(String name);
    public Collection<Calendar> getAllCalendars();
}
