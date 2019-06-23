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

package com.aganchiran.chimera.chimerafront.utils;

import com.aganchiran.chimera.chimeracore.ItemModel;

import java.util.List;

public class CompareUtil {

    public static boolean areItemsTheSame(List list1, List list2) {
        boolean result = list1.size() == list2.size();
        for (int i = 0; result && i < list1.size(); i++) {
            result = list1.get(i).equals(list2.get(i));
        }
        return result;
    }

    public static <T extends ItemModel> boolean areItemsContentTheSame(List<T> list1, List<T> list2) {
        boolean result = list1.size() == list2.size();
        for (int i = 0; result && i < list1.size(); i++) {
            result = list1.get(i).contentsTheSame(list2.get(i));
        }
        return result;
    }
}
