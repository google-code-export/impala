package org.impalaframework.spring.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

/**
 * Extension of <code>FileSystemResource</code> which expects the resource
 * concerned to represent a directory on a file system rather than the file
 * itself. Checks that file supplied in constructor is a directory if it exists.
 * @see FileSystemResource
 * @author Phil Zoio
 */
public class DirectoryResource extends FileSystemResource {

	public DirectoryResource(String path) {
		super(path);
		checkIsDirectory();
	}

	public DirectoryResource(File file) {
		super(file);
		checkIsDirectory();
	}

	/**
	 * This implementation returns a URL for the underlying file, with the
	 * assumption that the underlying file is a directory.
	 * @see java.io.File#getAbsolutePath()
	 */
	public URL getURL() throws IOException {
		File directory = getFile();
		return new URL(ResourceUtils.FILE_URL_PREFIX + directory.getAbsolutePath() + "/");
	}

	private void checkIsDirectory() {
		File dir = getFile();
		if (dir.exists()) {
			Assert.isTrue(dir.isDirectory(), "Supplied file '" + dir.getAbsolutePath() + "' is not a directory");
		}
	}

}
