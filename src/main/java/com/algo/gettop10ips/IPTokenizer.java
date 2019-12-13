package com.algo.gettop10ips;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.algo.gettop10ips.util.Util;

/**
 * Hello world!
 *
 */
public class IPTokenizer 
{
	private final RandomAccessFile is;
	private final String fileName;
	private Long startingOffset = 0L;
	private Long range = 0L;
	private Long currentPos = 0L;
	
	public IPTokenizer(String _filename) throws IOException
	{
		this(_filename, (Long)0L, (Long)null);
	}
	
	public IPTokenizer(String _filename, int files, int curFileNo) throws IOException
	{
		Util.log("Initializing IPTokenizer with filename:" + _filename + "\n" + " files:" + files + "\ncurFileNo:" + curFileNo);
		File f = new File(_filename);
		long length = f.length();
		long rangeForOneFile;
		if (length % files == 0)
		{
			rangeForOneFile = (length / files);
		}
		else
		{
			rangeForOneFile = (length / files) + 1;
		}

		fileName = _filename;
		if (fileName == null)
		{
			throw new IllegalStateException("fileName can't be null!");
		}
		is = new RandomAccessFile(fileName, "r");
		currentPos = startingOffset = rangeForOneFile * curFileNo;
		range = startingOffset + rangeForOneFile;
		Util.log("startingOffset = " + startingOffset + " range = " + range + " currentPos = " + currentPos);
		adjustRange();
		Util.log("after adjustment startingOffset = " + startingOffset + " range = " + range + " currentPos = " + currentPos);
	}
	
	/**
	 * IPTokenizer
	 * This function will try to get an IP from a file whose lines are separated by '\r' or '\r\n'
	 * If the startingOffset is not 0(zero), the program will adjust it to the next '\r' or '\r\n' via adjustRange() function
	 * If the ending position(_range) is not enough to cover a line, the IPTokenizer will automatically extend its range to cover a whole line via  adjustRange() function
	 * @param fileStream The stream to be tokenized
	 * @param _startingOffset The starting position within the fileStream
	 * @param _range The ending position, the tokenizer will try to get IPs until the stream reaches _range,
	 * if _range is not enough to cover a line, the IPTokenizer will continue reading the file until a line end is reached or the EOF is reached
	 * set range to null if you'd like to process the whole file
	 **/
	
	public IPTokenizer(String _filename, Long _startingOffset, Long _range) throws IOException
	{
		fileName = _filename;
		if (fileName == null)
		{
			throw new IllegalStateException("fileName can't be null!");
		}
		is = new RandomAccessFile(fileName, "r");
		currentPos = startingOffset = _startingOffset;
		range = _range;
		adjustRange();
	}
	
	private static boolean isBlank(byte b)
	{
		switch (b)
		{
		case ' ':return true;
		case '\t':return true;
		default:return false;
		}
	}

	private static boolean isNewline(byte b)
	{
		switch (b)
		{
		case '\r':return true;
		case '\n':return true;
		default:return false;
		}
	}
	
	private static boolean isNewline(int b)
	{
		switch (b)
		{
		case '\r':return true;
		case '\n':return true;
		default:return false;
		}
	}

	/**
	 * adjustRange
	 * This function will adjust the startingOffset and range according to the file's contents:
	 * If the startingOffset is not 0(zero), the program will adjust it to the next line (after '\r' or '\r\n') via adjustRange() function
	 * If the ending position(_range) is not enough to cover a line,
	 * the IPTokenizer will automatically extend its range to cover a whole line (Including '\r' or '\r\n') via adjustRange() function
	 * @throws IOException 
	 **/
	private void adjustRange() throws IOException
	{
		int inputData;
		boolean gotNewLine = false;
		is.seek(startingOffset);
		if (startingOffset != 0)
		{
			while ((inputData = is.read()) != -1)
			{
				startingOffset++;//startingOffset points at the next char after inputData
				//1. try to find the first cr(or lf)
				if (!gotNewLine && !isNewline(inputData))
				{
					continue;
				}
				//2. found the new line start
				else if (!gotNewLine && isNewline(inputData))
				{
					gotNewLine = true;
					continue;
				}
				//3. the new line end, we should be coming here from stage 2
				else if (gotNewLine && isNewline(inputData))
				{
					break;
				}
				//4. we are now on the first character of a new line
				else if (gotNewLine && !isNewline(inputData))
				{
					startingOffset--;//go back to current character
					break;
				}
			}

			if (inputData == -1)//encounter EOF
			{
				startingOffset--;
			}
			currentPos = startingOffset;
		}

		if (range == null)
		{
			return;
		}

		is.seek(range);
		gotNewLine = false;
		while ((inputData = is.read()) != -1)
		{
			range++;//range points at the next char after inputData
			//1. try to find the first cr(or lf)
			if (!gotNewLine && !isNewline(inputData))
			{
				continue;
			}
			//2. found the new line start
			else if (!gotNewLine && isNewline(inputData))
			{
				gotNewLine = true;
				continue;
			}
			//3. the new line end, we should be coming here from stage 2
			else if (gotNewLine && isNewline(inputData))
			{
				range--;//go back to current character, since inputData is the trailing '\n'
				break;
			}
			//4. we are now on the first character of a new line
			else if (gotNewLine && !isNewline(inputData))
			{
				range -= 2;//go back to the trailing '\n'
				break;
			}
		}

		if (inputData == -1)//encounter EOF
		{
			range--;
		}
		is.seek(currentPos);
	}
	
	void eatLine() throws IOException
	{
		int inputData;
		while ((inputData = is.read()) != -1)
		{
			currentPos++;
			if (inputData == '\n')
			{
				break;
			}
		}
	}

	
	public String getNextIp() throws IOException
	{
		if (is == null)
		{
			return null;
		}

		/*We are at the beginning of a line*/
		int inputData;
		byte bIPdata[] = new byte[16];
		byte curCh;
		int curIpdataPos = 0;
		int dotCount = 0;
		while ((inputData = is.read()) != -1)
		{
			if (range != null)//If we are not processing a whole file
			{
				if (currentPos > range)
				{
					break;
				}
			}
			currentPos++;
			curCh = (byte)inputData;
			if (isBlank(curCh))
			{
				continue;
			}
			
			if (curCh == '.')
			{
				bIPdata[curIpdataPos] = curCh;
				curIpdataPos++;
				dotCount++;
			}
			else if (dotCount == 3 && !Character.isDigit(curCh))
			{
				dotCount = 0;
				eatLine();
				String curIp = new String(bIPdata);
				return curIp.trim();
			}
			else
			{
				bIPdata[curIpdataPos] = curCh;
				curIpdataPos++;
			}
		}
		
		return null;
	}
}
