package de.timgerdes.remote.websocket;

import org.json.JSONObject;

import javax.websocket.RemoteEndpoint;
import java.io.IOException;

public class Request {

    private RemoteEndpoint.Basic basic;
    private MessageStorage storage;
    private int id;

    public Request(RemoteEndpoint.Basic basic, MessageStorage storage) {
        this.basic = basic;
        this.storage = storage;
    }

    public void send(int id, String message) {
        this.id = id;

        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        params.put("expression", message);

        json.put("id", id);
        json.put("method", "Runtime.evaluate"); //see https://developer.chrome.com/devtools/docs/protocol/tot/runtime#command-evaluate
        json.put("params", params);

        try {
            basic.sendText(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response getResponse() {
        Response response = new Response(id, storage);
        response.search(); //looks up for a response, because they need some time

        return response;
    }

}
