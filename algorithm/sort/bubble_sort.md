# 冒泡排序

```java
package com.htw.algorithm.sort;

public class BubbleSort {
	public static void bubblesort(int[] array) {
		int len = array.length;
		// 比较len-1次即可
		for (int i = 1; i < len; i++) {
			for (int j = 0; j < len - i; j++) {
				if (array[j] > array[j + 1]) {
					int temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
				}
			}
		}
	}

	// 优化
	public static void bubblesort_v2(int[] array) {
		int len = array.length;
		boolean needSort = true;
		// 比较len-1次即可
		for (int i = 1; i < len && needSort; i++) {
			needSort = false;
			for (int j = 0; j < len - i; j++) {
				if (array[j] > array[j + 1]) {
					int temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
					needSort = true;
				}
			}
		}

	}

	public static void main(String[] args) {
		int a[] = { 1, 2, 4, 1, 7, 3, 8, 65, 3, 123, 3 };
		int b[] = { 1, 2, 4, 1, 7, 3, 8, 65, 3, 123, 3 };
		bubblesort(a);
		for (int t : a) {
			System.out.print(t + " ");
		}
		System.out.println();
		bubblesort_v2(b);
		for (int t : b) {
			System.out.print(t + " ");
		}
	}

}

```