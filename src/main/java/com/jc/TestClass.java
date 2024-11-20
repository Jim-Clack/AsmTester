package com.jc;

public class TestClass {

    public static int s = 0;
    private int x;

    public static void stat() {
        // System.out.println("stat"); // for testing
        s = 1;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }
}
