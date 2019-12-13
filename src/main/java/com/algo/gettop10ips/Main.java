package com.algo.gettop10ips;

import java.io.IOException;
import java.util.Map;

import com.algo.sharding.IPShardingCounter;

public class Main {
    public static void main( String[] args ) throws IOException
    {
    	IPTokenizer ipc = new IPTokenizer("access.log");
    	IPShardingCounter ipSharding = new IPShardingCounter();
    	ipc.OpenAccessLog();
    	String curIp;
    	while ((curIp = ipc.getNextIp()) != null)
    	{
    		ipSharding.inc(curIp);
    	}
    	Map<String, Integer> res = ipSharding.getTopnAccessdIPs(40, 10);
    	System.out.println(res);
    }
}
