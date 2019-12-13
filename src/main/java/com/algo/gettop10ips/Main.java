package com.algo.gettop10ips;

import java.io.IOException;
import java.util.Map;

import com.algo.sharding.IPSharding;

public class Main {
    public static void main( String[] args ) throws IOException
    {
    	IPTokenizer ipc = new IPTokenizer("access.log");
    	IPSharding ipSharding = new IPSharding();
    	ipc.OpenAccessLog();
    	String curIp;
    	while ((curIp = ipc.getNextIp()) != null)
    	{
    		ipSharding.setIP(curIp);
    		ipSharding.inc();
    	}
    	Map<String, Integer> res = ipSharding.getTopnAccessdIPs(40, 10);
    	System.out.println(res);
    }
}
