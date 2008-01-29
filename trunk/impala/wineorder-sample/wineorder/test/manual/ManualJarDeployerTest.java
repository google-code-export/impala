package manual;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.facade.JarOperationsFacade;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationContext;

public class ManualJarDeployerTest extends TestCase implements ModuleDefinitionSource {

	public void testRun() throws Exception {
		String workspaceRoot = "../deploy";
		File file = new File(workspaceRoot);
		assertTrue(file.exists());
		
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, workspaceRoot);
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "wineorder");
		System.setProperty(LocationConstants.APPLICATION_VERSION, "0.1");
		JarOperationsFacade facade = new JarOperationsFacade();
		facade.init(this);
		
		ApplicationContext rootContext = facade.getRootContext();
		ClassLoader classLoader = rootContext.getClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		
		Object bean = rootContext.getBean("wineDAO");
		System.out.println(ReflectionUtils.invokeMethod(bean, "toString", new Object[0]));
		
		Class<?> wineClass = Class.forName("classes.Wine", false, classLoader);
		Object wine = wineClass.newInstance();
		BeanWrapper wineWrapper = new BeanWrapperImpl(wine);
		wineWrapper.setPropertyValue("title", "mytitle");
		
		System.out.println(ReflectionUtils.invokeMethod(bean, "save", new Object[]{ wine }));
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return new SimpleModuleDefinitionSource("parent-context.xml", new String[] { "wineorder-dao", "wineorder-hibernate" }).getModuleDefinition();
	}
	
}
