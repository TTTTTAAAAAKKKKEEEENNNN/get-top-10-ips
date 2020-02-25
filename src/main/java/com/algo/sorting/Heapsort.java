package com.algo.sorting;

public class Heapsort {
	
	/**
	 * maxheapifier(int arr[], int start, int end, T keyToAdjust[])
	 * A function adjusts specified set (arr) to a heap whose nodes are greater than both its left child and right child
	 * @param arr the array(tree) to heapify
	 * @param start range of start
	 * @param end range of end(inclusive)
	 * @param keyToAdjust will also be adjusted according to arr's order if not null
	 */
	public static <T> void maxheapifier(int arr[], int start, int end, T keyToAdjust[])
	{
		int currentNode = start;
		int child = start * 2 + 1;
		while (child < end)
		{
			if (child + 1 <= end && arr[child + 1] > arr[child])
			{
				child++;
			}
			
			if (arr[currentNode] < arr[child])
			{
				int tmp = arr[currentNode];
				arr[currentNode] = arr[child];
				arr[child] = tmp;

				if (keyToAdjust != null)//also adjust sort key
				{
					T keyTmp = keyToAdjust[currentNode];
					keyToAdjust[currentNode] = keyToAdjust[child];
					keyToAdjust[child] = keyTmp;
				}
				
				currentNode = child;
				child = currentNode * 2 + 1;
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * minheapifier(int arr[], int start, int end, T keyToAdjust[])
	 * A function adjusts specified set (arr) to a heap whose nodes are less than both its left child and right child
	 * @param arr the array(tree) to heapify
	 * @param start range of start
	 * @param end range of end(inclusive)
	 * @param keyToAdjust will also be adjusted according to arr's order if not null
	 */
	public static <T> void minheapifier(int arr[], int start, int end, T keyToAdjust[])
	{
		int currentNode = start;
		int child = start * 2 + 1;
		while (child < end)
		{
			if (child + 1 <= end && arr[child + 1] < arr[child])
			{
				child++;
			}
			
			if (arr[currentNode] > arr[child])
			{
				int tmp = arr[currentNode];
				arr[currentNode] = arr[child];
				arr[child] = tmp;

				if (keyToAdjust != null)//also adjust sort key
				{
					T keyTmp = keyToAdjust[currentNode];
					keyToAdjust[currentNode] = keyToAdjust[child];
					keyToAdjust[child] = keyTmp;
				}
				
				currentNode = child;
				child = currentNode * 2 + 1;
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * heapsort(int arr[], int start, int end, T keyToAdjust[])
	 * A function performs heap sort on arr with an ascending order
	 * @param arr the array(tree) to sort
	 * @param len length of the array
	 * @param keyToAdjust will also be adjusted according to arr's order if not null
	 */
	public static <T> void heapsortasc(int arr[], int len, T keyToAdjust[])
	{
		for (int i = len / 2 - 1; i >= 0; i--)
		{
			maxheapifier(arr, i, len - 1, keyToAdjust);
		}
		
		for (int i = len - 1; i > 0; i--)
		{
			int tmp = arr[0];
			arr[0] = arr[i];
			arr[i] = tmp;
			Object tmpkey = keyToAdjust[0];
			keyToAdjust[0] = keyToAdjust[i];
			keyToAdjust[i] = (T) tmpkey;
			maxheapifier(arr, 0, i - 1, keyToAdjust);
		}
	}
	
	/**
	 * heapsortdesc(int arr[], int start, int end, T keyToAdjust[])
	 * A function performs heap sort on arr with a descending order
	 * @param arr the array(tree) to sort
	 * @param len length of the array
	 * @param keyToAdjust will also be adjusted according to arr's order if not null
	 */
	public static <T> void heapsortdesc(int arr[], int len, T keyToAdjust[])
	{
		for (int i = len / 2 - 1; i >= 0; i--)
		{
			minheapifier(arr, i, len - 1, keyToAdjust);
		}
		
		for (int i = len - 1; i > 0; i--)
		{
			int tmp = arr[0];
			arr[0] = arr[i];
			arr[i] = tmp;
			Object tmpkey = keyToAdjust[0];
			keyToAdjust[0] = keyToAdjust[i];
			keyToAdjust[i] = (T) tmpkey;
			minheapifier(arr, 0, i - 1, keyToAdjust);
		}
	}
}
