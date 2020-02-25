package com.algo.sorting.mergesort.merger;

import com.algo.sorting.Heapsort;

public class NWayMergerAsc<T extends Way<E>, E> extends NWayMerger<T, E> {

	protected void adjustHeap()
	{
		for (int i = N / 2 - 1; i >= 0; i--)
		{
			Heapsort.minheapifier(sortingKeyOfWay, i, N - 1, ways);
		}
	}
	
	public NWayMergerAsc(Way<E> ... Ways)
	{
		super(Ways);
	}
}
