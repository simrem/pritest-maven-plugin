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
     * The project dependency classpath
     *
     * @parameter expression="${project.dependencies}"
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
    private String reportUrl;
    

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
				collectAllPriorityLists(priorityLists);
				new CitrusTester(classLoader, priorityLists.get(1), getLog(), reporter).execute();
				priorityListsToAPFD(priorityLists, reporter.getMeasureList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
        }
        else {
        	PriorityList2 priorityListService = new PriorityList2(new OnlineClassService(citrusTechniqueUrl, techniqueNumber), new LocalClassService(testOutputDirectory), basedir, techniqueNumber);
        	List<String> priorityList = new ArrayList<String>();
			try {
				priorityList.addAll(priorityListService.getPriorityList());
				new CitrusTester(classLoader, priorityList, getLog(), reporter).execute();
				calculateAPFD(reporter.getMeasureList().getList(), techniqueNumber);
			} catch (Exception e2) {
				e2.printStackTrace();
			}

//        try {
//			//reporter.sendReport();
//		} catch (ClientHandlerException e) {
//			getLog().error("Report not sent, could not connect to report server.");
//		} catch (JAXBException e) {
//			getLog().error("Exception - send report");
//		}
        }
    }
    

    private void priorityListsToAPFD(Map<Integer, List<String>> priorityLists, MeasureList measureList) throws IOException {
		for (Integer localTechniqueNumber : priorityLists.keySet()){
			List<String> localPriorityList = priorityLists.get(localTechniqueNumber);
			List<Measure> localMeasureList = new ArrayList<Measure>();
			for(String priorityItem : localPriorityList){
				for(Measure measure : measureList.getList()){
					if(measure.getSource().equals(priorityItem)){
						localMeasureList.add(measure);
						break;
					}
				}
			}
			for(Measure measure : measureList.getList()) {
				if(!localMeasureList.contains(measure)){
					localMeasureList.add(measure);
				}
			}
			calculateAPFD(localMeasureList, localTechniqueNumber);
		}
	}


	private void calculateAPFD(List<Measure> localMeasureList, int localTechniqueNumber) throws IOException {
		APFD apfd = new APFD(new MeasureList(localMeasureList));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		apfd.outputToFile("apfd/" + localTechniqueNumber + "/", sdf.format(new Date()) + ".txt");
	}


	private void collectAllPriorityLists(
			Map<Integer, List<String>> priorityLists) throws NoWorkTreeException, JSONException, IOException, Exception {
		for (int tempTechniqueNumber = 1; tempTechniqueNumber <= 6; tempTechniqueNumber++) {
			PriorityList2 priorityListService = new PriorityList2(new OnlineClassService(citrusTechniqueUrl, tempTechniqueNumber), new LocalClassService(testOutputDirectory), basedir, tempTechniqueNumber);
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
