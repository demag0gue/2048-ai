package de.timgerdes.remote.wait;

public class WaitThread extends Thread {

    private WaitCondition condition;
    private long sleep;

    public WaitThread(WaitCondition condition, long sleep) {
        this.condition = condition;
        this.sleep = sleep;
    }

    @Override
    public void run() {
        while(!condition.isFulfilled())
            if(sleep > 0)
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
    }

}
