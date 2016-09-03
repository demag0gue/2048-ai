package de.timgerdes.remote.json;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONPageFilter implements JSONFilter {

    @Override
    public Object find(Object object) {
        if(!(object instanceof JSONArray))
            return null;

        JSONArray json = (JSONArray) object;

        for(int i = 0; i < json.length(); i++) {
            JSONObject page = json.getJSONObject(i);

            if(page.getString("url").equals("https://gabrielecirulli.github.io/2048/"))
                return page.getString("webSocketDebuggerUrl");
        }

        return null;
    }

}
