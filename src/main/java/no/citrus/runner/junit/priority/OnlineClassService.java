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
	
	public OnlineClassService(String techniqueURL){
		this.techniqueURL = techniqueURL;
	}
	
	@Override
	public List<String> getClassList() throws Exception, JSONException, ConnectException {
        Client c = Client.create();
        WebResource r = c.resource(techniqueURL);
        String result = r.get(String.class);

        JSONArray jsonArray = new JSONArray(result);
        List<String> data = new ArrayList<String>();
        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            data.add(jsonArray.getString(i).replace(".java", ""));
        }
        return data;
	}

}
