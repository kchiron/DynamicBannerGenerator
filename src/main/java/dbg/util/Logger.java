package dbg.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static File standardLogFolder;
	private final File logFolder;
	private final File logFile;

	public Logger(File logFolder, String name) {
		this.logFolder = logFolder;
		this.logFile = new File(logFolder, name + ".log");
	}

	public Logger(String subFolder, String name) {
		this(
			subFolder == null ?
					getStandardLogFolder() :
					newFolder(getStandardLogFolder(), subFolder),
			name
		);
	}

	public Logger newLogger(String name) {
		return new Logger(this.logFolder, name);
	}

	private static File newFolder(File parent, String name) {
		File folder = new File(parent, name);
		folder.mkdirs();
		return folder;
	}

	private static File getStandardLogFolder() {
		if (standardLogFolder == null) {
			standardLogFolder = new File("logs");
			standardLogFolder.mkdirs();
		}
		return standardLogFolder;
	}

	public Logger(String name) {
		this((String)null, name);
	}

	public FileWriter getWriter() {
		FileWriter writer = null;

		if (!logFolder.exists())
			logFolder.mkdirs();

		try {
			if (!logFile.exists())
				logFile.createNewFile();

			return new FileWriter(logFile, true);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	 synchronized public void print(String message) {
		FileWriter writer = null;
		try {
			writer = getWriter();
			writer.write("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] " + message);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void println(String message) {
		print(message + "\n");
	}

	public void info(String message) {
		println("INFO : " + message);
	}

	public void error(String message) {
		println("ERROR : " + message);
	}

	public void error(Throwable e) {
		error(e.getClass().getName()+": "+e.getMessage());
		for (StackTraceElement t : e.getStackTrace()) {
			error("\t" + t.toString());
		}
	}
}