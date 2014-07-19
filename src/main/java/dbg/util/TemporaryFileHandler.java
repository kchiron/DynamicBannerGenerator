package dbg.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemporaryFileHandler {

	private static File tempFolder;

	private final List<File> files;

	public TemporaryFileHandler() {
		this.files = new ArrayList<>();
	}

	public void clearAllFiles() {
		synchronized (files) {
			for (File file : files)
				file.delete();
		}
	}

	public File createTempFile(String name) throws IOException {
		return createTempFile(tempFolder, name);
	}

	public File createTempFolder(String name) throws IOException {
		return createTempFolder(tempFolder, name);
	}

	public File createTempFile(File tempFolder, String name) throws IOException {
		return createTempFile(tempFolder, name, false);
	}

	public File createTempFolder(File tempFolder, String name) throws IOException {
		return createTempFile(tempFolder, name, true);
	}

	private File createTempFile(File tempFolder, String name, boolean folder) throws IOException {
		if (tempFolder == null) tempFolder = initialize();

		File tempFile = new File(tempFolder, System.currentTimeMillis() + "-" + name);
		if (folder)
			tempFile.mkdir();
		else
			tempFile.createNewFile();
		synchronized (files) {
			files.add(tempFile);
		}
		return tempFile;
	}

	private static File initialize() throws IOException {
		final File tempFile = File.createTempFile("tempFile", null);
		tempFile.delete();
		final File folder = new File(tempFile.getParent(), "DynamicBannerGenerator");
		folder.mkdir();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					FileUtils.cleanDirectory(folder);
					folder.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return tempFolder = folder;
	}

}
