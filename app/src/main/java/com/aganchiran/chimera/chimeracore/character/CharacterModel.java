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

package com.aganchiran.chimera.chimeracore.character;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.ItemModel;
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.chimeracore.dice.AnimaDice;

import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "character_table", foreignKeys = {@ForeignKey(onDelete = CASCADE, entity = CampaignModel.class, parentColumns = "id", childColumns = "campaignId")}, indices = {@Index("campaignId")})
public class CharacterModel extends ItemModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String description;

    private String image;

    private int displayPosition;

    private int initiative;

    private int attack;

    private int defense;

    private int initiativeMod;

    private int attackMod;

    private int defenseMod;

    private boolean attackEnabled = true;

    private boolean defenseEnabled = true;

    private int life = 100;

    private int weaponDamage = 100;

    private Integer campaignId;

    @Ignore
    private int iniRoll = 0;

    @Ignore
    private int attackRoll = 0;

    @Ignore
    private int defenseRoll = 0;

    @Ignore
    private int lastHit = 0;

    public CharacterModel(final String name, final String description, final Integer campaignId) {
        this.name = name;
        this.description = description;
        this.displayPosition = Integer.MAX_VALUE;
        this.attack = 0;
        this.defense = 0;
        this.initiativeMod = 0;
        this.attackMod = 0;
        this.defenseMod = 0;
        this.campaignId = campaignId;
    }

    @Ignore
    public CharacterModel(final CharacterModel characterModel){
        this.name = characterModel.name;
        this.description = characterModel.description;
        this.image = characterModel.image;
        this.displayPosition = characterModel.displayPosition;
        this.initiative = characterModel.initiative;
        this.attack = characterModel.attack;
        this.defense = characterModel.defense;
        this.initiativeMod = characterModel.initiativeMod;
        this.attackMod = characterModel.attackMod;
        this.defenseMod = characterModel.defenseMod;
        this.attackEnabled = characterModel.attackEnabled;
        this.defenseEnabled = characterModel.defenseEnabled;
        this.life = characterModel.life;
        this.weaponDamage = characterModel.weaponDamage;
        this.campaignId = characterModel.campaignId;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(final int displayPosition) {
        this.displayPosition = displayPosition;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(final int initiative) {
        this.initiative = initiative;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(final int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(final int defense) {
        this.defense = defense;
    }

    public int getInitiativeMod() {
        return initiativeMod;
    }

    public void setInitiativeMod(final int initiativeMod) {
        this.initiativeMod = initiativeMod;
    }

    public int getAttackMod() {
        return attackMod;
    }

    public void setAttackMod(final int attackMod) {
        this.attackMod = attackMod;
    }

    public int getDefenseMod() {
        return defenseMod;
    }

    public void setDefenseMod(final int defenseMod) {
        this.defenseMod = defenseMod;
    }

    public int getLife() {
        return life;
    }

    public void setLife(final int life) {
        this.life = life;
    }

    public int getWeaponDamage() {
        return weaponDamage;
    }

    public void setWeaponDamage(final int weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    public boolean isAttackEnabled() {
        return attackEnabled;
    }

    public void setAttackEnabled(final boolean attackEnabled) {
        this.attackEnabled = attackEnabled;
    }

    public boolean isDefenseEnabled() {
        return defenseEnabled;
    }

    public void setDefenseEnabled(final boolean defenseEnabled) {
        this.defenseEnabled = defenseEnabled;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(final Integer campaignId) {
        this.campaignId = campaignId;
    }

    public int getIniRoll() {
        return iniRoll;
    }

    public void setIniRoll(final int iniRoll) {
        this.iniRoll = iniRoll;
    }

    public int getAttackRoll() {
        return attackRoll;
    }

    public int getDefenseRoll() {
        return defenseRoll;
    }

    public int getLastHit() {
        return lastHit;
    }

    public void setLastHit(final int lastHit) {
        this.lastHit = lastHit;
    }

    public void endCombat() {
        attackRoll = 0;
        defenseRoll = 0;
        lastHit = 0;
    }

    public void hit(final int damage) {
        life -= damage;
    }

    public void rollIni() {
        int roll = AnimaDice.getRollOpen();
        switch (roll) {
            case 1:
                roll = -125;
                break;
            case 2:
                roll = -100;
                break;
            case 3:
                roll = -75;
                break;
        }
        iniRoll = roll + getInitiative() + getInitiativeMod();
    }

    public void rollAttack() {
        int roll = 0;
        if (isAttackEnabled()) {
            roll = AnimaDice.getRollOpen();
            if (roll <= 3) {
                roll -= AnimaDice.getRoll();
            }
        }
        attackRoll = roll + getAttack() + getAttackMod();
    }

    public void rollDefense() {
        int roll = 0;
        if (isDefenseEnabled()) {
            roll = AnimaDice.getRollOpen();
            if (roll <= 3) {
                roll -= AnimaDice.getRoll();
            }
        }
        defenseRoll = roll + getDefense() + getDefenseMod();
    }

    public int calculateDamage(final int defenseResult) {
        int result = (int) (((double) (getAttackRoll() - defenseResult) / 100.0) * weaponDamage);
        if (result < 0) result = 0;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof CharacterModel
                && ((CharacterModel) obj).getId() == this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean contentsTheSame(final Object obj) {
        return (obj instanceof CharacterModel
                && ((CharacterModel) obj).getId() == this.getId()
                && ((CharacterModel) obj).getName().equals(this.getName())
                && ((CharacterModel) obj).getDescription().equals(this.getDescription())
                && ((CharacterModel) obj).getDisplayPosition() == this.getDisplayPosition()
                && ((CharacterModel) obj).getInitiative() == this.getInitiative()
                && ((CharacterModel) obj).getInitiativeMod() == this.getInitiativeMod()
                && ((CharacterModel) obj).getAttack() == this.getAttack()
                && ((CharacterModel) obj).getAttackMod() == this.getAttackMod()
                && ((CharacterModel) obj).getDefense() == this.getDefense()
                && ((CharacterModel) obj).getDefenseMod() == this.getDefenseMod());
    }
}
