package net.java.impala.test;

import org.apache.tools.ant.launch.Launcher;

public class AntRun {
	public static void main(String[] args) {
		Launcher.main(new String[] { "-f", "../impala-core/build.xml", "download:get"});
	}
}
