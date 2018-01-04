# 快速排序

```java
package com.htw.algorithm.sort;

import java.util.Arrays;

public class QuickSort
{

	public static void quick_sort(int[] array, int left, int right)
	{
		if (left < right)
		{
			int q = partition(array, left, right);
			quick_sort(array, left, q - 1);
			quick_sort(array, q + 1, right);
		}

	}

	public static int partition(int[] array, int left, int right)
	{
		int x = array[right];
		int i = left - 1;
		int temp;
		for (int j = left; j < right; j++)
		{
			if (array[j] <= x)
			{
				i++;
				temp = array[j];
				array[j] = array[i];
				array[i] = temp;
			}
		}
		i++;
		temp = array[i];
		array[i] = array[right];
		array[right] = temp;
		return i;

	}

	public static void main(String[] args)
	{
		int[] array = { 5, 3, 4, 1, 4, 32, 32, 5, 6, 2 };
		quick_sort(array, 0, array.length - 1);
		System.out.println(Arrays.toString(array));
	}

}
```