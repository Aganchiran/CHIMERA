/*
 This file is part of CHIMERA: Companion for Humans Intending to
 Master Extreme Role Adventures ("CHIMERA").

 CHIMERA is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CHIMERA is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with CHIMERA.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aganchiran.chimera.chimeracore.consumable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ConsumableModelTest {

    @Test
    public void setMaxValue() {
        ConsumableModel consumableModel = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        consumableModel.setMaxValue(10);
        assertEquals(10, consumableModel.getCurrentValue());

        consumableModel.setMaxValue(1000);
        assertNotEquals(1000, consumableModel.getCurrentValue());
    }

    @Test
    public void setMinValue() {
        ConsumableModel consumableModel = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        consumableModel.setMinValue(60);
        assertEquals(60, consumableModel.getCurrentValue());

        consumableModel.setMinValue(-100);
        assertNotEquals(-100, consumableModel.getCurrentValue());
    }

    @Test
    public void getValueFormated() {
        ConsumableModel consumableModel = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        assertEquals("50", consumableModel.getValueFormated());

        consumableModel.setCurrentValue(50000);
        assertEquals("50K", consumableModel.getValueFormated());

        consumableModel.setCurrentValue(50000000);
        assertEquals("50M", consumableModel.getValueFormated());
    }

    @Test
    public void testEquals_Symmetric() {
        ConsumableModel cm1 = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        cm1.setId(1);
        ConsumableModel cm2 = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        cm1.setId(2);
        ConsumableModel cm3 = new ConsumableModel("Name2", 1000, 10, 500, 10, true, "blue", 10, 1);
        cm3.setId(cm1.getId());

        assertNotEquals(cm1, cm2);
        assertNotEquals(cm2, cm1);
        assertEquals(cm1, cm3);
        assertEquals(cm3, cm1);
    }

    @Test
    public void contentsTheSame() {
        ConsumableModel cm1 = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        cm1.setId(1);
        ConsumableModel cm2 = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        cm1.setId(2);
        ConsumableModel cm3 = new ConsumableModel("Name2", 1000, 10, 500, 10, true, "blue", 10, 1);
        cm3.setId(cm1.getId());
        ConsumableModel cm4 = new ConsumableModel("Name", 100, 0, 50, 1, false, "red", -1, 0);
        cm4.setId(cm1.getId());

        assertFalse(cm1.contentsTheSame(cm2));
        assertFalse(cm1.contentsTheSame(cm3));
        assertTrue(cm1.contentsTheSame(cm4));
    }
}