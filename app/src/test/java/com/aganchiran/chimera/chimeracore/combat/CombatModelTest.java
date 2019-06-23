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

package com.aganchiran.chimera.chimeracore.combat;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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