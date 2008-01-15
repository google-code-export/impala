package org.impalaframework.command.framework;

public interface TextParsingCommand extends Command {
	
	void extractText(String[] text, CommandState commandState);
	
}
