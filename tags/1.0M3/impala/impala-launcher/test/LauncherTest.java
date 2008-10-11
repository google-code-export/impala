import junit.framework.TestCase;


public class LauncherTest extends TestCase {

	public void testLaunch() throws Exception {
		Launcher.launch(MainWithException.class.getName(), new String[0]);
	}
	
}
