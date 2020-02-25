package com.algo.utils.tokenizer;

import java.io.IOException;

//TODO
public class RangedIPTokenizer extends IPTokenizer {
	
	private int startLine;
	private int endLine;
	private int curLine = 0;

	public RangedIPTokenizer(String filename, int startLine, int endLine) {
		super(filename);
		this.startLine = startLine;
		this.endLine = endLine;
	}
}
