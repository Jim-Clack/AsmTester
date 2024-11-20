package com.jc;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * This is the guy that does special things to a class
 */
public class SpecialClassAdaptor extends ClassVisitor {

    private final String targetClassFullName;
    private boolean isClassOfInterest = false;

    public SpecialClassAdaptor(ClassWriter cw, String targetClassFullName) {
        super(ASM9, cw);
        this.targetClassFullName = targetClassFullName;
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        isClassOfInterest = name.equals(targetClassFullName);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if(isClassOfInterest && !name.startsWith("<") && mv != null) {
            // System.out.println("Changing Method " + targetClassFullName + "." + name);
            return new SpecialMethodAdaptor(name, mv);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        cv.visitEnd();
        // Just for testing
        /*
        if(isClassOfInterest) {
            try (FileOutputStream stream = new FileOutputStream(
                    targetClassFullName.replaceAll("\\\\", "-").replaceAll("/", "-") + ".class")) {
                stream.write(((ClassWriter)cv).toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        */
    }
}
