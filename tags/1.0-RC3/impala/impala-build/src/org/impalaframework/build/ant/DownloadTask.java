/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.build.ant;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;

/**
 * Extension of GetTask which uses the following format to identify
 * dependencies: [targetDir] from [organisation]:[artifact]:[version] <br/>
 * <br/> For example the following entry would go into the repository's main
 * directory as ant-1.7.0.jar<br/> main from org.apache.ant:ant:1.7.0<br/>
 * <br/> Unlike <code>GetTask</code>, this allows for a single dependency
 * file to be applied to all target locations
 * @author Phil Zoio
 */
public class DownloadTask extends GetTask {

	@Override
	protected List<DownloadInfo> getDownloadInfos(String urlString) {

		ArtifactInfo ai = parseArtifactInfo(urlString);

		List<DownloadInfo> dis = new LinkedList<DownloadInfo>();

		String fileName = ai.getArtifact() + "-" + ai.getVersion();
		String url = url(ai, fileName);

		File subDirectory = new File(getToDir(), ai.getTargetSubdirectory());
		File toFile = new File(subDirectory, fileName + ".jar");

		dis.add(new DownloadInfo(url, toFile));

		boolean downloadSource = isDownloadSources();
		if (ai.isHasSource() != null) {
			// only override if hasSource property is set
			downloadSource = ai.isHasSource();
		}

		if (downloadSource) {
			String sourceFileName = fileName + "-sources";
			String sourceUrl = url(ai, sourceFileName);
			File toSourceFile = new File(subDirectory, sourceFileName + ".jar");
			dis.add(new DownloadInfo(sourceUrl, toSourceFile));
		}

		return dis;
	}

	private String url(ArtifactInfo ai, String fileName) {
		String url = ai.getOrganisation() + "/" + ai.getArtifact() + "/" + ai.getVersion() + "/" + fileName + ".jar";
		return url;
	}

	ArtifactInfo parseArtifactInfo(String urlString) {

		final String invalidFormatString = urlString
				+ " in "
				+ getDependencies()
				+ " has invalid format. Should be: [targetDir] from [organisation]:[artifact]:[version]:[extraInfo] source=[true|false]";

		String[] twoPart = urlString.split("from");

		if (twoPart.length != 2) {
			throw new BuildException(invalidFormatString);
		}

		ArtifactInfo info = new ArtifactInfo();
		info.setTargetSubdirectory(twoPart[0].trim());

		String[] threePart = twoPart[1].split(":");

		if (threePart.length != 3 && threePart.length != 4) {
			throw new BuildException(invalidFormatString);
		}

		info.setOrganisation(replaceAndTrim(threePart[0]));
		info.setArtifact(threePart[1]);

		String remainder = null;

		String[] remainderArray = null; 
		
		if (threePart.length == 3) {
			remainder = threePart[2];
			remainderArray = remainder.split(" ");
			info.setVersion(remainderArray[0].trim());
		} else {
			info.setVersion(threePart[2]);
			remainder = threePart[3];
			remainderArray = remainder.split(" ");
			info.setExtraInfo(remainderArray[0].trim());
		}

		if (remainderArray.length == 2) {
			String[] sourceArray = remainderArray[1].split("=");
			if (sourceArray.length != 2) {
				throw new BuildException(invalidFormatString);
			}
			if (!sourceArray[0].equals("source")) {
				throw new BuildException(invalidFormatString);
			}
			boolean source = Boolean.valueOf(sourceArray[1]);
			info.setHasSource(source);
		}

		return info;
	}

	String replaceAndTrim(String segment) {
		
		StringBuffer buffer = new StringBuffer(segment.length());
		char[] array = segment.toCharArray();
		for (int i = 0; i < array.length; i++) {
			
			if (array[i] == '\\') {
				continue;
			}
			
			if (array[i] == '.') {
				if (i > 0) {
					if (array[i - 1] == '\\') {
						buffer.append('.');
					}
					else {
						buffer.append('/');
					}
				}
				else {
					buffer.append('/');
				}
			} else {
				buffer.append(array[i]);
			}
		}
		return buffer.toString().trim();
		
	}

}
