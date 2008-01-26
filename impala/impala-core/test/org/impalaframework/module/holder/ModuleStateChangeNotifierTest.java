package org.impalaframework.module.holder;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.Transition;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class ModuleStateChangeNotifierTest extends TestCase {

	private DefaultModuleStateChangeNotifier notifier;

	private ModuleStateChange change;

	private ModuleStateChangeListener listener1;

	private ModuleStateChangeListener listener2;

	private ModuleStateChangeListener listener3;

	private ModuleStateHolder moduleStateHolder;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		notifier = new DefaultModuleStateChangeNotifier();
		change = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, new SimpleModuleDefinition("myModule"));
		List<ModuleStateChangeListener> listeners = new ArrayList<ModuleStateChangeListener>();
		listener1 = createMock(ModuleStateChangeListener.class);
		listener2 = createMock(ModuleStateChangeListener.class);
		listener3 = createMock(ModuleStateChangeListener.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);
		listeners.add(listener1);
		listeners.add(listener2);
		listeners.add(listener3);
		notifier.setListeners(listeners);
	}

	public final void testNotify1() {
		notifier = new DefaultModuleStateChangeNotifier();
		assertNotNull(notifier.getListeners());
		assertEquals(0, notifier.getListeners().size());

		ModuleStateChangeListener listener1 = createMock(ModuleStateChangeListener.class);
		notifier.addListener(listener1);
		assertEquals(1, notifier.getListeners().size());

		List<ModuleStateChangeListener> listeners = new ArrayList<ModuleStateChangeListener>();
		listeners.add(createMock(ModuleStateChangeListener.class));
		listeners.add(createMock(ModuleStateChangeListener.class));
		notifier.setListeners(listeners);
		assertEquals(2, notifier.getListeners().size());
	}

	public void testNotify2() throws Exception {
		
		//listener1 returns the same module name as current name, so is notified
		expect(listener1.getModuleName()).andReturn("myModule");
		listener1.moduleStateChanged(moduleStateHolder, change);

		//listener2 returns null for module name, so is notified
		expect(listener2.getModuleName()).andReturn(null);
		listener2.moduleStateChanged(moduleStateHolder, change);

		//listener3 returns a different module name as current name, so is not notified
		expect(listener3.getModuleName()).andReturn("anotherModule");

		replay(listener1);
		replay(listener2);
		replay(listener3);
		replay(moduleStateHolder);
		notifier.notify(moduleStateHolder, change);

		verify(listener1);
		verify(listener2);
		verify(listener3);
		verify(moduleStateHolder);
	}

}
