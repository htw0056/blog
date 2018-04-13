# 插入排序

```java
package com.htw.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author htw
 * @date 2018/2/11
 */
public class InsertSort {
    /**
     * 插入排序,算法导论版
     *
     * @param list
     */
    public static void insertSortV1(int[] list) {
        for (int i = 1; i < list.length; i++) {
            int currentElement = list[i];
            int j;
            for (j = i - 1; j >= 0 && list[j] > currentElement; j--) {
                list[j + 1] = list[j];
            }
            list[j + 1] = currentElement;
        }
    }

    /**
     * 使用二分查找插入点
     * 实际移动元素次数不变,因此效率和v1一样
     *
     * @param list
     */
    public static void insertSortV2(int[] list) {
        for (int i = 1; i < list.length; i++) {
            int key = list[i];
            int left = 0, right = i, mid;
            while (left < right) {
                mid = (left + right) / 2;
                if (list[mid] <= key) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            // left为list中大于key的第一个下标
            for (int j = i - 1; j >= left; j--) {
                list[j + 1] = list[j];
            }
            list[left] = key;
        }
    }


    public static void main(String[] args) {
        int a[] = {1, 2, 4, 9, 7, 6, 8, 65, 3, 123, 3};
        int b[] = {1, 2, 4, 1, 7, 3, 8, 65, 3, 123, 3};
        insertSortV1(a);
        System.out.println(Arrays.toString(a));
        insertSortV2(b);
        System.out.println(Arrays.toString(b));
    }
}
```
