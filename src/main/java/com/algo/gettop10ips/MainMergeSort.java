package com.algo.gettop10ips;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.algo.sharding.FileSharder;
import com.algo.sorting.FileRanker;
import com.algo.sorting.mergesort.FileWay;
import com.algo.sorting.mergesort.merger.NWayMergerDesc;
import com.algo.counting.IpCounter;
import com.algo.counting.MultiThreadIPCounter;
import com.algo.counting.MultiThreadIPCounterWithOrdering;
import com.algo.utils.LogFileCreator;

public class MainMergeSort {
    public static void main( String[] args ) throws IOException, InterruptedException
    {
    	LogFileCreator.generateRandomIpLogFile( "e:\\access.log", 2000000);
    	FileSharder fs = new FileSharder("e:\\access.log", 100, "e:\\logshard");
    	fs.doShard();
    	MultiThreadIPCounterWithOrdering smpCounter = new MultiThreadIPCounterWithOrdering("e:\\logshard", 100);
    	smpCounter.doCount();
    	FileWay fileWays[] = new FileWay[100];
    	for (int i = 0; i < 100; i++)
    	{
    		fileWays[i] = new FileWay("e:\\logshard.stat." + i);
    	}
    	
    	NWayMergerDesc<FileWay, String> merger = new NWayMergerDesc<FileWay, String>(fileWays);

    	Integer sortKey = merger.peekSortingKey();
    	String value = merger.get();
    	for (int i = 0; i < 10; i++)
    	{
    		System.out.println(value + "	" + sortKey);
        	sortKey = merger.peekSortingKey();
    		value = merger.get();
    	}
    }
}
