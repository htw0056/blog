# 选择排序

```java
package com.htw.algorithm.sort;

public class SelectionSort {

	public static void selectsort(int[] array) {
		int len=array.length;
		for (int i = 0; i < len; i++) {
			int minIndex = i;
			for (int j = i + 1; j < len; j++) {
				if (array[j] < array[minIndex]) {
					minIndex = j;
				}
			}
			if (minIndex != i) {
				int temp = array[i];
				array[i] = array[minIndex];
				array[minIndex] = temp;
			}
		}
	}

	public static void main(String[] args) {
		int a[]={1,2,4,1,7,3,8,65,3,123,3};
		selectsort(a);
		for(int t:a){
			System.out.print(t+" ");
		}
	}

}
```