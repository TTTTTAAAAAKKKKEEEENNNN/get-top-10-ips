package com.algo.gettop10ips.test.utils;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TestUtils {
	public static void logFileMultiplier(String filename, int multiplyFactor) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(filename, "rw");
		long size = raf.length();
		byte fileContent[] = new byte[(int) size];
		raf.readFully(fileContent);

		raf.seek(size);
		for (int i = 1; i < multiplyFactor; i++) {
			raf.write(fileContent);
		}

		raf.close();
	}

	public static void main(String[] args) throws IOException {
		TestUtils.logFileMultiplier("access.log", 10);
	}
}
