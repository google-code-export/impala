package net.java.impala.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.FileCopyUtils;

public class ClassLoaderTestUtils {

	static CustomClassLoader getLoader(String location) {
		File file = new File(location);
		return new CustomClassLoader(new File[] { file });
	}

	static String readResource(ClassLoader location1Loader, String resourceName) throws IOException {
		InputStream resource = location1Loader.getResourceAsStream(resourceName);
		String result = FileCopyUtils.copyToString(new InputStreamReader(resource));
		return result;
	}

}
