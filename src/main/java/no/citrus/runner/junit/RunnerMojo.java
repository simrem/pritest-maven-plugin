package no.citrus.runner.junit;

import java.io.File;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import no.citrus.restapi.model.Measure;
import no.citrus.runner.junit.priority.LocalClassService;
import no.citrus.runner.junit.priority.OnlineClassService;
import no.citrus.runner.junit.priority.PriorityList;
import no.citrus.runner.junit.reporter.Reporter;
import no.citrus.runner.junit.test.CitrusTester;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.jettison.json.JSONException;
import org.junit.runners.model.InitializationError;

import com.sun.jersey.api.client.ClientHandlerException;


/**
 * @goal runtest
 */
public class RunnerMojo extends AbstractMojo {
    /**
     * @parameter expression="${project}"
     */
    protected MavenProject mavenProject;

    /**
     * @parameter expression="${project.basedir}"
     * @required
     */
    private File basedir;

    /**
     * The project compile classpath.
     *
     * @parameter default-value="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    protected List<String> compileClasspathElements;

    /**
     * The project test classpath
     *
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    protected List<String> testClasspathElements;

    /**
     * The directory containing generated test classes of the project being tested.
     * This will be included at the beginning of the test classpath.                                                                                                                            *
     *
     * @parameter default-value="${project.build.testOutputDirectory}"
     */
    private File testOutputDirectory;

    /**
     * The directory containing generated classes of the project being tested.
     * This will be included after the test classes in the test classpath.
     *
     * @parameter default-value="${project.build.outputDirectory}"
     */
    private File classesDirectory;

    /**
     * @parameter
     */
    private String citrusTechniqueUrl;
    
    /**
     * @parameter
     */
    private String reportUrl;

    private List<URL> citrusClassPaths;

    public void execute() throws MojoExecutionException, MojoFailureException {
    	citrusClassPaths = new ArrayList<URL>();
    	addFileToClassPath(testOutputDirectory);
    	addFileToClassPath(classesDirectory);

        URLClassLoader classLoader = new URLClassLoader(citrusClassPaths.toArray(new URL[]{}), this.getClass().getClassLoader());

        
        getLog().info("Fetching priority list...");
        
        PriorityList priorityListService = new PriorityList(new OnlineClassService(citrusTechniqueUrl), new LocalClassService(testOutputDirectory));
        List<String> priorityList = new ArrayList<String>();
		try {
			priorityList.addAll(priorityListService.getPriorityList());
		} catch (ConnectException e1) {
			e1.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Reporter reporter = new Reporter(reportUrl, new ArrayList<Measure>());
        try {
            getLog().debug("Initializing CitrusTester with testOrder: " + priorityList.toString());
            new CitrusTester(classLoader, priorityList, getLog(), reporter).execute();
        } catch (InitializationError initializationError) {
            initializationError.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
			reporter.sendReport();
		} catch (ClientHandlerException e) {
			getLog().error("Report not sent, could not connect to report server.");
		} catch (JAXBException e) {
			getLog().error("Exception - send report");
		}
    }

    private void addFileToClassPath(File file) throws MojoExecutionException {
        try {
            this.citrusClassPaths.add(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Could not load classpath elements", e);
        }
    }

}
