# 插入排序

```java
package com.htw.algorithm.sort;

public class InsertSort {

	// 算法导论
	// version1.0
	public static void insertsort_v1(int[] array) {
		int len = array.length;
		for (int i = 1; i < len; i++) {
			int key = array[i];
			int j;
			for (j = i - 1; j >= 0 && array[j] > key; j--) {
				array[j + 1] = array[j];
			}
			array[j + 1] = key;
		}
	}

	// 二分
	public static void insertsort_v2(int[] array) {
		int len = array.length;
		for (int i = 1; i < len; i++) {
			int key = array[i];
			int left = 0, right = i, mid;
			while (left < right) {
				mid = (left + right) / 2;
				if (array[mid] <= key)
					left = mid + 1;
				else
					right = mid;
			}
			// left为array值大于key的第一个下标
			for (int j = i - 1; j >= left; j--) {
				array[j + 1] = array[j];
			}
			array[left] = key;
		}
	}

	public static void main(String[] args) {
		int a[] = { 1, 2, 4, 9, 7, 6, 8, 65, 3, 123, 3 };
		int b[] = { 1, 2, 4, 1, 7, 3, 8, 65, 3, 123, 3 };
		insertsort_v1(a);
		for (int t : a) {
			System.out.print(t + " ");
		}
		System.out.println();
		insertsort_v2(b);
		for (int t : b) {
			System.out.print(t + " ");
		}
	}

}
```