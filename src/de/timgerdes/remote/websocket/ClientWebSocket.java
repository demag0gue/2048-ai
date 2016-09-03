package de.timgerdes.remote.websocket;

import javax.websocket.*;

@ClientEndpoint
public class ClientWebSocket extends MessageStorage {

    private Session session = null;
    private int id = 0; //increases with every request

    @OnOpen
    public void onOpen(Session session) {
        if(isConnected()) //make sure we only have one session
            return;
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        addMessage(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        this.session = null;
    }

    public Request request(String message) {
        Request request = new Request(getCurrentSession().getBasicRemote(), this);
        request.send(id, message);
        id++;

        return request;
    }

    public Session getCurrentSession() {
        return this.session;
    }

    public boolean isConnected() {
        return this.session != null; //@see onClose()
    }

}
