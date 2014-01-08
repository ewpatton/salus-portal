package edu.rpi.tw.calendar;

import java.util.Collection;

/**
 * The CalendarFactory is an interface to an environment-specific factory
 * for accessing calendar information.
 *
 * @author ewpatton
 *
 */
public interface CalendarFactory {
    /**
     * Obtains a collection of calendar names from the underlying model that
     * can be constructed by this factory.
     * @return
     */
    public Collection<String> getCalendarNames();

    /**
     * Obtains a specific calendar given the calendar's name.
     * @param name
     * @return
     */
    public Calendar getCalendar(String name);

    /**
     * Obtains a collection of all calendars from the underlying model.
     * @return
     */
    public Collection<Calendar> getAllCalendars();
}
