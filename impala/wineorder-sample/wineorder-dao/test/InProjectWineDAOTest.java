

import interfaces.WineDAO;

import java.util.Collection;

import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.testrun.InteractiveTestRunner;

import test.BaseDataTest;
import classes.Wine;
import classes.WineDAOImpl;

public class InProjectWineDAOTest extends BaseDataTest {

	public static void main(String[] args) {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "wineorder");
		InteractiveTestRunner.run(InProjectWineDAOTest.class);
	}

	public void testDAO() {		
		WineDAO dao = DynamicContextHolder.getBean("wineDAO", WineDAO.class);

		//this relies on setting SuiteOperationFacade when running as JUnit test
		//FIXME make this the default
		WineDAOImpl impl = DynamicContextHolder.getModuleBean("wineorder-dao", "wineDAO", WineDAOImpl.class);
		System.out.println(impl.getHibernateTemplate());
		
		Wine wine = new Wine();
		wine.setColor("red");
		wine.setVineyard("Chateau X");
		wine.setTitle("Cabernet");
		wine.setVintage(1996);
		dao.save(wine);

		Collection<Wine> winesOfVintage = dao.getWinesOfVintage(1996);
		System.out.println("Wines of vintage 1996: " + winesOfVintage.size());
		assertEquals(1, winesOfVintage.size());

		wine.setVintage(2000);
		wine.setColor("rose");
		dao.update(wine);

		Wine updated = dao.findById(wine.getId());
		assertEquals(2000, updated.getVintage());

	}

	public RootModuleDefinition getModuleDefinition() {
		return new SimpleModuleDefinitionSource("parent-context.xml", new String[] { "wineorder-dao", "wineorder-hibernate" }).getModuleDefinition();
	}

}