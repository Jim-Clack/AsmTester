package com.jim.inst;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @apiNote PreMain ----------------------------------------------------------
 * This class gets control BEFORE main due to settings in MANIFEST.MF as well
 *     as a special JVM command-line argument
 */
public class PreMain {

    /**
     * True to assemble the set of all classes.
     */
    public static boolean TRACK_CLASSES = true;

    /**
     * Class to modify.
     */
    private static String targetClassFullName = "com/jim/inst/TestClass";

    /**
     * Let's track all referenced classes, both loaded and transformable, just for testing.
     */
    private static final Set<String> classes = new HashSet<>();

    /**
     * This must be called premain() and have the arguments shown here.
     * Note that this must be in a .jar file, not a bare .class file.
     * To call this BEFORE main(), see MANIFEST.MF and VM option -javaagent
     * @param agentArgs command-line tail, full name of a class with slashes
     *                  instead of periods. If omitted, defults to TestClass
     * @param inst instrumentation, created and passed to this by JVM
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        if(agentArgs != null && !agentArgs.trim().isEmpty()) {
            targetClassFullName = agentArgs;
        }
        System.out.println("########## premain()");
        changeClass(inst, targetClassFullName);
    }

    /**
     * This inserts a SpecialClassAdaptor into the class loaders
     * @param inst instrumentation from the jvm
     */
    private static void changeClass(Instrumentation inst, String targetClassFullName) {
        if(classes.isEmpty()) {
            Arrays.stream(inst.getAllLoadedClasses()).forEach(clazz -> {
                addClassName(clazz, null);
            });
        }
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader l, String name, Class c,
                                    ProtectionDomain d, byte[] b) {
                addClassName(c, name);
                ClassReader cr = new ClassReader(b);
                ClassWriter cw = new ClassWriter(cr, 0);
                ClassVisitor cv = new SpecialClassAdaptor(cw, targetClassFullName);
                cr.accept(cv, 0);
                return cw.toByteArray();
            }
        });
    }

    /**
     * Update the classes set with the name of a class.
     * @implNote Number, Enum, collections, and boxed types are also added to the set
     * @param clazz the Class itself, null if class is not loaded and can be transformed
     * @param name the name of the class, may be null if clazz is not null
     */
    private static void addClassName(Class<?> clazz, String name) {
        if(!TRACK_CLASSES) {
            return;
        }
        String loaded = " wasloaded: ";
        String className = name;
        if(clazz == null) {
            loaded = " transform: ";
        } else {
            if (clazz.isPrimitive() || clazz.isArray()) {
                return;
            }
            if (className == null) {
                className = clazz.getName();
            }
        }
        classes.add(loaded + className.replaceAll("/", "."));
    }

    /**
     * Return the descriptors of all classes. Note: Callable from main().
     * @return set of referenced classes
     */
    public static Set<String> getClasses() {
        return classes;
    }

}
