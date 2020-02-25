package com.algo.sharding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatMerger {

	private String srcFiles[];
	private String targetFile;
	
	public StatMerger(String srcFiles[], String targetFile)
	{
		this.srcFiles = srcFiles;
		this.targetFile = targetFile;
	}
	
	private Map<String, Integer> doMerge(String fileName1, String fileName2) throws IOException
	{
		Map<String, Integer> statInfo = new HashMap<String, Integer>();
		BufferedReader fileReader1  = new BufferedReader(new FileReader(fileName1));
		BufferedReader fileReader2  = new BufferedReader(new FileReader(fileName2));
		String curLine = fileReader1.readLine();
		
		while (curLine != null)
		{
			String[] ipAndCount = curLine.split(":");
			String ip = ipAndCount[0];
			Integer count = Integer.valueOf(ipAndCount[1]);
			//Already in the list, need to sum up
			if (statInfo.containsKey(ip))
			{
				Integer curCount = statInfo.get(ip);
				curCount += count;
				statInfo.put(ip, curCount);
			}
			else
			//not in the list, adding
			{
				statInfo.put(ip, count);
			}
		}

		fileReader1.close();
		fileReader2.close();

		return statInfo;
	}
}
