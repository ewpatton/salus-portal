package edu.rpi.tw.calendar;

import org.junit.Test;

import junit.framework.TestCase;

public class EventEnumTestCase extends TestCase {

    @Test
    public void testEnums() {
        assertEquals( Event.Availability.BUSY, Event.Availability.BUSY );
        assertEquals( Event.Availability.FREE, Event.Availability.FREE );
        assertFalse( Event.Availability.BUSY.equals( Event.Availability.TENTATIVE ) );
    }
}
