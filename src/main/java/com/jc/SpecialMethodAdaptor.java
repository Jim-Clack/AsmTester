package com.jc;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

public class SpecialMethodAdaptor extends MethodVisitor {

    private final String methodName;
    private boolean isReadyForCode = false;
    private boolean wasChanged = false;

    public SpecialMethodAdaptor(String name, MethodVisitor mv) {
        super(ASM9, mv);
        this.methodName = name;
    }

    public void visitCode() {
        mv.visitCode();
        isReadyForCode = true;
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        mv.visitLineNumber(line, start);
        if (isReadyForCode) {
            // These bytecodes do this: System.out.println(methodName)
            // I used ASMifier to figure these out
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            isReadyForCode = false;
            wasChanged = true;
        }
    }

    public void visitMaxs(final int maxStack, final int maxLocals) {
        if(wasChanged) {
            // Make sure we accommodate space on the stack for the field and methodName
            mv.visitMaxs(maxStack + 2, maxLocals);
            wasChanged = false;
        }
    }

}