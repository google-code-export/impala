package net.java.impala.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

public class FileUtils {

	public static byte[] getBytes(File f) throws IOException {

		if (f == null)
			throw new IllegalArgumentException("File is null");

		if (!f.exists())
			throw new IllegalArgumentException("File " + f + " does not exist");

		int size = (int) f.length();
		byte buffer[] = new byte[size];
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(f);
			DataInputStream dis = new DataInputStream(inputStream);
			dis.readFully(buffer);
		}
		finally {
			try {
				inputStream.close();
			}
			catch (Exception e) {
			}
		}
		return buffer;
	}

	public static byte[] getBytes(Resource resource) throws IOException {
		InputStream inputStream = null;
		try {
			 inputStream = resource.getInputStream();
			return FileCopyUtils.copyToByteArray(inputStream);
		}
		finally {
			try {
				inputStream.close();
			}
			catch (Exception e) {
			}
		}
	}

}
