# 快速排序

```java
package com.htw.algorithm.sort;

import java.util.Arrays;

/**
 *
 * @author htw
 * @date 2018/2/11
 */
public class QuickSort {


    public static void quickSort(int[] list, int left, int right) {
        if (left < right) {
            int q = partition(list, left, right);
            quickSort(list, left, q - 1);
            quickSort(list, q + 1, right);
        }

    }

    public static int partition(int[] list, int left, int right) {
        int x = list[right];
        int i = left - 1;
        int temp;
        for (int j = left; j < right; j++) {
            if (list[j] <= x) {
                i++;
                temp = list[j];
                list[j] = list[i];
                list[i] = temp;
            }
        }
        i++;
        temp = list[i];
        list[i] = list[right];
        list[right] = temp;
        return i;

    }
  
  	// 简化版
     public static int partition(int[] list, int left, int right) {
        int x = list[right];
        int i = left - 1;
        int temp;
        for (int j = left; j <= right; j++) {  // < 改为 <=,将最后一次交换在循环内处理 
            if (list[j] <= x) {   // 必须是包含=情况，否则错误
                i++;
                temp = list[j];
                list[j] = list[i];
                list[i] = temp;
            }
        }
//        i++;
//        temp = list[i];
//        list[i] = list[right];
//        list[right] = temp;
        return i;

    }

    public static void main(String[] args) {
        int[] array = {5, 3, 4, 1, 4, 32, 32, 5, 6, 2};
        quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }

}

```