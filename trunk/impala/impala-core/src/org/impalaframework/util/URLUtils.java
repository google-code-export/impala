package org.impalaframework.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public abstract class URLUtils {

	public static URL[] createUrls(File[] files) {
		
		Assert.notNull(files);
		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			URL url = null;
			
			boolean addSlash = false;
			if (file.isDirectory()) {
				addSlash = true;
			}
			
			try {
				String canonicalPath = file.getCanonicalPath();
				url = new URL("file:" + StringUtils.cleanPath(canonicalPath + (addSlash ? "/" : "")));
			}
			catch (MalformedURLException e) {
				throw new IllegalArgumentException("Location " + file + " cannot be converted to a URL", e);
			}
			catch (IOException e) {
				throw new IllegalArgumentException("Cannot get canonical path for location " + file, e);
			}
	
			urls[i] = url;
		}
		return urls;
	}

}
