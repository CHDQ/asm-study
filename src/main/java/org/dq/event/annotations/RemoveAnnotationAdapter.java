package org.dq.event.annotations;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class RemoveAnnotationAdapter extends ClassVisitor {
    private String asmDesc;

    public RemoveAnnotationAdapter(String asmDesc) {
        super(Opcodes.ASM7);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(asmDesc)) {
            return null;
        }
        return super.visitAnnotation(descriptor, visible);
    }
}
