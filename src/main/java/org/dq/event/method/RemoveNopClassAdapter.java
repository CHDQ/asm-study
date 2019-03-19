package org.dq.event.method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM7;

public class RemoveNopClassAdapter extends ClassVisitor {
    public RemoveNopClassAdapter(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor;
        methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (methodVisitor != null) {
            methodVisitor = new RemoveNopAdapter(methodVisitor);
        }
        return methodVisitor;
    }
}
