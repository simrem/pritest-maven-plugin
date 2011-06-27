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

package no.citrus.runner.junit.test;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.citrus.restapi.model.Measure;
import no.citrus.runner.junit.reporter.Reporter;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

public class CitrusTester extends RunListener {
    private final URLClassLoader classLoader;
    private final List<String> testOrder;
    private final Reporter reporter;
    private final Log log;
    private Measure measure;

    public CitrusTester(URLClassLoader classLoader, List<String> testOrder, Log log, Reporter reporter) {
        this.classLoader = classLoader;
        this.testOrder = testOrder;
        this.log = log;
        this.reporter = reporter;
    }

    public boolean execute() throws InitializationError {
        RunNotifier runnerNotifier = new RunNotifier();
        runnerNotifier.addListener(this);
        
        for (String file : testOrder) {
        	
			try {
				measure = new Measure();
	        	measure.setChildren(new ArrayList<Measure>());
	        	measure.setSource(file);
	        	measure.setDate(new Date(System.currentTimeMillis()));
	        	measure.setNumOfFails(0);
	        	long time = System.currentTimeMillis();
	            
	            Class<?> aClass = this.classLoader.loadClass(file);
	            
	            boolean isRunWithPresent = false;
	            if (aClass.isAnnotationPresent(RunWith.class)) {
	            	isRunWithPresent = true;
	            }
	            
	            boolean isJunit4Present = false;
	            for(Method m : aClass.getDeclaredMethods()){
	            	if(m.isAnnotationPresent(Test.class)){
	            		isJunit4Present = true;
	            	}
	            }
	            if(isJunit4Present && !isRunWithPresent){
	            	log.info("Running test: " + file);
	            	JUnit4 runner = new JUnit4(aClass);
//	            	BlockJUnit4ClassRunner runner = new BlockJUnit4ClassRunner(aClass);
	            	runner.run(runnerNotifier);
	            	
	            	measure.setValue(System.currentTimeMillis() - time);
	                reporter.addMeasure(measure);
	            }
	            else{
	            	log.warn("Not junit4 class or RunWith present: " + file);
	            }
			} catch (ClassNotFoundException e) {
				log.debug(file + " not found");
			}
        }
        return true;
    }

	@Override
	public void testFailure(Failure failure) throws Exception {
		super.testFailure(failure);
		log.warn("Test failed: " + failure.getTestHeader() + failure.getMessage() + "\n"
				+ failure.getDescription() + "\n"
				+ failure.getTrace() + "\n"
				+ failure.getException());
		
		Measure failedMeasure = new Measure();
		failedMeasure.setFailed(true);
		failedMeasure.setName(failure.getDescription().getMethodName());
		failedMeasure.setSource(failure.getDescription().getClassName());
		failedMeasure.setDate(new Date(System.currentTimeMillis()));
		measure.getChildren().add(failedMeasure);
		measure.setNumOfFails(measure.getNumOfFails() +1);
	}
	
}
