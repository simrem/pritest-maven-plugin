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
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
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

    public boolean execute() throws ClassNotFoundException, InitializationError {
        RunNotifier runnerNotifier = new RunNotifier();
        runnerNotifier.addListener(this);
        
        for (String file : testOrder) {
        	
        	measure = new Measure();
        	measure.setChildren(new ArrayList<Measure>());
        	measure.setSource(file);
        	measure.setDate(new Date(System.currentTimeMillis()));
        	long time = System.currentTimeMillis();
            
        	log.info("Loading file: " + file);
            Class<?> aClass = this.classLoader.loadClass(file);
            boolean isJunit4Present = false;
            for(Method m : aClass.getDeclaredMethods()){
            	if(m.isAnnotationPresent(Test.class)){
            		isJunit4Present = true;
            	}
            }
            if(isJunit4Present){
            	log.info("Running file: " + file);
            	BlockJUnit4ClassRunner runner = new BlockJUnit4ClassRunner(aClass);
            	runner.run(runnerNotifier);
            	
            	measure.setValue(System.currentTimeMillis() - time);
                reporter.addMeasure(measure);
            }
            else{
            	log.warn("Not junit4 class: " + file);
            }
        }
        return true;
    }

	@Override
	public void testFailure(Failure failure) throws Exception {
		super.testFailure(failure);
		log.warn("Test failed: " + failure.getTestHeader() + failure.getMessage());
		
		Measure failedMeasure = new Measure();
		failedMeasure.setFailed(true);
		failedMeasure.setName(failure.getDescription().getMethodName());
		failedMeasure.setSource(failure.getDescription().getClassName());
		failedMeasure.setDate(new Date(System.currentTimeMillis()));
		measure.getChildren().add(failedMeasure);
	}
	
}
