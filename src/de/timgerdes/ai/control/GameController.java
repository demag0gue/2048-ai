package de.timgerdes.ai.control;

import de.timgerdes.remote.RemoteBrowserController;
import de.timgerdes.remote.wait.WaitThread;
import de.timgerdes.remote.websocket.Request;
import de.timgerdes.remote.websocket.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameController {

    private RemoteBrowserController remote;

    public GameController(RemoteBrowserController remote) {
        this.remote = remote;
    }

    /**
     * Create a new instance for easy access
     * To restart the game see restart()
     */
    public void setup() {
        WaitThread wait = new WaitThread(() -> {
            Response response = execute("GameManager").getResponse();
            return !response.hasError();
        }, 1000L); //need to wait that variable is initialized and make sure the javascript is loaded
        wait.run();
        try {
            wait.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        execute("var _func_tmp=GameManager.prototype.isGameTerminated;GameManager.prototype.isGameTerminated=function(){return GameManager._instance=this,!0};"); //cache isGameTerminated and replace it to create '_instance'
        fireKeyEvent("keydown", 38); //trigger isGameTerminated by moving the tiles
        fireKeyEvent("keyup", 38);
        execute("GameManager.prototype.isGameTerminated = _func_tmp;"); //reset with cached function
    }

    /**
     * Restarts the game
     */
    public void restart() {
        fireKeyEvent("keydown", 82);
        fireKeyEvent("keyup", 82);
    }

    /**
     * Returns current board
     * @return String JSON
     */
    public Board getBoard() {
        String response =  execute("JSON.stringify(GameManager._instance.grid)").getResponse().getJSON().getJSONObject("result").getString("value");
        JSONObject json = new JSONObject(response);
        JSONArray grid = json.getJSONArray("cells");

        int[][] board = new int[4][4];

        for(int x = 0; x < grid.length(); x++) { //the web applications sorts the grid by columns
            JSONArray column = grid.getJSONArray(x);
            for(int y = 0; y < column.length(); y++) {
                Object raw = column.opt(y);
                int value = 0;
                if(raw != null && raw instanceof JSONObject) {
                    JSONObject cell = (JSONObject) raw;
                    value= cell.getInt("value");
                }

                board[x][y] = value;

            }
        }

        Board b = new Board(board);
        b.setScore(getScore());

        return b;
    }

    /**
     * Returns the current score
     * @return int score
     */
    public int  getScore() {
        return execute("GameManager._instance.score").getResponse().getJSON().getJSONObject("result").getInt("value");
    }

    /**
     * Returns the status by the following Strings:
     * ended
     * won
     * running
     * @return String status
     */
    public String getStatus() {
        return execute("if(GameManager._instance.over) {\"ended\"} else if(GameManager._instance.won && !GameManager._instance.keepPlaying) {\"won\"} else {\"running\"}").getResponse().getJSON().getJSONObject("result").getString("value");
    }

    /**
     * Move tiles
     * @param direction
     */
    public void move(Direction direction) {
        execute("GameManager._instance.move(" + direction.getCode() + ")");
    }

    /**
     * Emulate keyEvents via javascript
     * @param action {keydown, keyup}
     * @param key javascript keycode
     * @return Request
     */
    private Request fireKeyEvent(String action, int key) {
        return remote.execute("var keyboardEvent=document.createEventObject?document.createEventObject():document.createEvent(\"Events\");keyboardEvent.initEvent&&keyboardEvent.initEvent(\"" + action +"\",!0,!0),keyboardEvent.keyCode=" + key + ",keyboardEvent.which=" + key + ";var element=document.body||document;element.dispatchEvent?element.dispatchEvent(keyboardEvent):element.fireEvent(\"on" + action + "\",keyboardEvent);");
    }

    /**
     * @see RemoteBrowserController#execute(String)
     * @param execute
     * @return Request
     */
    private Request execute(String execute) {
        return remote.execute(execute);
    }

    /**
     * Returns used RemoteBrowserController
     * @return
     */
    public RemoteBrowserController getRemote() {
        return remote;
    }

}
