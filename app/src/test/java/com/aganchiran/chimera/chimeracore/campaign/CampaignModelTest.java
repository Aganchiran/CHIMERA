package com.aganchiran.chimera.chimeracore.campaign;

import org.junit.Test;

import static org.junit.Assert.*;

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