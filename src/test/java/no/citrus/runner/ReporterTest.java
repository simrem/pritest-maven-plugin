package no.citrus.runner;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import no.citrus.restapi.model.Measure;
import no.citrus.runner.junit.reporter.Reporter;

import com.sun.jersey.api.client.ClientHandlerException;


public class ReporterTest {
	
	public void reporterTest(){
		MyReporter reporter = new MyReporter("", new ArrayList<Mesure>());
	}
	
	private class MyReporter extends Reporter{
		
		public MyReporter(String reportURL, List<Measure> measureList) {
			super(reportURL, measureList);
			// TODO Auto-generated constructor stub
		}

		public String getXML() throws ClientHandlerException, JAXBException{
			return this.getMeasureListAsXML().toString();
		}
	}

}
