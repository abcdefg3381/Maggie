package maggie.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MaggieWriter extends OutputStreamWriter {

	public MaggieWriter(File file) throws IOException {
		super(new FileOutputStream(file), "ASCII");
	}

	public MaggieWriter(String filename, String encoding) throws IOException {
		super(new FileOutputStream(new File(filename)), encoding);
	}

	public void write(Object o) throws IOException {
		// TODO Auto-generated method stub
		super.write(o.toString());
	}

	@Override
	public void write(String str) {
		try {
			super.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeln() {
		try {
			super.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			super.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeln(String string) {
		try {
			super.write(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeln();
	}
}
