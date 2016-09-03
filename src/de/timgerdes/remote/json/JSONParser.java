package de.timgerdes.remote.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONParser {

    private String json;

    public void readFromUrl(String urlString) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            json = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void readString(String json) {
        this.json = json;
    }

    public JSONArray parseArray() {
        return new JSONArray(json);
    }

    public JSONObject parseJSON() {
        return new JSONObject(json);
    }

    public Object find(JSONFilter filter) {
        return filter.find((json.startsWith("[") ? parseArray() : parseJSON())); //no loop, since different depths and types
    }

}
