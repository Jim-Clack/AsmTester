package com.jim.inst;

/**
 * Default target class for testing. Note the tha methods do NOT
 * contain any println() statements. They will be injected.
 */
public class TestClass {

    public static int s = 0;
    private int x;

    public static void stat() {
        // System.out.println("stat"); // for testing only
        s = 1;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }
}
