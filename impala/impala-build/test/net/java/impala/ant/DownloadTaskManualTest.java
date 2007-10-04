package net.java.impala.ant;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.tools.ant.Project;

public class DownloadTaskManualTest extends TestCase {

	private File downloadDir;

	private DownloadTask task;

	public void setUp() {
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		downloadDir = new File(tmpDir, "downloads");
		downloadDir.mkdir();

		task = new DownloadTask();
		task.setBaseSourceUrls("http://ibiblio.org/pub/packages/maven2/");
		task.setToDir(tmpDir);
		task.setDependencies(new File("resources/download-dependencies.txt"));
		task.setProject(new Project());
	}

	public void tearDown() {
		File[] listFiles = downloadDir.listFiles();
		if (listFiles != null) {
			for (int i = 0; i < listFiles.length; i++) {
				System.out.println("Deleting " + listFiles[i]);
				listFiles[i].delete();
			}
		}
		downloadDir.delete();
	}

	public void testExecute() {
		task.execute();
		File[] listFiles = downloadDir.listFiles();
		assertEquals(1, listFiles.length);
	}
	
	public void testExecuteWithSource() {
		task.setDownloadSources(true);
		task.execute();
		File[] listFiles = downloadDir.listFiles();
		assertEquals(2, listFiles.length);
	}
	

	public void testExecuteWithMultipeRepos() throws MalformedURLException {
		File file = new File("C:\\Documents and Settings\\phil\\.m2\\repository");
		URL url = file.toURI().toURL();
		String urlString = url.toString();
		task.setBaseSourceUrls(urlString+",http://ibiblio.org/pub/packages/maven2/");
		task.setDownloadSources(true);
		task.execute();
		File[] listFiles = downloadDir.listFiles();
		assertEquals(2, listFiles.length);
	}

}
