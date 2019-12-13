package com.algo.sharding;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class IPShardingCounter {

	//Number of sharding files
	private final Integer SHARD_FILE_NUM;
	
	//length of access count of each IP in byte
	private final Integer COUNTER_BYTES;
	
	//IP in each file
	private final Integer IPsPerFile;


	public IPShardingCounter()
	{
		SHARD_FILE_NUM = 1024;
		COUNTER_BYTES = 4;//Only support 4 currently
		IPsPerFile = (int)Math.ceil(Math.pow(2, 32) / SHARD_FILE_NUM);
	}
	
	private Map<Integer, RandomAccessFile> IPCountfiles = new HashMap<Integer, RandomAccessFile>(16);
	
	//fileNo = (int) (valueOfIP / IPsPerFile);
	//offset = (int) (valueOfIP % IPsPerFile);

	private RandomAccessFile createInitialFile(int fileNo) throws IOException
	{
		RandomAccessFile raf = new RandomAccessFile("sharding/" + fileNo + ".cnt", "rw");
		//The access times for THE_IP is kept on (THE_IP / SHARD_FILE_NUM) th file
		//It is kept at offset (THE_IP % SHARD_FILE_NUM) * COUNTER_BYTES
		//Each file will keep ceil(2^32 / SHARD_FILE_NUM) ips at most
		//TODO:on the tail of each file, a bitmap will be kept for ips with access times > 0
		byte b[] = new byte[IPsPerFile * COUNTER_BYTES];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = 0;
		}

		raf.write(b);
		IPCountfiles.put(fileNo, raf);
		return raf;
	}

	public static String number2Ip(Long numberIp)
	{
		return String.valueOf((numberIp & 0xff000000) >> 24) + "." +
		String.valueOf((numberIp & 0xff0000) >> 16) + "." +
		String.valueOf((numberIp & 0xff00) >> 8) + "." +
		String.valueOf(numberIp & 0xff);
	}

	public static long ip2Number(String ip)
	{
		String[] splittedIp = ip.split("\\.");
		long valueOfIP =
				(Long.valueOf(splittedIp[0]) << 24) +
				(Long.valueOf(splittedIp[1]) << 16) +
				(Long.valueOf(splittedIp[2]) << 8) +
				(Long.valueOf(splittedIp[3]) << 0);
		
		return valueOfIP;
	}

	private long ip2FileOffset(String ip)
	{
		long valueOfIP = ip2Number(ip);
		int posOfIp = (int) (valueOfIP % IPsPerFile);
		long fileOffset = posOfIp * COUNTER_BYTES;
		return fileOffset;
	}
	
	private int ip2FileNo(String ip)
	{
		long valueOfIP = ip2Number(ip);
		int fileNo = (int) (valueOfIP / IPsPerFile);
		return fileNo;
	}
	
	public long inc(String ip) throws IOException
	{
		int strFileNo = ip2FileNo(ip);
		if (!IPCountfiles.containsKey(strFileNo))
		{
			RandomAccessFile initFile = createInitialFile(strFileNo);
			IPCountfiles.put(strFileNo, initFile);
		}

		long fileOffset = ip2FileOffset(ip);
		RandomAccessFile ipCountFile = IPCountfiles.get(strFileNo);
		ipCountFile.seek(fileOffset);
		Integer currentAccessTimes = ipCountFile.readInt();
		currentAccessTimes++;
		ipCountFile.seek(fileOffset);
		ipCountFile.writeInt(currentAccessTimes);

		return currentAccessTimes;
	}
	
	public Map<String, Integer> getTopnAccessdIPs(Integer fileNo, Integer numberOfElements) throws IOException
	{
		Map<String, Integer> topN = new TreeMap<String, Integer>();
		Integer strFileNo = fileNo;
		if (!IPCountfiles.containsKey(strFileNo))
		{
			return topN;
		}
		RandomAccessFile ipCountFile = IPCountfiles.get(strFileNo);
		Map<Long, Integer> allIPMap = new TreeMap<Long, Integer>();
		
		ipCountFile.seek(0);
		for (int i = 0; i < IPsPerFile; i++)
		{
			Integer accessTimes = ipCountFile.readInt();
			if (accessTimes == 0)
			{
				continue;
			}
			Long curIp = (long) (IPsPerFile * fileNo + i);
			allIPMap.put(curIp, accessTimes);
		}
		List<Map.Entry<Long, Integer>> entryList = new ArrayList<Map.Entry<Long, Integer>>(allIPMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Entry<Long, Integer>> it = entryList.iterator();
		for (int i = 0; i < numberOfElements; i++)
		{
			if (it.hasNext())
			{
				Entry<Long, Integer> curRec = it.next();
				topN.put(number2Ip(curRec.getKey()), curRec.getValue());
			}
		}
		
		return topN;
	}
}
