package com.algo.counting;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadIPCounter {
	
	private String fileNamePrefix;
	private int range;
	
	/**
	 * MultiThreadIPCounter count ips within the file provided by fileNamePrefix in a parallel way
	 * @param fileNamePrefix the prefix of the files to be counted, e.g. /path/to/count/fileshard
	 * @param range this class will automatically generate pattern according to range,
	 * e.g. range = 5, this class will count from /path/to/count/fileshard.0 to /path/to/count/fileshard.4
	 */
	public MultiThreadIPCounter(String fileNamePrefix, int range)
	{
		this.fileNamePrefix = fileNamePrefix;
		this.range = range;
	}
	
	public void doCount() throws InterruptedException
	{
    	ExecutorService tpool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	
    	final CountDownLatch finishedTasks = new CountDownLatch(range);
    	Thread tlist[] = new Thread[range];
    	
    	for ( int i = 0; i < range; i++)
    	{
    		final int allocateI = i;
    		tlist[i] = new Thread(new Runnable(){

				public void run() {
			    	IpCounter theIpCounter = new SimpleIpCounter(fileNamePrefix + "." + allocateI, fileNamePrefix + ".stat." + allocateI);
			    	try {
						theIpCounter.countAndWrite();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	finishedTasks.countDown();
			    	System.out.println("Thread " + allocateI + " has finished.");
				}
    			
    		});
    		tpool.submit(tlist[i]);
    	}

    	tpool.shutdown();
    	finishedTasks.await();
	}
}
