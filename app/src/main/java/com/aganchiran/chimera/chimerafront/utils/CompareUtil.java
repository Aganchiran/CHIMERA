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
