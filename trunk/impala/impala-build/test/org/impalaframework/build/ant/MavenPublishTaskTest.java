package org.impalaframework.build.ant;

import java.io.File;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;

public class MavenPublishTaskTest extends TestCase {

    private MavenPublishTask task;
    private File destDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        task = new MavenPublishTask();
        task.setProject(new Project());
        task.setSourceDir(new File("nonexistent"));
        task.setArtifacts("impala-core,impala-build,impala-osgi");
        task.setOrganisation("org.impalaframework");
        task.setSourceDir(new File("../impala-repository/dist"));
        destDir = new File("../maven/testrepo");
        task.setDestDir(destDir);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Delete delete = new Delete();
        delete.setProject(new Project());
        delete.setDir(destDir);
        delete.setFailOnError(false);
        
        //comment this out to inspect output
        delete.execute();
    }
    
    public void testExecute() throws Exception {
        task.setSharedPomFragment(new File("../impala/mvn/pom.xml"));
        task.execute();
    }
    
    public void testGetSharedPomFragment() throws Exception {
        task.setSharedPomFragment(new File("../impala/mvn/pom.xml"));
        final String sharedPom = task.getSharedPomFragment();
        System.out.println(sharedPom);
        assertTrue(sharedPom.startsWith("<url>"));
    }
    
    public void testCheckArgs() throws Exception {
        task = new MavenPublishTask();
        expectFailure("'sourceDir' cannot be null");
        task.setSourceDir(new File("nonexistent"));
        expectFailure("'artifacts' cannot be null");
        task.setArtifacts("art1,art2");
        expectFailure("'organisation' cannot be null");
        task.setOrganisation("org");
        expectFailure("'destDir' cannot be null");
        task.setDestDir(new File("nonexistent"));
        expectFailure("The source directory 'nonexistent' does not exist");
        task.setSourceDir(new File("../impala-build/build.xml"));
        expectFailure("'sourceDir' is not a directory");
        task.setSourceDir(new File("./"));
        task.checkArgs();       
    }
    
    public void testFirstDigitIndex() throws Exception {
        assertEquals(12, task.firstDigitIndex("impala-build-1.0"));
        assertEquals(12, task.firstDigitIndex("impala-build-1.0-RC2"));
        assertEquals(12, task.firstDigitIndex("impala-build-1.0-SNAPSHOT"));
        assertEquals(-1, task.firstDigitIndex("impala-build"));
        assertEquals(-1, task.firstDigitIndex("impala-build-"));
    }
    
    public void testGetFiles() throws Exception {
        final File[] files = task.getFiles();       
        for (File file : files) {
            System.out.println(file.getName());
        }
        assertTrue(files.length > 0);
    }
    
    public void testGetDescriptions() throws Exception {
        final File[] files = task.getFiles();       
        final ArtifactDescription[] ads = task.getArtifactOutput(files);
        for (ArtifactDescription artifactDescription : ads) {
            System.out.println(artifactDescription);
        }
    }
    
    public void testGetOrganisationDirectory() throws Exception {
        final File organisationDirectory = task.getOrganisationDirectory();
        assertEquals(new File("../maven/testrepo/org/impalaframework"), organisationDirectory);
    }

    private void expectFailure(String expectedMessage) {
        try {
            task.checkArgs();
            fail();
        } catch (BuildException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
}
