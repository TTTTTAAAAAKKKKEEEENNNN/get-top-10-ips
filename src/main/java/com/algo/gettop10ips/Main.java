package com.algo.gettop10ips;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.algo.sharding.IPShardingCounter;

public class Main {
    public static void main( String[] args ) throws IOException
    {
    	IPTokenizer []ipc = new IPTokenizer[4];
    	ExecutorService e = Executors.newFixedThreadPool(ipc.length);
    	for (int i = 0; i < ipc.length; i++)
    	{
    		ipc[i] = new IPTokenizer("access.log", ipc.length, i);
    	}
    	
    	for (int i = 0; i < ipc.length; i++)
    	{
    		final IPTokenizer x = ipc[i];
    		e.submit(() -> { try {
    			String ss;
				while ( (ss = x.getNextIp()) != null) {
					//System.out.println(ss);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}; });
    	}
    	
    	IPShardingCounter ipSharding = new IPShardingCounter();
    	String curIp;

    	Map<String, Integer> res = ipSharding.getTopnAccessdIPs(40, 10);
    	System.out.println(res);
    }
}
