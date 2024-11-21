package com.jim.inst;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

/**
 * This is the guy that does special things to a class.
 */
public class SpecialClassAdaptor extends ClassVisitor {

    private final String targetClassFullName;
    private boolean isClassOfInterest = false;

    /**
     * Ctor.
     * @param cw a ClassWriter
     * @param targetClassFullName name of the class that we want to modify
     */
    public SpecialClassAdaptor(ClassWriter cw, String targetClassFullName) {
        super(ASM9, cw);
        this.targetClassFullName = targetClassFullName;
    }

    /**
     * This gets called for each Class, as a step in chain-of-command
     * @param version the class version. The minor version is stored in the 16 most significant bits,
     *     and the major version in the 16 least significant bits.
     * @param access the class's access flags (see {@link Opcodes})
     * @param name the internal name of the class (see {@link Type#getInternalName()}).
     * @param signature the signature of this class. May be {@literal null} if the class is not a
     *     generic one, and does not extend or implement generic classes or interfaces.
     * @param superName the internal of name of the super class (see {@link Type#getInternalName()}).
     *     For interfaces, the super class is {@link Object}. May be {@literal null}, but only for the
     *     {@link Object} class.
     * @param interfaces the internal names of the class's interfaces (see {@link
     *     Type#getInternalName()}). May be {@literal null}.
     */
    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        isClassOfInterest = name.equals(targetClassFullName);
    }

    /**
     * This gets called for each Method, as a step in chain-of-command
     * @param access the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the method is synthetic and/or deprecated.
     * @param name the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature the method's signature. May be {@literal null} if the method parameters,
     *     return type and exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *     Type#getInternalName()}). May be {@literal null}.
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if(isClassOfInterest && !name.startsWith("<") && mv != null) {
            // System.out.println("Changing Method " + targetClassFullName + "." + name);
            return new SpecialMethodAdaptor(name, mv);
        }
        return mv;
    }

    /**
     * Called at the end of processing, a step in the chain-of-command
     */
    @Override
    public void visitEnd() {
        cv.visitEnd();
        // Only for testing
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
