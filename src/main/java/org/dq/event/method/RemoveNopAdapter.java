package org.dq.event.method;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class RemoveNopAdapter extends MethodVisitor {
    public RemoveNopAdapter(MethodVisitor methodVisitor) {
        super(ASM7, methodVisitor);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode != NOP) {
            super.visitInsn(opcode);
        }
    }
}
