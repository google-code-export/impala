package org.impalaframework.ant;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class SVNRevisionTask extends Task {

	protected static final String DEFAULT_REVISION_PROPERTY = "svn.revision";

	private String svnUrl;

	private String latestRevisionProperty;

	@Override
	public void execute() throws BuildException {
		try {
			String in = readUrl(svnUrl);
			String temp = extractRevision(in);
			setRevisionValue(temp);
		} catch (IOException e) {
			throw new BuildException(
					"Unable to determine latest svn revision for svnUrl:[" + svnUrl + "].", getLocation());
		}
	}

    String extractRevision(String in) {
		String temp = in.replaceAll("\n", "");
		temp = temp.replaceAll(":.*","");
		temp = temp.replaceAll(".*Revision ", "");
		return temp;
	}

	protected String readUrl(String svnUrl) throws IOException {
		URL url = new URL(svnUrl);
		InputStream is = null;
		String in = null;
		try { is = url.openStream();
			in = read(is);
		}
		finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
		return in;
	}

	private static String read(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[2048];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	private void setRevisionValue(String latestRevision) {
		String property = getRevisionPropertyName();
		getProject().setProperty(property, latestRevision);
	}

	private String getRevisionPropertyName() {
		String property = DEFAULT_REVISION_PROPERTY;
		if (latestRevisionProperty != null) {
			property = latestRevisionProperty;
		}
		return property;
	}

	public void setUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}

	public void setLatestRevisionProperty(String latestRevisionProperty) {
		this.latestRevisionProperty = latestRevisionProperty;
	}

}
