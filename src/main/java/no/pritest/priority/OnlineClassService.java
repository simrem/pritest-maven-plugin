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

package no.pritest.priority;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

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
