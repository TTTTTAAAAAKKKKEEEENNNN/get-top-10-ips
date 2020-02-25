package com.algo.counting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SortedIpCounter extends IpCounter {
	
	private int ordering;
	private Comparator<Entry<String, Integer>> comparator;
	public static final int ORDER_ASC = 0;
	public static final int ORDER_DESC = 1;
	private static class AscendingSorter implements Comparator<Entry<String, Integer>>
	{
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
			return o1.getValue() - o2.getValue();
		}
	}
	
	private static class DescendingSorter implements Comparator<Entry<String, Integer>>
	{
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
			return o2.getValue() - o1.getValue();
		}
	}

	public SortedIpCounter(String ipFileName, String statFileName, int ordering) {
		super(ipFileName, statFileName);
		assert(ordering == ORDER_ASC || ordering == ORDER_DESC);
		this.ordering = ordering;
		this.comparator = ordering == ORDER_ASC ? new AscendingSorter() : new DescendingSorter();
	}

	@Override
	protected void doWrite() throws IOException {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(ipCount.entrySet());
		Collections.sort(list, comparator);

		FileOutputStream fos = new FileOutputStream(statFile);
		for (Entry<String, Integer> curItem : list)
		{
			String ip = curItem.getKey();
			Integer count = curItem.getValue();
			String statInfo = ip + ":" + count + "\n";
			fos.write(statInfo.getBytes());
		}

		fos.close();
	}
}
