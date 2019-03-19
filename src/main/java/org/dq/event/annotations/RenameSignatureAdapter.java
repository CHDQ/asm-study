package org.dq.event.annotations;

import jdk.internal.org.objectweb.asm.signature.SignatureReader;
import jdk.internal.org.objectweb.asm.signature.SignatureVisitor;
import jdk.internal.org.objectweb.asm.signature.SignatureWriter;

import java.util.HashMap;
import java.util.Map;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM4;


public class RenameSignatureAdapter extends SignatureVisitor {
    private SignatureVisitor signatureVisitor;
    private Map<String, String> renaming;
    private String oldName;

    public RenameSignatureAdapter(SignatureVisitor signatureVisitor, Map<String, String> renaming) {
        super(ASM4);
        this.signatureVisitor = signatureVisitor;
        this.renaming = renaming;
    }

    @Override
    public void visitFormalTypeParameter(String name) {
        signatureVisitor.visitFormalTypeParameter(name);
    }

    @Override
    public SignatureVisitor visitClassBound() {
        signatureVisitor.visitClassBound();
        return this;
    }

    @Override
    public SignatureVisitor visitInterfaceBound() {
        signatureVisitor.visitInterfaceBound();
        return this;
    }

    @Override
    public void visitClassType(String name) {
        oldName = name;
        String newName = renaming.get(name);
        signatureVisitor.visitInnerClassType(newName == null ? name : newName);
    }

    @Override
    public SignatureVisitor visitParameterType() {
        signatureVisitor.visitParameterType();
        return this;
    }

    @Override
    public void visitTypeArgument() {
        signatureVisitor.visitTypeArgument();
    }

    @Override
    public void visitEnd() {
        signatureVisitor.visitEnd();
    }

    @Override
    public void visitInnerClassType(String name) {
        oldName = oldName + "." + name;
        String newName = renaming.get(oldName);
        signatureVisitor.visitInnerClassType(newName == null ? name : newName);
    }

    @Override
    public SignatureVisitor visitTypeArgument(char c) {
        signatureVisitor.visitTypeArgument(c);
        return this;
    }

    @Override
    public void visitBaseType(char c) {
        signatureVisitor.visitBaseType(c);
    }

    @Override
    public void visitTypeVariable(String s) {
        signatureVisitor.visitTypeVariable(s);//泛型<TK;TV;>
    }

    public static void main(String[] args) {
        //LA<TK;TV;>.B<TK;>; 结果
        String s = "Ljava/util/HashMap<TK;TV;>.HashIterator<TK;>;";
        Map<String, String> renaming = new HashMap<>();
        renaming.put("java/util/HashMap", "A");
        renaming.put("java/util/HashMap.HashIterator", "B");
        SignatureWriter sw = new SignatureWriter();
        SignatureVisitor sa = new RenameSignatureAdapter(sw, renaming);
        SignatureReader sr = new SignatureReader(s);
        sr.acceptType(sa);
        System.out.println(sw.toString());
    }
}
