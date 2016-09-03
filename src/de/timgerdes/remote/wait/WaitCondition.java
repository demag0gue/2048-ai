package de.timgerdes.remote.wait;

@FunctionalInterface
public interface WaitCondition {

    /**
     * Checks if the condition is fullfilled
     * This is only useful by using the WaitThread
     * @return
     */
    boolean isFulfilled();

}
