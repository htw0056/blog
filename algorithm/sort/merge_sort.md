# 归并排序

```java
package com.htw.algorithm.sort;

import java.util.Arrays;

/**
 * Created by htw on 2018/2/11.
 */
public class MergeSort {


    public static void mergeSort(int[] list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);

        }
    }

    public static void merge(int[] list, int left, int mid, int right) {
        // copy函数左闭右开,所以右边+1
        int[] arr1 = Arrays.copyOfRange(list, left, mid + 1);
        int[] arr2 = Arrays.copyOfRange(list, mid + 1, right + 1);
        int i = 0, j = 0;
        int len1 = arr1.length;
        int len2 = arr2.length;
        int k = left;
        while (i < len1 && j < len2) {
            if (arr1[i] < arr2[j]) {
                list[k++] = arr1[i++];
            } else {
                list[k++] = arr2[j++];
            }
        }
        while (i < len1) {
            list[k++] = arr1[i++];
        }
        while (j < len2) {
            list[k++] = arr2[j++];
        }
    }


    public static void main(String[] args) {
        int[] array = {5, 3, 4, 1, 4, 32, 32, 5, 6, 2};
        mergeSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }


}

```
