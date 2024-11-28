package com.jim.inst;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

/**
 * @apiNote PreMain ----------------------------------------------------------
 * This class gets control BEFORE main due to settings in MANIFEST.MF as well
 *     as a special JVM command-line argument
 */
public class PreMain {

    /**
     * Let's track all referenced classes, just for testing.
     */
    private static final List<String> classes = new LinkedList<>();

    /**
     * This must be called premain() and have the arguments shown here.
     * Note that this must be in a .jar file, not a bare .class file.
     * To call this BEFORE main(), see MANIFEST.MF and VM option -javaagent
     * @param agentArgs command-line tail, full name of a class with slashes
     *                  instead of periods. If omitted, deafults to TestClass
     * @param inst instrumentation, created and passed to this by JVM
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        changeClass(inst);
    }

    /**
     * This inserts a SpecialClassAdaptor into the class loaders
     * @param inst instrumentation from the jvm
     */
    private static void changeClass(Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader l, String name, Class c,
                                    ProtectionDomain d, byte[] b) {
                if(name == null) {
                    if(c == null) {
                        return null;
                    }
                    name = c.getName();
                }
                classes.add(name);
                ClassReader cr = new ClassReader(b);
                ClassWriter cw = new ClassWriter(cr, 0);
                ClassVisitor cv = new SpecialClassAdaptor(cw);
                cr.accept(cv, 0);
                return cw.toByteArray();
            }
        });
    }

    /**
     * Return the descriptors of all classes.
     * @return list of referenced classes
     */
    public static List<String> getClasses() {
        return classes;
    }

}
