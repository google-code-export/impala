package org.impalaframework.command.interactive;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.impalaframework.util.MemoryUtils;
import org.springframework.util.StopWatch;

public class InteractiveCommandUtils {
	
	public static void printException(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		System.out.println(stringWriter.toString());
	}

	public static void printReloadInfo(String moduleToReload, StopWatch watch) {
		if (moduleToReload != null) {
			System.out.println("Module '" + moduleToReload + "' loaded in " + watch.getTotalTimeSeconds() + " seconds");
			System.out.println(MemoryUtils.getMemoryInfo());
		}
		else {
			System.out.println("No module found which matches the text '" + moduleToReload + "'");
		}
	}
	
}
