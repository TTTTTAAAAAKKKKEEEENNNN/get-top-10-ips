package com.algo.sorting.mergesort.merger;

public abstract class Way<T> {
	private Integer sortKey = null;
	protected boolean depleted = false;
	public abstract T peekValue();
	public abstract T getValue();
	
	public boolean isDepleted()
	{
		return depleted;
	}
	
	public void setDepleted(boolean depleted)
	{
		this.depleted = depleted;
	}
	/**
	 * peekSortKey retrieve the sort key of the element in front of this way(queue)
	 * 
	 * @return the sortkey (used as the basis of sort)<br>
	 * null if the way is empty<br>
	 * will call peekValue() if no value is cached in this way<br>
	 */
	public Integer peekSortKey()
	{
		if (isDepleted())
		{
			return null;
		}

		if (sortKey == null)
		{
			peekValue();
		}
		return sortKey;
	}
	public void setSortKey(int key)
	{
		if (isDepleted())
		{
			return;
		}
		this.sortKey = key;
	}
}
