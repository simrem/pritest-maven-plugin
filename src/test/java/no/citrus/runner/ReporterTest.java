package no.citrus.runner;

import com.sun.jersey.api.client.ClientHandlerException;
import no.citrus.restapi.model.Measure;
import no.citrus.runner.junit.reporter.Reporter;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static no.citrus.runner.ReporterTest.IsNotADate.isNotADate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.junit.Assert.assertThat;


public class ReporterTest {

    @Test
    public void test_functionality_of_reporter(){
        MyReporter reporter = new MyReporter("", new ArrayList<Measure>());
        Measure m = new Measure();
        m.setChildren(new ArrayList<Measure>());
        m.setSource("testfile");
        m.setDate(new Date(System.currentTimeMillis()));
        reporter.addMeasure(m);
        Document testResult = null;
        try {
            testResult = parseXmlStringToDocument(reporter.getXML());
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        assertThat(testResult, hasXPath("/measures"));
        assertThat(testResult, hasXPath("/measures/measure"));
        assertThat(testResult, hasXPath("/measures/measure/children", equalTo("")));
        assertThat(testResult, hasXPath("/measures/measure/value", equalTo("0.0")));
        assertThat(testResult, hasXPath("/measures/measure/date", isNotADate()));
        assertThat(testResult, hasXPath("/measures/measure[1]/failed", equalTo("false")));
        assertThat(testResult, hasXPath("/measures/measure[1]/source", equalTo("testfile")));
    }


    private Document parseXmlStringToDocument(String expectedXmlString) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new ByteArrayInputStream(expectedXmlString.getBytes()));
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

    public static class IsNotADate extends TypeSafeMatcher<String> {

        @Override
        public boolean matchesSafely(String date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                sdf.parse(date);
                return true;
            } catch (ParseException e) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return false;
            }
        }

        public void describeTo(Description description) {
            description.appendText("Is not a Date");
        }

        @Factory
        public static <T> Matcher<String> isNotADate() {
            return new IsNotADate();
        }

    }

}
