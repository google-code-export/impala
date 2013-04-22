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

package org.impalaframework.command.framework;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class CommandStateTest extends TestCase {

    private CommandState commandState;

    public void setUp() {
        commandState = new CommandState();
    }

    public void testCapture() {

        RecordingInputCapturer capturer = new RecordingInputCapturer() {
            @Override
            public String capture(CommandInfo info) {
                super.capture(info);
                return "not a number";
            }

            @Override
            public String recapture(CommandInfo info) {
                super.recapture(info);
                if (recaptured.size() <= 1)
                    return "still not a number";
                return "2";
            }

        };
        commandState.setInputCapturer(capturer);
        TestCommand command = new TestCommand();

        // shared, mandatory, not isolated
        command.addCommandInfo(new CommandInfo("number", "A number", "Please type a number", null, null, true, false,
                false, false) {
            @Override
            public String validate(String input) {
                try {
                    Integer.parseInt(input);
                }
                catch (NumberFormatException e) {
                    return "Please enter a number";
                }
                return null;
            }
        });

        CommandInput capture = commandState.capture(command);
        assertFalse(capture.isGoBack());
        assertEquals(1, capture.properties.size());

        assertEquals(1, capturer.captured.size());
        assertEquals(2, capturer.recaptured.size());
        assertEquals(2, capturer.validationMessages.size());

        assertEquals("Please enter a number", capturer.validationMessages.get(0));
        assertEquals("Please enter a number", capturer.validationMessages.get(1));
    }

    public void testBack() {

        RecordingInputCapturer capturer = new RecordingInputCapturer() {
            @Override
            public String capture(CommandInfo info) {
                super.capture(info);
                return "back";
            }

        };
        commandState.setInputCapturer(capturer);
        TestCommand command = new TestCommand();

        // shared, mandatory, not isolated
        command.addCommandInfo(new CommandInfo("number", "A number", "Please type a number", null, null, true, false,
                false, false));

        GlobalCommandState.getInstance().reset();
        CommandInput capture = commandState.capture(command);
        assertTrue(capture.isGoBack());
        assertEquals(0, capture.properties.size());

        assertEquals(1, capturer.captured.size());
        assertEquals(0, capturer.recaptured.size());
        assertEquals(0, capturer.validationMessages.size());
    }

    public void testQuit() {

        RecordingInputCapturer capturer = new RecordingInputCapturer() {
            @Override
            public String capture(CommandInfo info) {
                super.capture(info);
                return "quit";
            }

        };
        commandState.setInputCapturer(capturer);
        TestCommand command = new TestCommand();

        // shared, mandatory, not isolated
        command.addCommandInfo(new CommandInfo("number", "A number", "Please type a number", null, null, true, false,
                false, false));

        try {
            commandState.capture(command);
            fail();
        }
        catch (TerminatedCommandException e) {
        }
    }

    public void testDefaultValue() {

        // not returning any input
        RecordingInputCapturer capturer = new RecordingInputCapturer();
        commandState.setInputCapturer(capturer);
        TestCommand command = new TestCommand();

        // default of 3
        command.addCommandInfo(new CommandInfo("number", "A number", "Please type a number", "3", (String[]) null, true,
                false, false, false));

        CommandInput capture = commandState.capture(command);
        assertFalse(capture.isGoBack());
        assertEquals(1, capture.properties.size());

        assertEquals(1, capturer.captured.size());
        assertEquals(0, capturer.recaptured.size());
        assertEquals(0, capturer.validationMessages.size());

        assertNotNull(commandState.getGlobalStateHolder().getProperty("number"));
    }

    public void testExistingValue() {
        // is true, so zero items are captured
        overriddeTest(true, 0);
        // global override is false, so 1 item is captured
        overriddeTest(false, 1);
    }

    public void overriddeTest(boolean globalOverride, int expectedCaptured) {

        GlobalCommandState.getInstance().reset();

        // existing value
        commandState.getGlobalStateHolder().addProperty("number", new CommandPropertyValue("4", "A number"));

        // not returning any input
        RecordingInputCapturer capturer = new RecordingInputCapturer();
        commandState.setInputCapturer(capturer);
        TestCommand command = new TestCommand();

        // no default
        command.addCommandInfo(new CommandInfo("number", "A number", "Please type a number", null, (String[]) null,
                true, false, false, globalOverride));

        CommandInput capture = commandState.capture(command);
        assertFalse(capture.isGoBack());
        assertEquals(1, capture.properties.size());

        // used the existing value
        assertEquals(expectedCaptured, capturer.captured.size());
        assertEquals(0, capturer.recaptured.size());
        assertEquals(0, capturer.validationMessages.size());

        assertNotNull(commandState.getGlobalStateHolder().getProperty("number"));
        assertEquals("4", capture.properties.get("number").getValue());
    }

    public void testOptional() {
        commandState.getGlobalStateHolder().addProperty("number", null);

        // not returning any input
        RecordingInputCapturer capturer = new RecordingInputCapturer();
        commandState.setInputCapturer(capturer);
        TestCommand command = new TestCommand();

        // no default
        command.addCommandInfo(new CommandInfo("number", "A number", "Please type a number", null, (String[]) null,
                true, true, false, false));

        CommandInput capture = commandState.capture(command);
        assertFalse(capture.isGoBack());
        assertEquals(0, capture.properties.size());

        assertEquals(1, capturer.captured.size());
        assertEquals(0, capturer.recaptured.size());
        assertEquals(0, capturer.validationMessages.size());

        assertNull(capture.properties.get("number"));
    }

}

class TestCommand implements Command {
    private CommandDefinition commandDefinition = new CommandDefinition();

    void addCommandInfo(CommandInfo info) {
        commandDefinition.add(info);
    }

    public boolean execute(CommandState commandState) {
        return false;
    }

    public CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

}

class RecordingInputCapturer implements InputCapturer {

    List<String> captured = new ArrayList<String>();

    List<String> recaptured = new ArrayList<String>();

    List<String> validationMessages = new ArrayList<String>();

    public String capture(CommandInfo info) {
        captured.add(info.getPropertyName());
        return null;
    }

    public String recapture(CommandInfo info) {
        recaptured.add(info.getPropertyName());
        return null;
    }

    public void displayValidationMessage(String validationMessage) {
        validationMessages.add(validationMessage);
    }

}
