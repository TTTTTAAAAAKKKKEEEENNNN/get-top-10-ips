package com.algo.utils.tokenizer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class IPTokenizer 
{
	BufferedInputStream bis = null;
	private String fileName = null;
	private boolean opened = false;
	
	public IPTokenizer(String filename)
	{
		fileName = filename;
	}
	
	static boolean isBlank(byte b)
	{
		switch (b)
		{
		case ' ':return true;
		case '\t':return true;
		default:return false;
		}
	}

	static boolean isNewline(byte b)
	{
		switch (b)
		{
		case '\r':return true;
		case '\n':return true;
		default:return false;
		}
	}

	public void OpenAccessLog() throws FileNotFoundException
	{
		if (opened)
		{
			return;
		}
		opened = true;
		if (fileName == null)
		{
			throw new FileNotFoundException("fileName can't be null!");
		}
		FileInputStream is = new FileInputStream(fileName);
		
		bis = new BufferedInputStream(is);
	}
	
	

	void eatLine() throws IOException
	{
		int inputData;
		while ((inputData = bis.read()) != -1)
		{
			if (inputData == '\n')
			{
				break;
			}
		}
	}
	
	public String getNextIp() throws IOException
	{
		if (!opened)
		{
			OpenAccessLog();
		}
		if (bis == null)
		{
			return null;
		}
		
		/*We are at the beginning of a line*/
		int inputData;
		byte bIPdata[] = new byte[16];
		byte curCh;
		int curPos = 0;
		while ((inputData = bis.read()) != -1)
		{
			curCh = (byte)inputData;
			if (isBlank(curCh))
			{
				eatLine();
				String curIp = new String(bIPdata);
				return curIp.trim();
			}
			else if (curCh == '\n')
			{
				String curIp = new String(bIPdata);
				return curIp.trim();
			}
				
			bIPdata[curPos] = curCh;
			curPos++;
		}
		
		return null;
	}
}
