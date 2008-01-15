package org.impalaframework.command.framework;

import org.impalaframework.command.basic.PrintDetailsCommand;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;

import junit.framework.TestCase;

public class CommandTest extends TestCase {

	public void testCommand() {

		ExtendedPrintDetailsCommand command = new ExtendedPrintDetailsCommand();

		// now need to capture
		CommandState commandState = new CommandState();
		commandState.setInputCapturer(new CommandLineInputCapturer() {

			public String capture(CommandInfo info) {
				if (info.getPropertyName().equals("dateOfBirth"))
					return "8 may 1969";
				if (info.getPropertyName().equals("name"))
					return "phil";
				return null;
			}
		});

		commandState.capture(command);
		command.execute(commandState);

		assertEquals("8 may 1969", command.getInput("dateOfBirth"));
		assertEquals("phil", command.getInput("name"));
	}

	public void testGlobalHolder() {

		GlobalCommandState holder = GlobalCommandState.getInstance();
		holder.addProperty("residence", new CommandPropertyValue("Woodbridge", true, "Place of residence"));

		ExtendedPrintDetailsCommand command = new ExtendedPrintDetailsCommand();

		// now need to capture
		CommandState commandState = new CommandState();
		commandState.setInputCapturer(new CommandLineInputCapturer() {

			public String capture(CommandInfo info) {
				if (info.getPropertyName().equals("dateOfBirth"))
					return null;
				if (info.getPropertyName().equals("name"))
					return "phil";
				return null;
			}
		});

		commandState.capture(command);
		command.execute(commandState);

		// this is optional
		assertEquals(null, command.getInput("dateOfBirth"));

		// this is shared
		assertEquals("phil", command.getInput("name"));

		// this comes from global default
		assertEquals("Woodbridge", command.getInput("residence"));

		// verified that this has been shared
		assertEquals("phil", holder.getProperty("name").getValue());

		// verified that this has come from default
		assertEquals(null, holder.getProperty("IvyD"));
	}

	class ExtendedPrintDetailsCommand extends PrintDetailsCommand {

		private CommandState state;

		public ExtendedPrintDetailsCommand() {
			super();
		}

		@Override
		public boolean execute(CommandState commandState) {
			super.execute(commandState);
			this.state = commandState;
			return true;
		}

		public String getInput(String name) {
			assertNotNull(state);
			CommandPropertyValue commandPropertyValue = state.getProperties().get(name);
			if (commandPropertyValue == null)
				return null;
			return commandPropertyValue.getValue();
		}

	}

}
