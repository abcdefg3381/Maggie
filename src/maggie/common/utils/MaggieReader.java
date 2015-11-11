package maggie.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MaggieReader extends BufferedReader {
	public MaggieReader(File file) throws IOException {
		super(new FileReader(file));
	}
	
	public MaggieReader(String filename) throws IOException {
		super(new FileReader(new File(filename)));
	}
}
