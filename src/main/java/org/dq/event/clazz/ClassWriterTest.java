package org.dq.event.clazz;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

public interface ClassWriterTest {
    int LESS = -1;
    int EQUAL = 0;
    int GREATER = 1;

    int compareTo(Object o);

    static void main(String[] args) {
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(V11, ACC_PUBLIC + ACC_INTERFACE, "org/dq/event/clazz/ClassWriterTest", null, "java/lang/Object", null);
        classWriter.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "LESS", "I", null, -1).visitEnd();//常量值必须设置初始值，非常量值必须不设置
        classWriter.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "EQUAL", "I", null, 0).visitEnd();
        classWriter.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "GREATER", "I", null, 1).visitEnd();
        classWriter.visitMethod(ACC_PUBLIC, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
        classWriter.visitEnd();
        byte[] bytes = classWriter.toByteArray();
    }
}
