package de.timgerdes.remote.websocket;

import de.timgerdes.remote.json.JSONParser;
import de.timgerdes.remote.wait.WaitThread;
import org.json.JSONObject;

public class Response {

    private MessageStorage storage;
    private String message;
    private int id;

    public Response(int id, MessageStorage storage) {
        this.id = id;
        this.storage = storage;
    }

    public void search() {
        int attempt = 0;
        WaitThread wait = new WaitThread(() -> {
            if(attempt == 4) {
                message = "NOT_FOUND";
                return true;
            }
            if(storage.hasMessage(id)) {
                message = storage.getMessage(id);
                return true;
            }

            return false;
        }, 30L);

        wait.run();
        try {
            wait.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getJSON() {
        if(getMessage().equals("NOT_FOUND"))
            return null;
        JSONParser parser = new JSONParser();
        parser.readString(getMessage());

        return parser.parseJSON().getJSONObject("result"); //actually only this JSONObject is important for us
    }

    public boolean hasError() {
        return getJSON() == null || getJSON().getBoolean("wasThrown");
    }

}
