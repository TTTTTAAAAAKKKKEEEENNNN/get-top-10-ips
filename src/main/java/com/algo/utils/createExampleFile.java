package com.algo.utils;

import java.io.IOException;

import com.algo.counting.IpCounter;

public class createExampleFile {
    public static void main( String[] args ) throws IOException
    {
    	LogFileCreator.generateRandomIpLogFile( "e:\\access.log", 10000000);
    }
}
