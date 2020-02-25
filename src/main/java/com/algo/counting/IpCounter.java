package com.algo.counting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.algo.utils.tokenizer.IPTokenizer;

import java.util.Set;

public abstract class IpCounter {

	protected String ipFileName;
	protected String statFileName;
	protected File ipFile;
	protected File statFile;
	protected Map<String, Integer> ipCount = new HashMap<String, Integer>();
	
	public IpCounter(String ipFileName, String statFileName)
	{
		if (ipFileName == null || ipFileName.equals(""))
		{
			throw new RuntimeException("ipFileName cannot be null");
		}
		if (statFileName == null || statFileName.equals(""))
		{
			throw new RuntimeException("statFileName cannot be null");
		}
		
		this.ipFileName = ipFileName;
		this.statFileName = statFileName;

		ipFile = new File(this.ipFileName);
		if (!ipFile.exists())
		{
			throw new RuntimeException(ipFileName + " doesn't exist");
		}
	}
	
	protected void doCount() throws IOException
	{
		statFile = new File(statFileName);
		if (statFile.exists())
		{
			statFile.delete();
		}
		
		IPTokenizer iptk = new IPTokenizer(ipFileName);
		iptk.OpenAccessLog();
		String currentIp = iptk.getNextIp();
		while (currentIp != null)
		{
			if (!ipCount.containsKey(currentIp))
			{
				ipCount.put(currentIp, 1);
			}
			else
			{
				Integer currentIpCount = ipCount.get(currentIp);
				currentIpCount++;
				ipCount.put(currentIp, currentIpCount);
			}
			currentIp = iptk.getNextIp();
		}
	}
	
	protected abstract void doWrite() throws IOException;
	
	public void countAndWrite() throws IOException
	{
		doCount();
		doWrite();
	}
}
