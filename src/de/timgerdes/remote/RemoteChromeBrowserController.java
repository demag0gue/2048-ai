package de.timgerdes.remote;

import de.timgerdes.remote.json.JSONPageFilter;
import de.timgerdes.remote.json.JSONParser;
import de.timgerdes.remote.wait.WaitThread;
import de.timgerdes.remote.websocket.ClientWebSocket;
import de.timgerdes.remote.websocket.Request;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class RemoteChromeBrowserController implements RemoteBrowserController {

    private int port = 9222;
    private ClientWebSocket client = new ClientWebSocket();

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void startBrowser() {
        try {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome https://gabrielecirulli.github.io/2048/ --remote-debugging-port=" + getPort()});
            WaitThread wait = new WaitThread(() -> {try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("127.0.0.1", getPort()), 50);
                return true;
            } catch (IOException e) {
                return false;
            }}, 0);
            wait.run();
            wait.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopBrowser() {
        try {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "taskkill /IM chrome.exe /F"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupWebSocket() {
        JSONParser parser = new JSONParser();
        parser.readFromUrl("http://localhost:" + getPort() + "/json");
        String webSocket = (String) parser.find(new JSONPageFilter());

        ClientManager cm = ClientManager.createClient();

        try {
            cm.connectToServer(client, new URI(webSocket));
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClientWebSocket getWebSocket() {
        return client;
    }

    @Override
    public boolean isReachable() {
        return client.isConnected(); //TODO this part is actually buggy
    }

    @Override
    public Request execute(String execute) {
        if(!isReachable())
            return null;
        return getWebSocket().request(execute);
    }
}
