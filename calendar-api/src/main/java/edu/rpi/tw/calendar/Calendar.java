package edu.rpi.tw.calendar;

import java.util.List;

/**
 * The Calendar object provides access to event information for a specific
 * calendar from a {@link CalendarFactory}.
 *
 * @author ewpatton
 *
 */
public interface Calendar extends Iterable<Event> {

    /**
     * Obtain a list of {@link Event}s from the calendar.
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation.
     */
    public List<Event> getUpcomingEvents();

    /**
     * Retrieves a list of {@link Event}s from the calendar that have instances
     * that occur within a specified interval in the future from the current
     * time.
     * @param millisInFuture Limit search to a certain number of milliseconds
     * into the future
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation, limited to events with instances
     * that will occur less than millisInFuture.
     */
    public List<Event> getUpcomingEvents(long millisInFuture);

    /**
     * Obtains a list of {@link Events}s from the calendar that occurred before
     * the current time.
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation.
     */
    public List<Event> getPastEvents();

    /**
     * Retrieves a list of {@link Event}s from the calendar that have instances
     * that occur within a specified interval in the past from the current
     * time.
     * @param millisInPast Limit search to a certain number of milliseconds in
     * the past
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation, limited to events with instances
     * that have occurred less than millisInFuture in the past.
     */
    public List<Event> getPastEvents(long millisInPast);

    /**
     * Obtains a list of all events in the calendar. If the underlying
     * implementation does not support this operation, it may elect to throw
     * a {@link java.lang.UnsupportedOperationException} runtime exception.
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation.
     */
    public List<Event> getEvents();

    /**
     * Obtains an event in the calendar with a specific identifier.
     * @param _id
     * @return An {@link Event} object if the identifier denotes an event in
     * the calendar, or null if the identifier is not valid or the event has
     * been deleted.
     */
    public Event getEventById(long _id);

    /**
     * Gets the name for this calendar.
     * @return
     */
    public String getName();

    /**
     * Returns the display name for the calendar as it should be displayed in
     * a user interface.
     * @return
     */
    public String getDisplayName();

    /**
     * Returns whether the calendar is visible in the underlying calendar API.
     * Depending on the implementation, this may have further implications for
     * other methods.
     * @return true if the calendar is visible, otherwise false.
     */
    public boolean isVisible();
}
