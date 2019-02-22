package org.dq.event.clazz.add;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class AddFieldAdapter extends ClassVisitor {
    private int fAcc;
    private String fName;
    private String fDesc;
    private boolean isFieldPresent;

    public AddFieldAdapter(ClassVisitor classVisitor, int fAcc, String fName, String fDesc, boolean isFieldPresent) {
        super(Opcodes.ASM7, classVisitor);
        this.fAcc = fAcc;
        this.fName = fName;
        this.fDesc = fDesc;
        this.isFieldPresent = isFieldPresent;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (name.equals(fName)) {
            isFieldPresent = true;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent) {
            FieldVisitor fieldVisitor = super.visitField(fAcc, fName, fDesc, null, null);
            if (fieldVisitor != null) {
                fieldVisitor.visitEnd();
            }
        }
        super.visitEnd();
    }
}
