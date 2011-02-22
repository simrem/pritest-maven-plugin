package no.citrus.runner.junit.priority;

import java.net.ConnectException;
import java.util.List;

import org.codehaus.jettison.json.JSONException;

public interface ClassService {
	public List<String> getClassList() throws Exception, JSONException, ConnectException;
}
