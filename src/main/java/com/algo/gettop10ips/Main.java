package com.algo.gettop10ips;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.algo.sharding.FileSharder;
import com.algo.sorting.FileRanker;
import com.algo.counting.IpCounter;
import com.algo.counting.MultiThreadIPCounter;
import com.algo.utils.LogFileCreator;

public class Main {
    public static void main( String[] args ) throws IOException, InterruptedException
    {
    	//LogFileCreator.generateRandomIpLogFile( "e:\\access.log", 10000000);
    	FileSharder fs = new FileSharder("e:\\access.log", 100, "e:\\logshard");
    	//fs.doShard();
    	MultiThreadIPCounter smpCounter = new MultiThreadIPCounter("e:\\logshard", 100);
    	//smpCounter.doCount();
    	FileRanker theRanker = new FileRanker("e:\\logshard.stat", 100);
    	theRanker.doRank(100);
    }
}
