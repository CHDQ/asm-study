package org.dq.event.clazz.remove;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * 移除元素测试
 */
public class RemoveDebugAdapter extends ClassVisitor {
    public RemoveDebugAdapter(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);//注释后，源码不会编译到class中
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);//会转发调用，如果注释掉之后，原有的class中的外部类将会被移除
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);//会转发调用，如果注释掉之后，原有的class中的内部类将会被移除
    }
}
