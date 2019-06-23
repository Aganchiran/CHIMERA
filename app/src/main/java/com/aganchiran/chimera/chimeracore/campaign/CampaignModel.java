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

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.ItemModel;

@Entity(tableName = "campaign_table")
public class CampaignModel extends ItemModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String description;

    private String backgroundImage;

    private int displayPosition;

    public CampaignModel(String name, String description) {
        this.name = name;
        this.description = description;
        this.displayPosition = Integer.MAX_VALUE;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CampaignModel
                && ((CampaignModel) obj).getId() == this.getId());
    }

    @Override
    public boolean contentsTheSame(Object obj) {
        return (obj instanceof CampaignModel
                && ((CampaignModel) obj).getId() == this.getId()
                && ((CampaignModel) obj).getName().equals(this.getName())
                && ((CampaignModel) obj).getDescription().equals(this.getDescription())
                && ((CampaignModel) obj).getDisplayPosition() == this.getDisplayPosition());
    }


}
