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

package com.aganchiran.chimera.chimeracore.campaign;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CampaignModelTest {

    @Test
    public void equals() {
        final CampaignModel cm1 = new CampaignModel("Name", "Description");
        cm1.setId(1);
        final CampaignModel cm2 = new CampaignModel("Name", "Description");
        cm2.setId(2);
        final CampaignModel cm3 = new CampaignModel("Name", "Description");
        cm3.setId(cm1.getId());

        assertNotEquals(cm1, cm2);
        assertEquals(cm1, cm3);
    }

    @Test
    public void contentsTheSame() {
        final CampaignModel cm1 = new CampaignModel("Name", "Description");
        cm1.setId(1);
        final CampaignModel cm2 = new CampaignModel("Name", "Description");
        cm1.setId(2);
        final CampaignModel cm3 = new CampaignModel("Name2", "Description2");
        cm1.setId(cm1.getId());
        final CampaignModel cm4 = new CampaignModel("Name", "Description");
        cm4.setId(cm1.getId());

        assertFalse(cm1.contentsTheSame(cm2));
        assertFalse(cm1.contentsTheSame(cm3));
        assertTrue(cm1.contentsTheSame(cm4));
    }
}