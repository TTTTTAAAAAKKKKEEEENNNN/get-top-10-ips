package com.algo.sharding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.algo.utils.DebugUtils;
import com.algo.utils.tokenizer.IPTokenizer;

public class FileSharder {
	private String srcFile;
	private String srcPath;
	private String shardFileName;
	private String targetFilePath;
	private int numberShards;
	private Map<Integer, FileOutputStream> shardFiles = null;
	
	public FileSharder(String srcFile, int numberShards, String targetFilePath)
	{
		assert(numberShards > 0);
		File toShardFile = new File(srcFile);
		this.srcFile = srcFile;
		this.numberShards = numberShards;
		srcPath = toShardFile.getParent();
		shardFileName = toShardFile.getName();
		this.targetFilePath = targetFilePath;
		this.shardFiles = new HashMap<Integer, FileOutputStream>(numberShards);
	}
	
	void removeOldShardingFiles()
	{
		for (int i = 0; i < numberShards; i++)
		{
			File curShardFileToDelete = null;
			if (targetFilePath != null)
			{
				curShardFileToDelete = new File(targetFilePath + "." + i);
			}
			else
			{
				curShardFileToDelete = new File(srcPath + "/" + shardFileName + "." + i);
			}
			if (curShardFileToDelete.isFile())
			{
				curShardFileToDelete.delete();
			}
		}
	}
	
	void createAllShardingFiles() throws FileNotFoundException
	{
		for (int i = 0; i < numberShards; i++)
		{
			FileOutputStream fos = null;
			if (!shardFiles.containsKey(i))
			{
				if (targetFilePath != null)
				{
					fos = new FileOutputStream(targetFilePath + "." + i);
				}
				else
				{
					fos = new FileOutputStream(srcPath + "/" + shardFileName + "." + i);
				}
				shardFiles.put(i, fos);
			}
		}
	}
	
	public void doShard() throws IOException
	{
		removeOldShardingFiles();
		createAllShardingFiles();
		IPTokenizer ipTokenizer = new IPTokenizer(srcFile);
		ipTokenizer.OpenAccessLog();
		
		String curIP = ipTokenizer.getNextIp();
		while (curIP != null)
		{
			Integer curShard = Math.abs(curIP.hashCode()) % numberShards;
			DebugUtils.print("curIp:" + curIP + " in shard:" + curShard);
			if (!shardFiles.containsKey(curShard))
			{
				FileOutputStream fos = null;
				if (targetFilePath != null)
				{
					fos = new FileOutputStream(targetFilePath + "." + curShard);
				}
				else
				{
					fos = new FileOutputStream(srcPath + "/" + shardFileName + "." + curShard);
				}
				shardFiles.put(curShard, fos);
			}
			FileOutputStream currentShardFile = shardFiles.get(curShard);
			String toWrite = curIP + "\n";
			currentShardFile.write(toWrite.getBytes());
			curIP = ipTokenizer.getNextIp();
		}
		
		for (Entry<Integer, FileOutputStream> fileToClose : shardFiles.entrySet())
		{
			fileToClose.getValue().flush();
			fileToClose.getValue().close();
		}
	}
}
