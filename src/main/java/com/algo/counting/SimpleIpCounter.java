package com.algo.counting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import java.util.Set;

public class SimpleIpCounter extends IpCounter {
	
	public SimpleIpCounter(String ipFileName, String statFileName)
	{
		super(ipFileName, statFileName);
	}

	@Override
	protected void doWrite() throws IOException {
		Set<Entry<String, Integer>> countSet = ipCount.entrySet();
		FileOutputStream fos = new FileOutputStream(statFile);
		for (Entry<String, Integer> curItem : countSet)
		{
			String ip = curItem.getKey();
			Integer count = curItem.getValue();
			String statInfo = ip + ":" + count + "\n";
			fos.write(statInfo.getBytes());
		}

		fos.close();
	}
}
