package org.impalaframework.command.interactive;

import org.impalaframework.command.CommandInfo;
import org.impalaframework.command.CommandLineInputCapturer;

public class InteractiveTestCommandTest extends ManualInteractiveTestCommandTest {

	
	
	@Override
	public void testInteractive() throws Exception {
		super.testInteractive();
		
		
	}

	@Override
	protected CommandLineInputCapturer getInputCapturer() {
		CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer() {
			@Override
			public String capture(CommandInfo info) {
				if (info.getPropertyName().equals("commandText")) {
					return "run";
				}
				return null;
			}

		};
		return inputCapturer;
	}

}
