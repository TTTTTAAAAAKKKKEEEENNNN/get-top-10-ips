package com.algo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class LogFileCreator {

	static Random rnd = new Random();
	static String newLine = "\n";
	static
	{
		rnd.setSeed(System.currentTimeMillis());
	}
	public static String getRandomIP()
	{
		int ip1 = Math.abs(rnd.nextInt()) % 255;
		int ip2 = Math.abs(rnd.nextInt()) % 255;
		int ip3 = Math.abs(rnd.nextInt()) % 255;
		int ip4 = Math.abs(rnd.nextInt()) % 255;
		
		return ip1 + "." + ip2 + "." + ip3 + "." + ip4;
	}
	
	/*
	 * multiplyFile
	 * multiply srcFile by @param times times, and write it to dstFile
	 * @throws IOException
	 */
	public static void multiplyFile(String srcFile, String dstFile, int times) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(dstFile);
		File srcFileHandle = new File(srcFile);
		FileInputStream is = new FileInputStream(srcFile);
		byte srcContent[] = new byte[(int) srcFileHandle.length()];
		is.read(srcContent);
		for (int i = 0; i < times; i++)
		{
			fos.write(srcContent);
		}
		is.close();
		fos.close();
	}
	
	public static void generateRandomIpLogFile(String dstFile, int numberRecs) throws IOException
	{
		String logContent[] =
		{
		" - - [16/Nov/2018:17:19:10 +0800] \"POST /some-web-service/getSomeInfo?iCount=10&lStartNo=1 HTTP/1.1\" 200 8995 \"http://88.88.88.88/this-module\" \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\"",
		" - - [16/Nov/2018:17:19:21 +0800] \"POST /some-other-service/kernel/checkStatus HTTP/1.1\" 200 90 \"http://88.88.88.88:9090/chk\" \"Mozilla/5.0 (WindoleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36\"",
		" - - [16/Nov/2018:17:20:24 +0800] \"POST /some-good-service/make-it-bad/query HTTP/1.1\" 200 459 \"http://88.88.88.88:3333/allPolicy\" \"Mozilla/5.0 (Wind4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36\"",
		" - - [16/Nov/2018:17:20:25 +0800] \"POST /some-bad-service/admin/checkUser HTTP/1.1\" 200 550 \"http://88.88.88.88:3433/allPolicy\" \"Mozilla/5.0 (; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36\"",
		" - - [16/Nov/2018:17:20:25 +0800] \"GET /lowPower?data=Check this line of data on your computer and make sure it does no harm to your computer",
		" - - [16/Nov/2018:17:20:25 +0800] \"GET /dynamic/js/manifest.73f0323e9023d1d20095.js HTTP/1.1\" 200 2440 \"http://88.88.88.88:9898/freezedUser?dummyData1dummyData1dummyData1dummyData1dummyData1dummyData1/70.102 Safari/537.36\"",
		" - - [16/Nov/2018:17:20:26 +0800] \"GET /dynamic/js/app.f6af20edb9583cf67a94.js HTTP/1.1\" 200 569038 \"http://88.88.88.88:3323/freezedUser?data=dummyData2dummyData2dummyData2dummyData2dummyData2dummyData2/102 Safari/537.36\"",
		" - - [16/Nov/2018:17:20:26 +0800] \"POST /some-bad-service/painting/status HTTP/1.1\" 200 80 \"http://88.88.88.88:3434/index\" \"Mozilla/5.0 (Windows NTleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36\"",
		" - - [16/Nov/2018:17:20:28 +0800] \"POST /state-of-art-service/check/list HTTP/1.1\" 200 17304 \"http://88.88.88.88:3232/index\" \"Mozilla/5.0 (WindowsAppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36\"",
		" - - [16/Nov/2018:17:20:28 +0800] \"POST /old-school-service/sendmessage/count HTTP/1.1\" 200 46 \"http://88.88.88.88:5555/index\" \"Mozilla/5.0 (Window AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36\"",
		};
		FileOutputStream fos = new FileOutputStream(dstFile);

		String previousIp = null;
		for (int i = 0; i < numberRecs; i++)
		{
			String curIp = getRandomIP();
			String fileContent;
			if (previousIp != null && rnd.nextInt(100) > 60)
			{
				fileContent = previousIp;
				curIp = previousIp;
			}
			else
			{
				fileContent = curIp;
				previousIp = curIp;
			}
			fos.write(fileContent.getBytes());
			int curLine = Math.abs(rnd.nextInt()) % logContent.length;
			fos.write(logContent[curLine].getBytes());
			fos.write(newLine.getBytes());
		}
		fos.close();
	}
}
