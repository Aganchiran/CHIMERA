package com.aganchiran.chimera.chimeracore.character;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.aganchiran.chimera.chimeracore.ItemModel;
import com.aganchiran.chimera.chimeracore.dice.AnimaDice;

import java.util.Objects;

@Entity(tableName = "character_table")
public class CharacterModel extends ItemModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String description;

    private int displayPosition;

    private int initiative;

    private int attack;

    private int defense;

    private int initiativeMod;

    private int attackMod;

    private int defenseMod;

    @Ignore
    private int iniRoll = 0;

    public CharacterModel(String name, String description) {
        this.name = name;
        this.description = description;
        this.displayPosition = Integer.MAX_VALUE;
        this.attack = 0;
        this.defense = 0;
        this.initiativeMod = 0;
        this.attackMod = 0;
        this.defenseMod = 0;
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

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getInitiativeMod() {
        return initiativeMod;
    }

    public void setInitiativeMod(int initiativeMod) {
        this.initiativeMod = initiativeMod;
    }

    public int getAttackMod() {
        return attackMod;
    }

    public void setAttackMod(int attackMod) {
        this.attackMod = attackMod;
    }

    public int getDefenseMod() {
        return defenseMod;
    }

    public void setDefenseMod(int defenseMod) {
        this.defenseMod = defenseMod;
    }

    public int getIniRoll() {
        return iniRoll;
    }

    public void setIniRoll(int iniRoll) {
        this.iniRoll = iniRoll;
    }

    public void rollIni(){
        int roll = AnimaDice.getRoll();
        switch (roll){
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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CharacterModel
                && ((CharacterModel) obj).getId() == this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean contentsTheSame(Object obj){
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
