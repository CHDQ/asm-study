package org.dq.event.clazz;


import org.objectweb.asm.*;
import org.objectweb.asm.util.ASMifier;

import java.io.IOException;

/**
 * 单独工作模式
 */
public class ClassReaderTest extends ClassVisitor {


    public ClassReaderTest(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName + " { ");
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println(" " + descriptor + " " + name);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println(" " + name + descriptor);
        return null;
    }

    @Override
    public void visitEnd() {
        System.out.println(" } ");
    }

    public static void main(String[] args) throws IOException {
        ClassReaderTest classReaderTest = new ClassReaderTest(Opcodes.ASM7);
        //可以用该方式作为参数cl.getResourceAsStream(classname.replace(’.’, ’/’) + ".class");
        ClassReader classReader = new ClassReader("java.lang.Runnable");
        classReader.accept(classReaderTest, 0);
    }
}
