package org.impalaframework.command.impl;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.impl.AlternativeInputCommand;

public class AlternativeInputCommandTest extends ManualAlternativeInputCommandTest {

	public void testCommandSpec()
	{
		AlternativeInputCommand command = new AlternativeInputCommand(new String[] { "one", "two" });
		CommandDefinition commandSpec = command.getCommandDefinition();
		assertEquals(1, commandSpec.getCommandInfos().size());
		CommandInfo ci = commandSpec.getCommandInfos().get(0);
		
		String numberErrorMessage = "Invalid Selection. Selected number does not correspond with one of the values.\nPlease select a number between 1 and 2";
		assertEquals(numberErrorMessage, ci.validate("-1"));
		assertEquals(numberErrorMessage, ci.validate("3"));
		
		String alphaErrorMessage = "Invalid Selection. Please select a number corresponding with one of the alternative choices";
		assertEquals(alphaErrorMessage , ci.validate("a value"));
		assertEquals(null, ci.validate("2"));
	}

	protected CommandLineInputCapturer getInputCapturer() {
		CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer(){
			@Override
			public String capture(CommandInfo info) {
				return "1";
			}};
		return inputCapturer;
	}

}
