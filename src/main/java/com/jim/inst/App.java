package com.jim.inst;

/*
 * @apiNote App --------------------------------------------------------------
 * How does Spring do its magic? How does Lombok work internally? How
 * do Scala and Kotlin generate bytecodes? How do Scala and Kotlin Compilers
 * create executables? How do so many Java tools accomplish so much?
 * The answer is "instrumentation."
 * ----------------
 * Intro to Instrumentation Exercise...
 *   Review App+TestClass, then run to be amazed - it injects println's.
 *   Look at SpecialMethodVisitor to see how a println()'s get injected.
 *   None of this works if you don't do all the proper preparations...
 * ----------------
 * Preparation: (The IDE may already have done some of these for you)
 *   Add this library to your project:
 *     org.ow2.asm:asm-util:9.7.1
 *     note: The latest update of IntelliJ cannot find/list this, but you can
 *           just type/paste it in and it will be okay. (IntelliJ bug/setting?)
 *   Add an artifact/output to the project structure: JAR
 *     Check the box to include it in the project build
 *     Make note of the JAR path <...>\out\artifacts\AsmTester_jar\AsmTester.jar
 *   Adjust your run/debug configuration...
 *     *Add JAR path to your -VM path (change backslashes to forward slashes)
 *       -javaagent:./out/artifacts/AsmTester_jar/AsmTester.jar
 *   In the META-INF/MANIFEST.MF file, add the following entries:
 *     Premain-Class: com.jim.inst.PreMain
 *     Can-Redefine-Classes: true
 *     Can-Retransform-Classes: true
 * ----------------
 * How this was created:
 *   Compiled TestClass with the println statement NOT commented-out
 *   Used ASMifier or View:ShowByteCodes to see the println byte codes
 *   Commented-out that println (or you can remove it entirely)
 *   Put customized code into SpecialClassAdaptor/SpecialMethodAdaptor
 * ----------------
 * Learning more about Instrumentation and ASM:
 *   Research the Visitor and Chain-of-Responsibilities design patterns.
 *   In your IDE, compile a println() then Menu > View > Show Bytecodes.
 *   Classes must be transformed before main runs, hence PreMain.premain().
 *   References: asm.ow2.io -and- org.objectweb.asm.Opcodes.java
 */
public class App
{
    public static void main(String[] args) {
        // For this test, we injected a println(methodName) in each method:
        System.out.println("########## main is constructing TestClass");
        TestClass tc = new TestClass();
        System.out.println("########## main is calling tc. stat(), getX(), and setX()");
        TestClass.stat();
        tc.setX(tc.getX());
        // for testing...
        System.out.println("########## list of classes reviewed:");
        PreMain.getClasses().forEach(System.out::println);
    }

}
