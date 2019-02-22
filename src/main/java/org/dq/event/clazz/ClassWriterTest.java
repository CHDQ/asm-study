package org.dq.event.clazz;

import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.TraceFieldVisitor;
import org.objectweb.asm.ClassWriter;


import static org.objectweb.asm.Opcodes.*;

/**
 * 单独工作模式
 */
public interface ClassWriterTest {
    int LESS = -1;
    int EQUAL = 0;
    int GREATER = 1;

    int compareTo(Object o);

    static void main(String[] args) {
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(V11, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "org/dq/event/clazz/ClassWriterTest", null, "java/lang/Object", null);
        classWriter.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "LESS", "I", null, -2).visitEnd();//常量值必须设置初始值，非常量值必须不设置
        classWriter.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "EQUAL", "I", null, 0).visitEnd();
        classWriter.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "GREATER", "I", null, 1).visitEnd();
        classWriter.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
        classWriter.visitEnd();
        byte[] bytes = classWriter.toByteArray();
        MyClassLoader myClassLoader = new MyClassLoader();
        Class aClass = myClassLoader.defineClass("org.dq.event.clazz.ClassWriterTest", bytes);//获取该类
    }
}
