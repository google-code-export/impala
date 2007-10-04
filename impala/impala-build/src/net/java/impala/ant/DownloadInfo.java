package net.java.impala.ant;

import java.io.File;

public class DownloadInfo {
	private String urlString;

	private File file;

	public DownloadInfo(String urlString, File file) {
		super();
		this.urlString = urlString;
		this.file = file;
	}

	public String getUrlString() {
		return urlString;
	}

	public File getFile() {
		return file;
	}

}
