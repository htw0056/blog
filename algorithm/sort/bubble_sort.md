# 冒泡排序

```java
package com.htw.algorithm.sort;

import java.util.Arrays;

/**
 * @author htw
 * @date 2018/2/11
 */
public class BubbleSort {
    public static void bubbleSortV1(int[] list) {
        // 比较len-1次即可
        for (int i = 1; i < list.length; i++) {
            for (int j = 0; j < list.length - i; j++) {
                if (list[j] > list[j + 1]) {
                    int temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
    }

    // 优化
    public static void bubbleSortV2(int[] list) {
        boolean needsort = true;
        // 比较len-1次即可
        for (int i = 1; i < list.length && needsort; i++) {
            needsort = false;
            for (int j = 0; j < list.length - i; j++) {
                if (list[j] > list[j + 1]) {
                    int temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                    needsort = true;
                }
            }
        }

    }

    public static void main(String[] args) {
        int a[] = {1, 2, 4, 9, 7, 6, 8, 65, 3, 123, 3};
        int b[] = {1, 2, 4, 1, 7, 3, 8, 65, 3, 123, 3};
        bubbleSortV1(a);
        System.out.println(Arrays.toString(a));
        bubbleSortV2(b);
        System.out.println(Arrays.toString(b));
    }
}

```