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

import java.util.Collections;
import java.util.List;

public abstract class Quicksort<T> {
    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    private int partition(T arr[], int low, int high) {
        T pivot = arr[high];
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than or
            // equal to pivot
            if (!(compare(arr[j], pivot) == 1)) {
                i++;

                // swap arr[i] and arr[j]
                T temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        T temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    private void sort(T arr[], int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }

    public void sort(T arr[]) {
        sort(arr, 0, arr.length - 1);
    }

    protected abstract int compare(T a, T b);


    public void sort(List<T> list, int left, int right) {
        int q;
        if (right > left) {
            q = partition(list, left, right);
            // after ‘partition’
            // list[left..q-1] ≤ list[q] ≤ list[q+1..right]
            sort(list, left, q - 1);
            sort(list, q + 1, right);
        }
    }

    int partition(List<T> list, int left, int right) {
        T P = list.get(left);
        int i = left;
        int j = right + 1;
        for (; ; ) { // infinite for-loop, break to exit
            while (compare(list.get(++i), P) < 0)
                if (i >= right)
                    break;
            // Now, list[i]≥P
            while (compare(list.get(--j), P) > 0)
                if (j <= left)
                    break;
            // Now, list[j]≤P
            if (i >= j)
                break; // break the for-loop
            else
                // swap(list[i],list[j]);
                Collections.swap(list, i, j);
        }
        if (j == left)
            return j;
        // swap (list[left],list[j]);
        Collections.swap(list, left, j);
        return j;
    }

    public void sort(List<T> list) {
        sort(list, 0, list.size() - 1);
    }
}
