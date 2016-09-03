package de.timgerdes.remote;

import de.timgerdes.remote.websocket.ClientWebSocket;
import de.timgerdes.remote.websocket.Request;

import javax.websocket.DeploymentException;
import java.net.URISyntaxException;

public interface RemoteBrowserController {

    /**
     * @return int Port for remote debugging
     */
    int getPort();

    /**
     * Starts following services:
     * - Browser
     * - Remote Debugging
     * - Websocket
     */
    void startBrowser();

    /**
     * Stops following services:
     * - Browser
     * - Remote Debugging (auto)
     * - Websocket (auto) @see de.timgerdes.remote.websocket.ClientWebSocket#onClose(Session session, CloseReason closeReason)
     */
    void stopBrowser();

    /**
     * Initialize the websocket
     */
    void setupWebSocket();

    /**
     * @return ClientWebSocket current socket
     */
    ClientWebSocket getWebSocket();

    /**
     * Checks if client can reach websocket
     * @return boolean can reach
     */
    boolean isReachable();

    /**
     * Executes javascript code in browser
     * @param execute Javascript
     */
    Request execute(String execute);

}
