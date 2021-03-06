/**
    This file is part of Pritest.

    Pritest is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Pritest is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package no.pritest;

import com.sun.jersey.api.client.ClientHandlerException;
import no.pritest.restapi.model.Measure;
import no.pritest.restapi.model.MeasureList;
import no.pritest.priority.LocalClassService;
import no.pritest.priority.OnlineClassService;
import no.pritest.priority.PriorityList2;
import no.pritest.reporter.APFD;
import no.pritest.reporter.APFDHelper;
import no.pritest.reporter.Reporter;
import no.pritest.test.PritestTester;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.jettison.json.JSONException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


/**
 * @goal runtest
 * @requiresDependencyResolution test
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
     * @readonly
     */
    private String testSourceDirectory;
    
    /**
     * @parameter default-value="${project.build.sourceDirectory}"
     * @readonly
     */
    private String sourceDirectory;
    

    private List<URL> citrusClassPaths;

    public void execute() throws MojoExecutionException, MojoFailureException {
    	citrusClassPaths = new ArrayList<URL>();
    	addFileToClassPath(testOutputDirectory);
    	addFileToClassPath(classesDirectory);
    	
    	addDependenciesToSystemClassPath();
    	
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
    				new PritestTester(classLoader, priorityLists.get(technique0[0]), getLog(), reporter).execute();
    			}
    			if(Arrays.asList(technique0).contains(10)) {
    				priorityLists.put(10, getOptimizedPriorityList(reporter.getMeasureList().getList()));
    			}
    			priorityListsToAPFD(priorityLists, reporter.getMeasureList().getList());
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    	}
    	else {
    		PriorityList2 priorityListService = new PriorityList2(
    				new OnlineClassService(citrusTechniqueUrl, techniqueNumber), 
    				new LocalClassService(testOutputDirectory), 
    				basedir, techniqueNumber, testSourceDirectory, sourceDirectory);
    		List<String> priorityList = new ArrayList<String>();
    		try {
    			priorityList.addAll(priorityListService.getPriorityList());
    			new PritestTester(classLoader, priorityList, getLog(), reporter).execute();
    			APFD apfd = new APFD(reporter.getMeasureList());
    			APFDHelper.outputAPFDToFile(apfd, techniqueNumber);
    			XYSeries plotData = APFDHelper.getXYSeries(techniqueNumber, reporter.getMeasureList().getList());
    			APFDHelper.outputSingleAPFDGraphToFile(plotData, techniqueNumber);
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
    	if(reporter.hasFailures()) {
    		throw new org.apache.maven.plugin.MojoFailureException("Has failing tests");
    	}
    }

    private void addDependenciesToSystemClassPath() {
    	List<String> dependencies = new ArrayList<String>();
    	
    	addDependenciesToList(dependencies, testClasspathElements);
    	addDependenciesToList(dependencies, compileClasspathElements);
    	
    	addWebInfClassesToList(dependencies);
    	
    	addDependenciesToSystemClassPath(dependencies);
	}
    
    private void addWebInfClassesToList(List<String> dependencies) {
		if (mavenProject.getPackaging().equals("war")) {
			String webInfClasses = "target/" + mavenProject.getArtifactId() + "-" + mavenProject.getVersion() + "/WEB-INF/classes/";
			File dependency = new File(webInfClasses);
			if (dependency.exists()) {
				getLog().info("Adds WEB-INF: " + dependency.getAbsolutePath());
				dependencies.add(dependency.getAbsolutePath());
			}
		}
	}


	private void addDependenciesToSystemClassPath(List<String> dependencies) {
        StringBuilder sb = new StringBuilder();
    	for (String cpElement : dependencies) {
    		sb.append(cpElement).append(File.pathSeparatorChar);
    	}
    	
    	System.setProperty("java.class.path", sb.toString());
    	
    	getLog().info("CLASSPATH: " + System.getProperty("java.class.path"));
	}
    
    private void addDependenciesToList(List<String> dependencies, List<String> classPathElements) {
		for (String cpElement : classPathElements) {
    		if (!dependencies.contains(cpElement)) {
    			dependencies.add(cpElement);
    		}
    	}
	}

	private List<String> getOptimizedPriorityList(List<Measure> list) {
		Collections.sort(list);
		Collections.reverse(list);
		List<String> optimizedList = new ArrayList<String>();
		for(Measure measure : list){
			optimizedList.add(measure.getSource());
		}
		return optimizedList;
	}


	private void priorityListsToAPFD(Map<Integer, List<String>> priorityLists, List<Measure> measureList) throws IOException {
		XYSeriesCollection plotdata = new XYSeriesCollection();
		for (Integer localTechniqueNumber : priorityLists.keySet()){
			List<String> localPriorityList = priorityLists.get(localTechniqueNumber);
			List<Measure> localMeasureList = APFDHelper.sortMeasureListBySource(localPriorityList, measureList);
			XYSeries localPlotdata = APFDHelper.getXYSeries(localTechniqueNumber, localMeasureList);
			plotdata.addSeries(localPlotdata);
			
			APFD apfd = new APFD(new MeasureList(localMeasureList));
			APFDHelper.outputAPFDToFile(apfd, localTechniqueNumber);
			APFDHelper.outputSingleAPFDGraphToFile(localPlotdata, localTechniqueNumber);
		}
		APFDHelper.outputAPFDGraphToFile(plotdata);
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
