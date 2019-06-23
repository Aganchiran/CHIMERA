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

package com.aganchiran.chimera.chimeracore.dice;

import org.junit.Test;

public class AnimaDiceTest {

    @Test
    public void getRollOpen() {
        for (long i = 0; i < 10000000; i++) {
            int roll = AnimaDice.getRollOpen();
            assert roll >= 1;
        }
    }

    @Test
    public void getRoll() {
        for (int i = 0; i < 10000000; i++) {
            int roll = AnimaDice.getRoll();
            assert roll <= 100 && roll >= 1;
        }
    }
}