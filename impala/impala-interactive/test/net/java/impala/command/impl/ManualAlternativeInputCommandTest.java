package net.java.impala.command.impl;

import junit.framework.TestCase;
import net.java.impala.command.CommandLineInputCapturer;
import net.java.impala.command.CommandState;

public class ManualAlternativeInputCommandTest extends TestCase {
	
	public void testAlternativeInputCommand() throws Exception {
		AlternativeInputCommand command = new AlternativeInputCommand(new String[] { "one", "two" });
		doTest(command);
	}

	@SuppressWarnings("unchecked")
	private void doTest(AlternativeInputCommand command) throws ClassNotFoundException {

		System.out.println("----");
		// now need to capture
		CommandState commandState = new CommandState();

		CommandLineInputCapturer inputCapturer = getInputCapturer();
		commandState.setInputCapturer(inputCapturer);

		commandState.capture(command);
		command.execute(commandState);
		
		System.out.println("Selected alternative: " + command.getSelectedAlternative());
	}
	
	protected CommandLineInputCapturer getInputCapturer() {
		CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer();
		return inputCapturer;
	}
}
