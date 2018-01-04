# 堆排序

```java
package com.htw.algorithm.sort;

import java.util.Arrays;

public class HeapSort
{

	public static int parent(int i)
	{
		return i / 2;
	}

	public static int left(int i)
	{
		return i * 2;
	}

	public static int right(int i)
	{
		return i * 2 + 1;
	}

	// 维护堆, 自上而下,递归
	// array[0]用来保存堆的大小
	public static void MAX_HEAPIFY(int[] array, int i)
	{
		int left = left(i);
		int right = right(i);
		int largest;
		if (left <= array[0] && array[left] > array[i])
			largest = left;
		else
			largest = i;
		if (right <= array[0] && array[right] > array[largest])
			largest = right;
		if (largest != i)
		{
			int temp = array[largest];
			array[largest] = array[i];
			array[i] = temp;
			MAX_HEAPIFY(array, largest);
		}

	}

	// 自底向上构建最大堆
	public static void BUILD_MAX_HEAP(int[] array)
	{
		for (int i = array[0] / 2; i >= 1; i--)
		{
			MAX_HEAPIFY(array, i);
		}
	}

	public static void HEAP_SORT(int[] array)
	{
		BUILD_MAX_HEAP(array);
		for (int i = array[0]; i >= 2; i--)
		{
			int temp = array[1];
			array[1] = array[i];
			array[i] = temp;
			array[0]--;
			MAX_HEAPIFY(array, 1);
		}
	}

	public static void main(String[] args)
	{
		// array[0]=array.length
		int[] array = { 9, 3, 5, 4, 3, 3, 2, 4, 7, 33 };
		HEAP_SORT(array);
		System.out.println(Arrays.toString(array));
	}

}
```