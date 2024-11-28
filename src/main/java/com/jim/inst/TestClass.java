package com.jim.inst;

/**
 * @apiNote TestClass --------------------------------------------------------
 * Default target class for testing. Note that the methods do NOT
 * contain any println() statements. The println's will be injected.
 */
@InjectPrintlnMethodNames
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
