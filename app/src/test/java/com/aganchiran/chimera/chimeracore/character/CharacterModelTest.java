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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CharacterModelTest {

    @Test
    public void endCombat() {
        int expectedResult = 0;

        CharacterModel cm1 = new CharacterModel("Name", "Description", 0);
        cm1.setAttack(30);
        cm1.setAttackEnabled(false);
        rollAttack();

        cm1.setDefense(20);
        cm1.setDefenseEnabled(false);
        rollDefense();

        cm1.setLastHit(100);

        cm1.endCombat();
        assertEquals(expectedResult, cm1.getAttackRoll());
        assertEquals(expectedResult, cm1.getDefenseRoll());
        assertEquals(expectedResult, cm1.getLastHit());
    }

    @Test
    public void hit() {
        int expectedResult = 70;
        CharacterModel cm1 = new CharacterModel("Name", "Description", 0);
        cm1.setLife(100);
        cm1.hit(30);

        assertEquals(expectedResult, cm1.getLife());
    }

    @Test
    public void rollAttack() {
        int expectedResult = 120;
        CharacterModel cm1 = new CharacterModel("Name", "Description", 0);
        cm1.setAttack(100);
        cm1.setAttackMod(20);
        cm1.setAttackEnabled(false);
        cm1.rollAttack();

        assertEquals(expectedResult, cm1.getAttackRoll());
    }

    @Test
    public void rollDefense() {
        int expectedResult = 70;
        CharacterModel cm1 = new CharacterModel("Name", "Description", 0);
        cm1.setDefense(30);
        cm1.setDefenseMod(40);
        cm1.setDefenseEnabled(false);
        cm1.rollDefense();

        assertEquals(expectedResult, cm1.getDefenseRoll());
    }

    @Test
    public void calculateDamage() {
        int expectedResult = 100;
        CharacterModel cm1 = new CharacterModel("Name", "Description", 0);
        cm1.setAttack(100);
        cm1.setAttackMod(20);
        cm1.setAttackEnabled(false);
        cm1.setDefense(30);
        cm1.setDefenseMod(40);
        cm1.setDefenseEnabled(false);
        cm1.setWeaponDamage(200);

        cm1.rollAttack();
        cm1.rollDefense();

        assertEquals(expectedResult, cm1.calculateDamage(cm1.getDefenseRoll()));
    }

    @Test
    public void testEquals_Symmetric() {
        CharacterModel cm1 = new CharacterModel("Name", "Description", 0);
        cm1.setId(1);
        CharacterModel cm2 = new CharacterModel("Name", "Description", 1);
        cm2.setId(2);
        CharacterModel cm3 = new CharacterModel("Name2", "Description2", 1);
        cm3.setId(cm1.getId());

        assertNotEquals(cm1, cm2);
        assertNotEquals(cm2, cm1);
        assertEquals(cm1, cm3);
        assertEquals(cm3, cm1);

        assertNotEquals(cm1.hashCode(), cm2.hashCode());
        assertEquals(cm1.hashCode(), cm3.hashCode());
    }

    @Test
    public void contentsTheSame() {
        CharacterModel cm1 = new CharacterModel("Name", "Description", 0);
        cm1.setId(1);
        cm1.setAttack(10);
        cm1.setAttackMod(20);
        cm1.setAttackEnabled(true);
        cm1.setDefense(30);
        cm1.setDefenseMod(40);
        cm1.setDefenseEnabled(true);
        cm1.setDisplayPosition(50);
        cm1.setIniRoll(60);
        cm1.setInitiative(70);
        cm1.setInitiativeMod(80);
        cm1.setLastHit(90);
        cm1.setLife(100);
        cm1.setWeaponDamage(110);

        CharacterModel cm2 = new CharacterModel("Name", "Description", 1);
        cm2.setId(1);
        cm2.setAttack(10);
        cm2.setAttackMod(20);
        cm2.setAttackEnabled(true);
        cm2.setDefense(30);
        cm2.setDefenseMod(40);
        cm2.setDefenseEnabled(true);
        cm2.setDisplayPosition(50);
        cm2.setIniRoll(60);
        cm2.setInitiative(70);
        cm2.setInitiativeMod(80);
        cm2.setLastHit(90);
        cm2.setLife(100);
        cm2.setWeaponDamage(110);

        CharacterModel cm3 = new CharacterModel("Name2", "Description2", 1);

        assertTrue(cm1.contentsTheSame(cm2));
        assertFalse(cm1.contentsTheSame(cm3));
    }
}