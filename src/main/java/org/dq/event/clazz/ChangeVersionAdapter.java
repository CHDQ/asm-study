package org.dq.event.clazz;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ChangeVersionAdapter extends ClassVisitor {
    public ChangeVersionAdapter(ClassVisitor cv) {
        super(Opcodes.ASM7, cv);
    }

    /**
     * 该方法中重写了class的jvm版本
     *
     * @param version
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(Opcodes.V11, access, name, signature, superName, interfaces);
    }

    /**
     * 演示优化26页
     * <p>
     * 如果ClassReader组件检测到作为参数传递给其Accept方法的ClassVisitor返回的方法访问者来自ClassWriter，
     * 这意味着该方法的内容不会被转换，甚至应用程序也不会看到。
     * <p>
     * <p>
     * If a ClassReader component detects that a MethodVisitor returned by
     * the ClassVisitor passed as argument to its accept method comes from
     * a ClassWriter, this means that the content of this method will not be
     * transformed, and will in fact not even be seen by the application.
     * <p>
     * In this case the ClassReader component does not parse the content of
     * this method, does not generate the corresponding events, and just copies
     * the byte array representation of this method in the ClassWriter.
     * 优缺点
     * Unfortunately this optimization requires to copy all the constants defined in the original class into the transformed one.
     * This is not a problem for tranformations that add fields, methods or instructions, but this leads to
     * bigger class files, compared to the unoptimized case, for transformations that
     * remove or rename many class elements.It is therefore recommanded to use
     * this optimization only for “additive” transformations.
     *
     * @param args
     */
    public static void main(String[] args) {
        byte[] b1 = new byte[10];//测试数据
        ClassReader classReader = new ClassReader(b1);
        ClassWriter classWriter = new ClassWriter(classReader, 0);
        ChangeVersionAdapter adapter = new ChangeVersionAdapter(classWriter);
        classReader.accept(adapter, 0);
        classWriter.toByteArray();
    }
}
