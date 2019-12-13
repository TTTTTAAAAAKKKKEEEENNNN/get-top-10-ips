package com.algo.sharding;

import java.io.FileNotFoundException;
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

public class IPSharding {

	static final Integer SHARD_FILE_NUM = 1024;
	static final Integer COUNTER_BYTES = 4;
	static final Integer IPsPerFile = (int)Math.ceil(Math.pow(2, 32) / SHARD_FILE_NUM);
	
	private String ip;
	private String[] splittedIp;
	private int fileNo;
	private int offset;
	private long valueOfIP;
	
	private Map<String, RandomAccessFile> IPCountfiles = new HashMap<String, RandomAccessFile>();
	
	public void setIP(String _ip)
	{
		ip = _ip;
		splittedIp = ip.split("\\.");
		valueOfIP =
				(Long.valueOf(splittedIp[0]) << 24) +
				(Long.valueOf(splittedIp[1]) << 16) +
				(Long.valueOf(splittedIp[2]) << 8) +
				(Long.valueOf(splittedIp[3]) << 0);
		fileNo = (int) (valueOfIP / IPsPerFile);
		offset = (int) (valueOfIP % IPsPerFile);
	}

	RandomAccessFile createInitialFile() throws IOException
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
		return raf;
	}
	
	long getFileOffset()
	{
		return offset * COUNTER_BYTES;
	}
	
	public long inc() throws IOException
	{
		String strFileNo = String.valueOf(fileNo);
		if (!IPCountfiles.containsKey(strFileNo))
		{
			RandomAccessFile initFile = createInitialFile();
			IPCountfiles.put(String.valueOf(strFileNo), initFile);
		}

		RandomAccessFile ipCountFile = IPCountfiles.get(strFileNo);
		ipCountFile.seek(getFileOffset());
		Integer currentAccessTimes = ipCountFile.readInt();
		currentAccessTimes++;
		ipCountFile.seek(getFileOffset());
		ipCountFile.writeInt(currentAccessTimes);

		return currentAccessTimes;
	}
	
	public String number2Ip(Long numberIp)
	{
		return String.valueOf((numberIp & 0xff000000) >> 24) + "." +
		String.valueOf((numberIp & 0xff0000) >> 16) + "." +
		String.valueOf((numberIp & 0xff00) >> 8) + "." +
		String.valueOf(numberIp & 0xff);
	}
	
	public Map<String, Integer> getTopnAccessdIPs(Integer fileNo, Integer numberOfElements) throws IOException
	{
		Map<String, Integer> topN = new TreeMap<String, Integer>();
		String strFileNo = String.valueOf(fileNo);
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
