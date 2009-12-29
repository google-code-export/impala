package org.impalaframework.module.modification;

import java.util.Collection;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.spi.TransitionSet;

import junit.framework.TestCase;

public class RepairModificationExtractorTest extends TestCase {

    public void testGetTransitions() {
        RootModuleDefinition definition = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3, plugin4");
        ModuleDefinition plugin1 = definition.findChildDefinition("plugin1", true);
        plugin1.setState(ModuleState.ERROR);
        definition.findChildDefinition("plugin3", true).setState(ModuleState.DEPENDENCY_FAILED);
        definition.findChildDefinition("plugin4", true).setState(ModuleState.DEPENDENCY_FAILED);
        
        Application application = TestApplicationManager.newApplicationManager().getCurrentApplication();
        
        TransitionSet transitions = new RepairModificationExtractor().getTransitions(application, definition, null);
        assertEquals(definition, transitions.getNewRootModuleDefinition());
        
        Collection<? extends ModuleStateChange> moduleTransitions = transitions.getModuleTransitions();
        assertEquals(3, moduleTransitions.size());
    }

}
