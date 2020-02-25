package com.algo.sorting;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileRanker {
	private String fileNamePrefix;
	private int range;
	public FileRanker(String fileNamePrefix, int range)
	{
		this.fileNamePrefix = fileNamePrefix;
		this.range = range;
	}
	
	public void doRank(int k) throws IOException
	{
		TopKSorter<String> topK = new TopKSorter<String>(k);
		
		for (int i = 0; i < range; i++)
		{
			BufferedReader curFilebf  = new BufferedReader(new FileReader(fileNamePrefix + "." + i));
			String curLine = curFilebf.readLine();
			
			while (curLine != null)
			{
				//curLine has a format of <ip>:<count>
				String[] ipAndCount = curLine.split(":");
				String ip = ipAndCount[0];
				int count = Integer.valueOf(ipAndCount[1]);
				topK.accept(ip, count);
				curLine = curFilebf.readLine();
			}
			curFilebf.close();
		}
		topK.printTopK();
	}
}
