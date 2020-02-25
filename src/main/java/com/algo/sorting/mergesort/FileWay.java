package com.algo.sorting.mergesort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.algo.sorting.mergesort.merger.Way;

public class FileWay extends Way<String> {

	String fileName;
	BufferedReader fileReader = null;
	String curValue = null;

	public FileWay(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		fileReader = new BufferedReader(new FileReader(fileName));
	}

	void setCachedState(String value) {
		curValue = value;
	}

	@Override
	public String peekValue() {
		if (isDepleted()) {
			return null;
		}
		if (curValue == null) {
			String curLine;
			try {
				curLine = fileReader.readLine();
			} catch (IOException e) {
				curLine = null;
			}
			if (curLine == null) {
				setDepleted(true);
				return null;
			} else {
				String valueAndSortKey[] = curLine.split(":");
				setCachedState(valueAndSortKey[0]);
				setSortKey(Integer.valueOf(valueAndSortKey[1]));
				return curValue;
			}
		} else {
			return curValue;
		}
	}

	@Override
	public String getValue() {
		if (isDepleted()) {
			return null;
		}
		String readFromFile = peekValue();
		setCachedState(null);//Set cache state to null, which will cause peekValue() to read next item from the input stream
		return readFromFile;
	}

}
