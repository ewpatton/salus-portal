package edu.rpi.tw.calendar;

/**
 * The Instance interface provides a wrapper around implementation-dependent
 * details of calendar event instances. It is meant to support accessing
 * the start/end times for a specific event occurrence. Instances may not
 * actually be stored anywhere in the implementation but instead be generated
 * at runtime in the event a recurrence rule exists in the parent event.
 * @author ewpatton
 *
 */
public interface Instance {

    /**
     * Obtains a {@link java.util.Calendar Calendar} representing the starting
     * date and time of this instance.
     * @return
     */
    public java.util.Calendar getStart();

    /**
     * Obtains a {@link java.util.Calendar Calendar} representing the ending
     * date and time of this instance.
     * @return
     */
    public java.util.Calendar getEnd();

    /**
     * Gets the parent {@link Event} for this instance. Note: Implementations
     * SHOULD use {@link java.lang.ref.WeakReference} to maintain parent links
     * to keep the calendar structure tree-like for garbage collection.
     * @return
     */
    public Event getEvent();
}
