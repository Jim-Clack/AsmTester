package com.jim.inst;

import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

/**
 * @apiNote SpecialMethodAdaptor ---------------------------------------------
 * This is the guy that does special things to a method.
 */
public class SpecialMethodAdaptor extends MethodVisitor {

    private final String methodName;
    private boolean wasChanged = false;

    /**
     * Ctor.
     * @param name name of method to modify
     * @param mv method visitor as returned from downstream.
     */
    public SpecialMethodAdaptor(String name, MethodVisitor mv) {
        super(ASM9, mv);
        this.methodName = name;
    }

    /**
     * This gets called for each method, as a step in chain-of-command.
     */
    public void visitCode() {
        mv.visitCode();
        // These bytecodes are equivalent this: System.out.println(methodName)
        // You can use ASMifier to figure these out or you can Show Bytecodes in
        // your IDE then refer to Opcodes.java to see what visit method to use.
        // The visitXxx() methods are documented in comments in Opcodes.java.
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(methodName);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        wasChanged = true;
    }

    /**
     * This gets called after each method is processed, as a step in chain-of-command.
     * @param maxStack maximum stack size of the method.
     * @param maxLocals maximum number of local variables for the method.
     */
    public void visitMaxs(final int maxStack, final int maxLocals) {
        if(wasChanged) {
            // Make sure we accommodate space on the stack for field and methodName
            mv.visitMaxs(maxStack + 2, maxLocals);
            wasChanged = false;
        }
    }

}