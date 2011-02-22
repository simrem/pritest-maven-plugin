package no.citrus.runner.junit.reporter;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import no.citrus.restapi.model.Measure;
import no.citrus.restapi.model.MeasureList;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

public class Reporter {
	
	private MeasureList measureList;
	private final String reportURL;
	
	public Reporter(String reportURL, List<Measure> measureList){
		this.reportURL = reportURL;
		this.measureList = new MeasureList(measureList);
	}
	
	public boolean addMeasure(Measure measure){
		return measureList.getList().add(measure);
	}
	
	public void sendReport() throws ClientHandlerException, JAXBException{
		StringBuffer sb = getMeasureListAsXML();
		System.out.println(sb.toString());
		Client c = Client.create();  
		WebResource r = c.resource(reportURL);  
		r.type("application/xml").post(String.class, sb.toString());
	}
	
	public StringBuffer getMeasureListAsXML() throws ClientHandlerException, JAXBException{
		JAXBContext context = JAXBContext.newInstance(MeasureList.class, Measure.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();

		marshaller.marshal(measureList, sw);
		
		return sw.getBuffer();
	}
}
