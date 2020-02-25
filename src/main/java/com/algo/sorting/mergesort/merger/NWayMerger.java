package com.algo.sorting.mergesort.merger;

public abstract class NWayMerger<T extends Way<E>, E> {
	
	protected Way<E> ways[];
	protected int sortingKeyOfWay[];
	protected int N;
	
	protected abstract void adjustHeap();
	
	public NWayMerger(Way<E> ... Ways)
	{
		assert(ways.length > 0);
		this.ways = Ways;
		this.N = Ways.length;
		sortingKeyOfWay = new int[N];
		int currentSlotToputSortKey = 0;
		for (int i = 0; i < N; i++)
		{
			Integer sortKey = ways[i].peekSortKey();
			if (sortKey != null)
			{
				sortingKeyOfWay[currentSlotToputSortKey] = sortKey;
				this.ways[currentSlotToputSortKey] = this.ways[i];
				currentSlotToputSortKey++;
			}
		}
		N = currentSlotToputSortKey;
		adjustHeap();
	}
	
	private void abandonWay0()
	{
		ways[0] = ways[N - 1];
		sortingKeyOfWay[0] = sortingKeyOfWay[N - 1];
		N--;
		adjustHeap();
	}
	
	public E get()
	{
		E value = ways[0].getValue();
		//We have depleted this way, abandon it
		if (value == null)
		{
			if (N > 1)//There are more ways to deal with
			{
				abandonWay0();
			}
			else//All ways have been depleted
			{
				return null;
			}
		}
		sortingKeyOfWay[0] = ways[0].peekSortKey();
		adjustHeap();
		return value;
	}
	
	public E peek()
	{
		E value = ways[0].peekValue();
		if (value == null)
		{
			if (N > 1)//There are more ways to deal with
			{
				abandonWay0();
			}
			else//All ways have been depleted
			{
				return null;
			}
		}
		sortingKeyOfWay[0] = ways[0].peekSortKey();
		adjustHeap();
		return value;
	}
	
	public int peekSortingKey()
	{
		peek();
		return sortingKeyOfWay[0];
	}
}
