package org.impalaframework.command;

public interface TextParsingCommand extends Command {
	
	void extractText(String[] text, CommandState commandState);
	
}
