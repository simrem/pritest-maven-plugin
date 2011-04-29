package no.citrus.runner.junit;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import no.citrus.restapi.model.Measure;
import no.citrus.restapi.model.MeasureList;
import no.citrus.runner.junit.priority.LocalClassService;
import no.citrus.runner.junit.priority.OnlineClassService;
import no.citrus.runner.junit.priority.PriorityList2;
import no.citrus.runner.junit.reporter.APFD;
import no.citrus.runner.junit.reporter.APFDHelper;
import no.citrus.runner.junit.reporter.Reporter;
import no.citrus.runner.junit.test.CitrusTester;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.jettison.json.JSONException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.junit.runners.model.InitializationError;

import com.sun.jersey.api.client.ClientHandlerException;


/**
 * @goal runtest
 */
public class RunnerMojo extends AbstractMojo {
    /**
     * @parameter default-value="${project}"
     */
    protected MavenProject mavenProject;

    /**
     * @parameter default-value="${project.basedir}"
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
     * @parameter default-value="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    protected List<String> testClasspathElements;

     /**
     * The project dependency classpath
     *
     * @parameter default-value="${project.dependencies}"
     * @required
     * @readonly
     */
    protected List<Dependency> dependencyClasspaths;

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
    private int techniqueNumber;
    
    /**
     * @parameter
     */
    private Integer[] technique0;
    
    /**
     * @parameter
     */
    private String reportUrl;
    
    /**
     * @parameter default-value="false"
     */
    private boolean skipSendReport;
    
    /**
     * @parameter default-value="false"
     */
    private boolean skipCalculateAPFD;
    
    /**
     * @parameter default-value="${project.build.testSourceDirectory}"
     */
    private String testSourceDirectory;
    
    /**
     * @parameter default-value="${project.build.sourceDirectory}"
     */
    private String sourceDirectory;
    

    private List<URL> citrusClassPaths;

    public void execute() throws MojoExecutionException, MojoFailureException {
    	citrusClassPaths = new ArrayList<URL>();
    	addFileToClassPath(testOutputDirectory);
    	addFileToClassPath(classesDirectory);
    	for (Artifact artifact : (Set<Artifact>) mavenProject.getArtifacts()) {
    		addFileToClassPath(artifact.getFile());
    	}

    	URLClassLoader classLoader = new URLClassLoader(citrusClassPaths.toArray(new URL[]{}), this.getClass().getClassLoader());
    	Reporter reporter = new Reporter(reportUrl, new ArrayList<Measure>());

    	getLog().info(String.format("Technique Number = %d", techniqueNumber));
    	getLog().info("Fetching priority list...");
    	if(techniqueNumber == 0) {
    		Map<Integer, List<String>> priorityLists = new HashMap<Integer, List<String>>();
    		try {
    			collectPriorityLists(technique0, priorityLists);
    			if(priorityLists.size() > 0) {
    				new CitrusTester(classLoader, priorityLists.get(technique0[0]), getLog(), reporter).execute();
    			}
    			priorityListsToAPFD(priorityLists, reporter.getMeasureList().getList());
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    	}
    	else {
    		PriorityList2 priorityListService = new PriorityList2(new OnlineClassService(citrusTechniqueUrl, techniqueNumber), new LocalClassService(testOutputDirectory), basedir, techniqueNumber, testSourceDirectory, sourceDirectory);
    		List<String> priorityList = new ArrayList<String>();
    		try {
    			priorityList.addAll(priorityListService.getPriorityList());
    			new CitrusTester(classLoader, priorityList, getLog(), reporter).execute();
    			APFD apfd = new APFD(reporter.getMeasureList());
    			APFDHelper.outputAPFDToFile(apfd, techniqueNumber);
    		} catch (Exception e2) {
    			e2.printStackTrace();
    		}
        }
    	if(!skipSendReport) {
			try {
				reporter.sendReport();
			} catch (ClientHandlerException e) {
				getLog().error("Report not sent, could not connect to report server.");
			} catch (JAXBException e) {
				getLog().error("Exception - send report");
			}
		}
    }
    

    private void priorityListsToAPFD(Map<Integer, List<String>> priorityLists, List<Measure> measureList) throws IOException {
		for (Integer localTechniqueNumber : priorityLists.keySet()){
			List<String> localPriorityList = priorityLists.get(localTechniqueNumber);
			List<Measure> localMeasureList = APFDHelper.sortMeasureListBySource(localPriorityList, measureList);
			APFD apfd = new APFD(new MeasureList(localMeasureList));
			APFDHelper.outputAPFDToFile(apfd, localTechniqueNumber);
		}
	}

	private void collectPriorityLists(Integer[] techniqueArray, Map<Integer, List<String>> priorityLists) throws NoWorkTreeException, JSONException, IOException, Exception {
		for(int tempTechniqueNumber : techniqueArray) {
			PriorityList2 priorityListService = new PriorityList2(new OnlineClassService(citrusTechniqueUrl, tempTechniqueNumber), new LocalClassService(testOutputDirectory), basedir, tempTechniqueNumber, testSourceDirectory, sourceDirectory);
			priorityLists.put(tempTechniqueNumber, priorityListService.getPriorityList());
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
