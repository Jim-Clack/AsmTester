package com.jim.inst;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

/**
 * @apiNote SpecialClassAdaptor ----------------------------------------------
 * This is the guy that does special things to a class.
 */
public class SpecialClassAdaptor extends ClassVisitor {

    private boolean isClassOfInterest = false;

    /**
     * Ctor.
     * @param cw a ClassWriter
     */
    public SpecialClassAdaptor(ClassWriter cw) {
        super(ASM9, cw);
    }

    /**
     * This gets called for each Class, as a step in chain-of-command
     * @param version the class version. The minor version is stored in the 16 most significant bits,
     *     and the major version in the 16 least significant bits.
     * @param access the class's access flags
     * @param name the internal name of the class
     * @param signature the signature of this class. May be {@literal null} if the class is not a
     *     generic one, and does not extend or implement generic classes or interfaces.
     * @param superName the internal of name of the super class.
     *     For interfaces, the super class is {@link Object}.
     * @param interfaces the internal names of the class's interfaces
     */
    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if(descriptor.equals("Lcom/jim/inst/InjectPrintlnMethodNames;")) {
            isClassOfInterest = true;
        }
        return cv.visitAnnotation(descriptor, visible);
    }

    /**
     * This gets called for each Method, as a step in chain-of-command
     * @param access the method's access flags. This parameter also indicates if
     *     the method is synthetic and/or deprecated.
     * @param name the method's name.
     * @param descriptor the method's descriptor.
     * @param signature the method's signature.
     *     return type and exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes.
     * @return new SpecialMethodAdaptor if it's a class of interest, else default MethodVisitor
     *     from next cv
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
