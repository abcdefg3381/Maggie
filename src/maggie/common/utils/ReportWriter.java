package maggie.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * A file writer.
 * 
 * @author LIU Xiaofan
 * 
 */
public class ReportWriter {
	protected class MyPrintWriter extends OutputStreamWriter {

		public MyPrintWriter(OutputStream os) throws UnsupportedEncodingException {
			super(os, "UTF-8");
		}

		public void print(String string) {
			try {
				write(string);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void println() {
			try {
				write(System.getProperty("line.separator"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void println(String string) {
			try {
				write(string);
				write(System.getProperty("line.separator"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean on = true;

	private MyPrintWriter out = null;

	protected void closePrintWriter() {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		out = null;
	}

	protected MyPrintWriter getPrintWriter() {
		return out;
	}

	protected void print(String string) {
		if (on == false)
			return;
		getPrintWriter().print(string);
	}

	protected void println() {
		if (on == false)
			return;
		getPrintWriter().println();
	}

	protected void println(String string) {
		if (on == false)
			return;
		getPrintWriter().println(string);
	}

	protected void setPrintWriter(PrintStream ps) {
		try {
			out = new MyPrintWriter(ps);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	protected void setPrintWriter(File file) {
		if (file == null) {
			on = false;
			return;
		}
		try {
			out = new MyPrintWriter(new FileOutputStream(file));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
