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

package no.pritest.reporter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import no.pritest.restapi.model.Measure;
import no.pritest.restapi.model.MeasureList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;

public class Reporter {
	
	private MeasureList measureList;
	private final String reportURL;
	private boolean hasFailures;
	
	public Reporter(String reportURL, List<Measure> measureList){
		this.reportURL = reportURL;
		this.measureList = new MeasureList(measureList);
		hasFailures = false;
	}
	
	public boolean addMeasure(Measure measure){
		if(measure.numOfFails > 0) {
			hasFailures = true;
		}
		return measureList.getList().add(measure);
	}
	
	public boolean hasFailures(){
		return hasFailures;
	}
//	public void outputAPFDToFile(String directory, String filename) throws IOException{
//		APFD apfd = new APFD(measureList);
//		apfd.outputToFile(directory, filename);
//	}
	
	public void sendReport() throws ClientHandlerException, JAXBException{
		StringBuffer sb = getMeasureListAsXML();
		//System.out.println(sb.toString());
        System.out.println("Sending report to " + reportURL);
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
	public MeasureList getMeasureList(){
		return measureList;
	}
}
