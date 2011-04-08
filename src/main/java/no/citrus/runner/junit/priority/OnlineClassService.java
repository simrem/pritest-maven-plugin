package no.citrus.runner.junit.priority;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OnlineClassService implements ClassService {

	private final String techniqueURL;
	private final int techniqueNumber;

	public OnlineClassService(String techniqueURL, int techniqueNumber){
		this.techniqueURL = techniqueURL;
		this.techniqueNumber = techniqueNumber;
	}
	
	@Override
	public List<String> getClassList() throws Exception, JSONException, ConnectException {
        Client c = Client.create();
        WebResource r = c.resource(techniqueURL + techniqueNumber);
        String result = r.get(String.class);

        JSONArray jsonArray = new JSONArray(result);
        List<String> data = new ArrayList<String>();
        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            data.add(jsonArray.getString(i).replace(".java", ""));
        }
        return data;
	}
	
	public String getTechniqueURL() {
		return techniqueURL;
	}
}
