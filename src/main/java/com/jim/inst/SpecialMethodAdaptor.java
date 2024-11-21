package com.jim.inst;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

public class SpecialMethodAdaptor extends MethodVisitor {

    private final String methodName;
    private boolean isReadyForCode = false;
    private boolean wasChanged = false;

    /**
     * Ctor.
     * @param name name of method.
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
        isReadyForCode = true;
    }

    /**
     * This gets called for each line-number opcode, as a step in chain-of-command.
     * If we have not yet injected a println(), then we do it after the first time
     * a linenumber is encountered, so as to ensure there is a valid linenumber.
     * @param line a line number. This number refers to the source file from which the class was
     *     compiled.
     * @param start the first instruction corresponding to this line number.
     */
    @Override
    public void visitLineNumber(final int line, final Label start) {
        mv.visitLineNumber(line, start);
        if (isReadyForCode) {
            // These bytecodes are equivalent this: System.out.println(methodName)
            // You can use ASMifier to figure these out or you can Show Bytecodes in
            // your IDE then refer to Opcodes.java to see what visit method to use.
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            isReadyForCode = false;
            wasChanged = true;
        }
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