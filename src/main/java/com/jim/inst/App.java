package com.jim.inst;

/**
 * How does Spring do its magic? How does Lombok work internally? How
 * do Scala and Kotlin generate bytecodes? How do so many Java tools
 * accomplish so much? The answer is "instrumentation."

 * Intro to Instrumentation Exercise...
 *   Review App & TestClass, then run to be amazed - injects println()'s.
 *   Look at SpecialMethodVisitor to see how a println()'s get injected.
 *   None of this works if you don't do all the proper preparations...

 * Preparation: (The IDE may already have done some of these for you)
 *   Add 2 Maven libraries
 *     org.ow2.asm:asm-9.7.1
 *     org.ow2.asm:asm-util-9.7.1
 *   Add an artifact/output to the project structure: JAR
 *     Check the box to include it in the project build
 *     Record the JAR path* ... out\artifacts\AsmTester_jar\AsmTester.jar
 *   Adjust your run/debug configuration...
 *     *Add JAR path to your -VM path (change backslashes to forward slashes)
 *       -javaagent:./out/artifacts/AsmTester_jar/AsmTester.jar
 *   In the META-INF/MANIFEST.MF file, add the following entries:
 *     Premain-Class: com.jim.inst.PreMain
 *     Can-Redefine-Classes: true
 *     Can-Retransform-Classes: true

 * How this was created:
 *   Compiled TestClass with the println statement NOT commented-out
 *   Used ASMifier to look at the instrumentation that creates the println
 *   Commented-out that println, or you can remove it entirely
 *   Put customized code into SpecialClassAdaptor/SpecialMethodAdaptor

 * Learning more about Instrumentation and ASM:
 *   Research the Visitor and Chain-of-Responsibilities design patterns.
 *   In your IDE, compile a println() then Menu > View > Show Bytecodes.
 *   Classes must be transformed before main runs, hence PreMain.premain().
 *   References: asm.ow2.io -and- org.objectweb.asm.Opcodes.java
 */
public class App
{
    public static void main(String[] args) {
        TestClass tc = new TestClass();
        // For this test, we injected a println(methodName) in each method:
        TestClass.stat();
        tc.setX(tc.getX());
    }
}
