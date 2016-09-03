package de.timgerdes.remote.websocket;

import de.timgerdes.remote.json.JSONParser;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageStorage {

    private HashMap<Integer, String> storage = new HashMap<>();

    public HashMap<Integer, String> getStorage() {
        return storage;
    }

    public void addMessage(String message) {
        JSONParser parser = new JSONParser();
        parser.readString(message);
        JSONObject json = parser.parseJSON();

        if(!json.has("id"))
            return;

        storage.put(json.getInt("id"), message);
    }

    public String getMessage(int id) {
        return storage.get(id);
    }

    public boolean hasMessage(int id) {
        return storage.containsKey(id);
    }

}
