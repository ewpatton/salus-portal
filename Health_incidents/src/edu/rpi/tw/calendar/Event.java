package edu.rpi.tw.calendar;

import java.util.List;

/**
 * The Event interface provides a contract for accessing calendar event data.
 * An Event includes at a minimum a title and at least one instance that has
 * a specified start/end time (or is all day, see {@link #isAllDay}).
 * @author ewpatton
 *
 */
public interface Event extends Iterable<Instance> {

    /**
     * Availability is an enumeration used to identify the availability of an
     * individual based on the information present in the calendar.
     * @author ewpatton
     *
     */
    public static enum Availability {
        BUSY,
        FREE,
        TENTATIVE
    }

    /**
     * Gets a unique identifier for this event that can be used to reference
     * it in future calls to other functions, such as
     * {@link Calendar#getEventById(long)}.
     * @return
     */
    public long getId();

    /**
     * Gets the parent calendar for this event. Note: implementations SHOULD
     * make use of {@link java.lang.ref.WeakReference WeakReferences}s to
     * maintain parent references and keep the calendar structure tree-like for
     * garbage collection.
     * @return
     */
    public Calendar getCalendar();

    /**
     * Gets a string naming the organizer of the calendar. The information
     * content is implementation-defined. It may be, for example, an email
     * address or the name of the individual holding the calendar account.
     * @return
     */
    public String getOrganizer();

    /**
     * Gets the title of the event in the calendar.
     * @return
     */
    public String getTitle();

    /**
     * Gets a description of the event. If no description is provided by the
     * implementationm, this function MAY return null.
     * @return
     */
    public String getDescription();

    /**
     * Gets the location description of the event. If no location information
     * is provided by the implementatino, this function MAY return null.
     * @return
     */
    public String getLocation();

    /**
     * Indicates if the event is an all-day event or not.
     * @return
     */
    public boolean isAllDay();

    /**
     * Obtains a {@link java.util.Calendar} object representing the start time
     * of this calendar event. If this event is recurring, it is implementation-
     * defined as to which specific instance the calendar object represents.
     * @return
     */
    public java.util.Calendar getStart();

    /**
     * Obtains a {@link java.util.Calendar} object representing the end time
     * of this calendar event. If this event is recurring, it is implementation-
     * defined as to which specific instance the calendar object represents.
     * @return
     */
    public java.util.Calendar getEnd();

    /**
     * Gets the duration of the event in the format specified by
     * {@link http://tools.ietf.org/html/rfc5545#section-3.8.2.5 IETF RFC 5545}.
     * @return
     */
    public String getDuration();

    /**
     * Gets the recurrence rule for this event if the user has set one.
     * @return
     */
    public String getRRule();

    /**
     * Gets the recurrence dates for this event if the user has set any.
     * @return
     */
    public String getRDate();

    /**
     * Gets the individual's availability for this event as specified in the
     * underlying calendar data.
     * @return
     */
    public Availability getAvailability();

    /**
     * Gets a {@link java.util.List List} of {@link Instance}s for this event.
     * Note: an implementation MAY throw an
     * {@link java.lang.UnsupportedOperationException UnsupportedOperationException}
     * in the event that it cannot generate a finite list of instances. It MAY
     * elect to return a subset of valid instances.
     * @return
     */
    public List<Instance> getInstances();

    /**
     * Gets a {@link java.util.List List} of {@link Instances}s for this event
     * that will occur within the specified number of milliseconds from the
     * current time.
     * @param millisInFuture
     * @return
     */
    public List<Instance> getInstances(long millisInFuture);
}
