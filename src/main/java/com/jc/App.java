package com.jc;

public class App
{
    public static void main( String[] args ) {
        // For this test, we insert a println(methodName) in each method, then call them
        TestClass tc = new TestClass();
        TestClass.stat();
        tc.setX(0);
        int a = tc.getX();
    }
}
