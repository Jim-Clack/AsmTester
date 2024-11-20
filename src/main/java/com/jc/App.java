package com.jc;

// Preparation
//  Added 2 Maven libraries (IntelliJ may already have done this for you)
//    org.ow2.asm:asm-9.7.1
//    org.ow2.asm:asm-util-9.7
//  Add an artifact to the project structure: JAR
//    Record the JAR path: ... out\artifacts\AsmTester_jar\AsmTester.jar
//    Check to box to include it in the project build
//  Adjust your run/debug configuration...
//    Add JAR path to your -VM path (change backslashes to forward slashes)
//      -javaagent:./out/artifacts/AsmTester_jar/AsmTester.jar
//  Edit the META-INF/MANIFEST.MF file, adding the following entries:
//    Premain-Class: com.jc.PreMain
//    Can-Redefine-Classes: true
//    Can-Retransform-Classes: true
// How this was created:
//   Compiled TestClass with the println statement NOT commented-out
//   Use ASMifier to look at the instrumentation that creates the println
//   Commented-out that println, or you can remove it entirely
//   Put the instrumentation code SpecialMethodAdaptor and customized it
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
