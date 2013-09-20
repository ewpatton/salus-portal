package edu.rpi.tw.calendar;

import java.util.List;

public interface Event extends Iterable<Instance> {

    public static enum Availability {
        BUSY,
        FREE,
        TENTATIVE
    }

    public Calendar getCalendar();
    public String getOrganizer();
    public String getTitle();
    public String getDescription();
    public String getLocation();
    public boolean isAllDay();
    public java.util.Calendar getStart();
    public java.util.Calendar getEnd();
    public String getDuration();
    public String getRRule();
    public String getRDate();
    public Availability getAvailability();
    public List<Instance> getInstances();
    public List<Instance> getInstances(long millisInFuture);
}
