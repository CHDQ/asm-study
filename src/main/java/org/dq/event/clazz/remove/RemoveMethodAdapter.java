package org.dq.event.clazz.remove;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RemoveMethodAdapter extends ClassVisitor {
    private String mName;
    private String mDesc;

    public RemoveMethodAdapter(ClassVisitor classVisitor, String mName, String mDesc) {
        super(Opcodes.ASM7, classVisitor);
        this.mName = mName;
        this.mDesc = mDesc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(mName) && descriptor.equals(mDesc)) {//如果名称和描述符匹配,移除该方法
            return null;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
