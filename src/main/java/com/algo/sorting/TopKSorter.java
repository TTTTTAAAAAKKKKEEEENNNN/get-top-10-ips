package com.algo.sorting;

import java.lang.reflect.Array;

public class TopKSorter<T> {
	
	private int values[];
	private T keys[];
	private int K;
	
	public TopKSorter(int k)
	{
		assert(k > 0);
		this.values = new int[k + 1];
		for (int i = 0; i < k + 1; i++)
		{
			this.values[i] = 0;
		}
		this.keys = (T[]) new Object[k + 1];
		this.K = k;
	}
	
	public void accept(T sortKey, int sortValue)
	{
		keys[(Integer) this.K] = sortKey;
		values[(Integer) this.K] = sortValue;
		
		Heapsort.heapsortdesc(values, K + 1, keys);
	}
	
	public void printTopK()
	{
		for (int i = 0; i < K; i++)
		{
			System.out.println("Key:" + keys[i] + " value:" + values[i]);
		}
	}
}
