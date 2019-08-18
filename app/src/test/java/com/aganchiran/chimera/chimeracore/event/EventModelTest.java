package com.aganchiran.chimera.chimeracore.event;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventModelTest {

    @Test
    public void equals() {
        final EventModel cm1 = new EventModel("Name", "Description", 100f, 100f, 1);
        cm1.setId(1);
        final EventModel cm2 = new EventModel("Name", "Description", 100f, 100f, 1);
        cm2.setId(2);
        final EventModel cm3 = new EventModel("Name", "Description", 100f, 100f, 1);
        cm3.setId(cm1.getId());

        assertNotEquals(cm1, cm2);
        assertEquals(cm1, cm3);
    }

    @Test
    public void contentsTheSame() {
        final EventModel cm1 = new EventModel("Name", "Description", 100f, 100f, 1);
        cm1.setId(1);
        final EventModel cm2 = new EventModel("Name", "Description", 100f, 100f, 1);
        cm1.setId(2);
        final EventModel cm3 = new EventModel("Name2", "Description2", 200f, 200f, 2);
        cm1.setId(cm1.getId());
        final EventModel cm4 = new EventModel("Name", "Description", 100f, 100f, 1);
        cm4.setId(cm1.getId());

        assertFalse(cm1.contentsTheSame(cm2));
        assertFalse(cm1.contentsTheSame(cm3));
        assertTrue(cm1.contentsTheSame(cm4));
    }
}