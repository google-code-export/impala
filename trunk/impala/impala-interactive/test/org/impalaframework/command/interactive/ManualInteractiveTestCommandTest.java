package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.CommandLineInputCapturer;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.interactive.InteractiveTestCommand;

public class ManualInteractiveTestCommandTest extends TestCase {

	public void testInteractive() throws Exception {

		InteractiveTestCommand command = getCommand();

		// now need to capture
		CommandState commandState = new CommandState();

		CommandLineInputCapturer inputCapturer = getInputCapturer();
		commandState.setInputCapturer(inputCapturer);

		System.out.println("--------------------");

		commandState.capture(command);
		command.execute(commandState);
	}

	protected InteractiveTestCommand getCommand() {
		InteractiveTestCommand command = new InteractiveTestCommand();
		return command;
	}

	protected CommandLineInputCapturer getInputCapturer() {
		return new CommandLineInputCapturer();
	}
}
