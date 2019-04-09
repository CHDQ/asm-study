package org.dq;

import static org.junit.Assert.assertTrue;

import org.dq.event.clazz.MyClassLoader;
import org.dq.event.method.AddTimerAdapter;
import org.dq.event.method.C;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void test() throws Exception {
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor classVisitor = new AddTimerAdapter(cw);
        ClassReader classReader = new ClassReader("org.dq.event.method.C");
        classReader.accept(classVisitor, 0);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //Class<?> aClass = Class.forName("org.dq.event.method.C", true, classLoader);
        Method defineClass = Class.forName("java.lang.ClassLoader").getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
        defineClass.setAccessible(true);
        Class aClass = (Class) defineClass.invoke(classLoader, "org.dq.event.method.C", cw.toByteArray(), 0, cw.toByteArray().length);
        C c = (C) aClass.getConstructor().newInstance();
        c.m();
    }
}
