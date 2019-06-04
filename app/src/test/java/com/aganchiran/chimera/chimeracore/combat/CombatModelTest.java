package com.aganchiran.chimera.chimeracore.combat;

import org.junit.Test;

import static org.junit.Assert.*;

public class CombatModelTest {

    @Test
    public void equals() {
        final CombatModel cm1 = new CombatModel("Name", 1);
        cm1.setId(1);
        final CombatModel cm2 = new CombatModel("Name", 1);
        cm2.setId(1);
        final CombatModel cm3 = new CombatModel("Name", 1);
        cm3.setId(2);

        assertEquals(cm1, cm2);
        assertNotEquals(cm1, cm3);
    }

    @Test
    public void contentsTheSame() {
        final CombatModel cm1 = new CombatModel("Name", 1);
        cm1.setId(1);
        final CombatModel cm2 = new CombatModel("Name", 1);
        cm2.setId(1);
        final CombatModel cm3 = new CombatModel("Name2", 1);
        cm3.setId(2);

        assertTrue(cm1.contentsTheSame(cm2));
        assertFalse(cm1.contentsTheSame(cm3));
    }
}