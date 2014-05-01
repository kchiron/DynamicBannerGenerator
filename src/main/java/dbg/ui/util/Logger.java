package dbg.ui.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static File logFolder;
	private final File logFile;

	public Logger(String name) {
		if (logFolder == null) {
			logFolder = new File("logs");
			if (!logFolder.exists())
				logFolder.mkdir();
		}

		logFile = new File(logFolder, name + ".log");
	}

	public FileWriter getWriter() {
		FileWriter writer = null;

		if (!logFolder.exists())
			logFolder.mkdir();

		try {
			if (!logFile.exists())
				logFile.createNewFile();

			return new FileWriter(logFile, true);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void print(String message) {
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

	public void error(Exception e) {
		error(e.getClass().getName());
		for (StackTraceElement t : e.getStackTrace()) {
			error("\t" + t.toString());
		}
	}
}