# 选择排序

```java
package com.htw.algorithm.sort;

import java.util.Arrays;

/**
 * @author htw
 * @date 2018/2/11
 */
public class SelectSort {
    public static void selectSort(int[] list) {
        for (int i = 0; i < list.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < list.length; j++) {
                if (list[j] < list[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = list[i];
                list[i] = list[minIndex];
                list[minIndex] = temp;
            }
        }
    }

    public static void main(String[] args) {
        int a[] = {1, 2, 4, 1, 7, 3, 8, 65, 3, 123, 3};
        selectSort(a);
        System.out.println(Arrays.toString(a));
    }
}
```