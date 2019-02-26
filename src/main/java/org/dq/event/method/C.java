package org.dq.event.method;


import org.dq.event.clazz.MyClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class C {

    public void m() throws Exception {
        Thread.sleep(100);
    }

    public static void main(String[] args) throws Exception {
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor classVisitor = new AddTimerAdapter(cw);
        ClassReader classReader = new ClassReader("org.dq.event.method.C");
        classReader.accept(classVisitor, 0);
        MyClassLoader myClassLoader = new MyClassLoader();
        Class aClass = myClassLoader.defineClass("org.dq.event.method.C", cw.toByteArray());
        Method m = aClass.getMethod("m", null);
        m.invoke(aClass.newInstance());
        Field timer = aClass.getDeclaredField("timer");
        timer.setAccessible(true);
        Object o = timer.get(aClass);
        System.out.println(o);
//        new C().m();
    }
}
