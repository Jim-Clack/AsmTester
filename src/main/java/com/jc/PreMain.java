package com.jc;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class PreMain {

    // Class to adapt
    private static String targetClassFullName = "com/jc/TestClass";

    // This must be called premain() and have the arguments shown here...
    // Note that this must be in a .jar file, not a bare .class file.
    // To call this BEFORE main(), see MANIFEST.MF and VM option -javaagent
    public static void premain(String agentArgs, Instrumentation inst) {
        if(agentArgs != null && !agentArgs.trim().isEmpty()) {
            targetClassFullName = agentArgs;
        }
        changeClass(inst, targetClassFullName);
    }

    /**
     * This inserts a SpecialClassAdaptor into the class loaders
     * @param inst instrumentation
     * @param targetClassFullName name of class to adapt
     */
    private static void changeClass(Instrumentation inst, String targetClassFullName) {
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader l, String name, Class c,
                                    ProtectionDomain d, byte[] b)
                    throws IllegalClassFormatException {
                ClassReader cr = new ClassReader(b);
                ClassWriter cw = new ClassWriter(cr, 0);
                ClassVisitor cv = new SpecialClassAdaptor(cw, targetClassFullName);
                cr.accept(cv, 0);
                return cw.toByteArray();
            }
        });
    }

}