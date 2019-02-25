package org.dq.event.method;

import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;
import jdk.internal.org.objectweb.asm.commons.AnalyzerAdapter;
import jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;

public class AddTimerAdapter extends ClassVisitor {
    private String owner;
    private boolean isInterface;

    public AddTimerAdapter(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        /**
         * 用两个字节标识方法的通过性，所以当执行位与的时候，如果不为0的时候说明，该位置的值不为0，即标志位接口
         */
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (!isInterface && methodVisitor != null && !name.equals("<init>")) {
            methodVisitor = new AddTimerMethodAdapter(methodVisitor);
        }
        return methodVisitor;
    }

    @Override
    public void visitEnd() {
        if (!isInterface) {
//            System.out.println(LONG_TYPE.getDescriptor());
            FieldVisitor fieldVisitor = super.visitField(ACC_PUBLIC + ACC_STATIC, "timer", "J", null, null);
            if (fieldVisitor != null) {
                fieldVisitor.visitEnd();
            }
        }
        super.visitEnd();
    }

    class AddTimerMethodAdapter extends MethodVisitor {

        public AddTimerMethodAdapter(MethodVisitor methodVisitor) {
            super(ASM7, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            super.visitFieldInsn(GETSTATIC, owner, "timer", LONG_TYPE.getDescriptor());
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitInsn(LSUB);
            super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
        }

        /**
         * 只是检测了return之前的代码
         *
         * @param opcode
         */
        @Override
        public void visitInsn(int opcode) {
            if (opcode >= IRETURN && opcode <= RETURN || opcode == ATHROW) {
                super.visitFieldInsn(GETSTATIC, owner, "timer", LONG_TYPE.getDescriptor());
                super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                super.visitInsn(LADD);
                super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals);
        }
    }

    private class AddTimerMethodAdapter2 extends AnalyzerAdapter {
        private int maxStack;

        public AddTimerMethodAdapter2(String owner, int access, String name, String desc, jdk.internal.org.objectweb.asm.MethodVisitor mv, int maxStack) {
            super(ASM7, owner, access, name, desc, mv);
            this.maxStack = maxStack;
        }

        @Override
        public void visitCode() {
            super.visitCode();
            super.visitFieldInsn(GETSTATIC, owner, "timer", LONG_TYPE.getDescriptor());
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitInsn(LSUB);
            super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
            this.maxStack = 4;
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode >= IRETURN && opcode <= RETURN || opcode == ATHROW) {
                super.visitFieldInsn(GETSTATIC, owner, "timer", LONG_TYPE.getDescriptor());
                super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                super.visitInsn(LADD);
                super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
                this.maxStack = Math.max(this.maxStack, stack.size());
            }
            super.visitInsn(opcode);
        }

        /**
         * 此处可以不重写，AnalyzerAdapter会自动计算stack的大小
         *
         * @param maxStack
         * @param maxLocals
         */
        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(Math.max(this.maxStack, maxStack), maxLocals);
        }
    }

    class AddTimerMethodAdapter5 extends MethodVisitor {
        public LocalVariablesSorter lvs;
        public AnalyzerAdapter aa;
        private int time;
        private int maxStack;

        public AddTimerMethodAdapter5(MethodVisitor mv) {
            super(ASM4, mv);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J", false);
            time = lvs.newLocal(jdk.internal.org.objectweb.asm.Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, time);
            maxStack = 4;
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                        "currentTimeMillis", "()J", false);
                mv.visitVarInsn(LLOAD, time);
                mv.visitInsn(LSUB);
                mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                mv.visitInsn(LADD);
                mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
                maxStack = Math.max(aa.stack.size() + 4, maxStack);
            }
            mv.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            mv.visitMaxs(Math.max(this.maxStack, maxStack), maxLocals);
        }
    }

    class AddTimerMethodAdapter6 extends AdviceAdapter {
        public AddTimerMethodAdapter6(int access, String name, String desc,
                                      jdk.internal.org.objectweb.asm.MethodVisitor mv) {
            super(ASM4, mv, access, name, desc);
        }

        @Override
        protected void onMethodEnter() {
            mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J", false);
            mv.visitInsn(LSUB);
            mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
        }

        @Override
        protected void onMethodExit(int opcode) {
            mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J", false);
            mv.visitInsn(LADD);
            mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals);
        }
    }
}


