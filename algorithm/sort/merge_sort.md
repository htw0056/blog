# 归并排序

```java
package com.htw.algorithm.sort;

import java.util.Arrays;

public class MergeSort {

	public static void Merge_Sort(int[] array, int left, int right) {
		if (left < right) {
			int mid = (left + right) / 2;
			Merge_Sort(array, left, mid);
			Merge_Sort(array, mid + 1, right);
			Merge(array, left, mid, right);

		}
	}

	public static void Merge(int[] array, int left, int mid, int right) {
		// copy函数左闭右开,所以右边+1
		int[] arr1 = Arrays.copyOfRange(array, left, mid + 1);
		int[] arr2 = Arrays.copyOfRange(array, mid + 1, right + 1);
		int i = 0, j = 0;
		int len1 = arr1.length;
		int len2 = arr2.length;
		int k = left;
		while (i < len1 || j < len2) {
			if (i >= len1) {
				array[k] = arr2[j++];
			} else if (j >= len2) {
				array[k] = arr1[i++];
			} else {
				if (arr1[i] < arr2[j]) {
					array[k] = arr1[i++];
				} else {
					array[k] = arr2[j++];
				}
			}
			k++;
		}
	}

	public static void main(String[] args) {
		int[] array = { 5, 3, 4, 1, 4, 32, 32, 5, 6, 2 };
		Merge_Sort(array, 0, array.length - 1);
		System.out.println(Arrays.toString(array));

	}

}

```