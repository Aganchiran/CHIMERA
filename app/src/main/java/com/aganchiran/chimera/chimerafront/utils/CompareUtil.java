package com.aganchiran.chimera.chimerafront.utils;

import java.util.List;

public class CompareUtil {

    public static boolean areItemsTheSame(List list1, List list2) {
        boolean result = list1.size() == list2.size();
        for (int i = 0; result && i < list1.size(); i++) {
            result = list1.get(i).equals(list2.get(i));
        }
        return result;
    }
}
