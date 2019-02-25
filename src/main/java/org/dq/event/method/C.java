package org.dq.event.method;


import org.dq.event.clazz.MyClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

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
        C o = (C) aClass.getConstructor().newInstance();
        o.m();
//        new C().m();
    }
}
