# _asm学习_
![image](https://img.shields.io/badge/jdk-8+-brightgreen.svg)
![image](https://img.shields.io/badge/org.ow2.asm-7.0-brightgreen.svg)
## 1. 字节码

>源码编译成字节码的时候会做以下几步

>1.将'.'替换成'/'

>2.类型会用描述符表示

- 类型描述符对照

|java类型|字节码描述符|
|:------:|:------:|
|boolean|Z|
|char|C|
|byte|B|
|short|S|
|int|I|
|float|F|
|long|J|
|double|D|
|Object|Ljava/lang/Object;|
|int[]|[I|
|Object[][]|[[Ljava/lang/Object;|
> 基本类型都是以单个字符表示,引用类型前面会添加L,后面会添加;

- 方法描述符
>方法描述符是一个参数类型和返回值类型的描述符的字符串

>A method descriptor is a list of type descriptors that describe the parameter types and the return type of a method, in a single string.

>A method descriptor starts with a left parenthesis, followed by the type descriptors of each formal parameter, followed by a right parenthesis, followed by the type descriptor of the return type, or V if the method returns void (a method descriptor does not contain the method’s name or the argument names).
 
 |方法声明|编译后的描述符|
 |:---:|:---:|
 |void m(int i, float f)|(IF)V|
 |int m(Object o)|(Ljava/lang/Object)I|
 |int[] m(int i, String s)|(ILjava/lang/String;)[I|
 |Object m(int[] i)|([I)Ljava/lang/Object;|
 
## 2.事件模式
1. class

    - ClassVisitor必须按照下面的顺序调用
    >visit visitSource? visitOuterClass? ( visitAnnotation | visitAttribute )* ( visitInnerClass | visitField | visitMethod )* visitEnd
    
    - 如果类名称需要更改的时候需要注意
    >Indeed the name of the class can appear in  many different places inside a compiled class, and all these occurrences must be changed to really rename the class.
    
    - 优化
    >If a ClassReader component detects that a MethodVisitor returned by the ClassVisitor passed as argument to its accept method comes from a ClassWriter, this means that the content of this method will not be transformed, and will in fact not even be seen by the application.
    
    - 注意
    
        - But class transformations done inside a ClassLoader can only transform the classes loaded by this class loader.If you want to transform all classes you will have to put your transformation inside a ClassFileTransformer.
        - 增加元素的时候(29页)
        >You cannot do
         this in the visit method, for example, because this may result in a call to
         visitField followed by visitSource, visitOuterClass, visitAnnotation
         or visitAttribute, which is not valid. You cannot put this new call in
         the visitSource, visitOuterClass, visitAnnotation or visitAttribute
         methods, for the same reason. The only possibilities are the visitInnerClass,
         visitField, visitMethod or visitEnd methods.
        - MultiClassAdapter
        ```
        public class MultiClassAdapter extends ClassVisitor {
            protected ClassVisitor[] cvs;
            public MultiClassAdapter(ClassVisitor[] cvs) {
                super(ASM4);
                this.cvs = cvs;
            }
            @Override public void visit(int version, int access, String name,
            String signature, String superName, String[] interfaces) {
                for (ClassVisitor cv : cvs) {
                  cv.visit(version, access, name, signature, superName, interfaces);
                }
            }
            ...
        }
        ```
   - 工具asm-util
        - Type保存了一些描述符和类型
        - TraceClassVisitor可以打印出修改后的class文件的文本描述
        - CheckClassAdapter检查生成的class是否有效(34页)
        ```
        ClassWriter cw = new ClassWriter(0);
        TraceClassVisitor tcv = new TraceClassVisitor(cw, printWriter);
        CheckClassAdapter cv = new CheckClassAdapter(tcv);
        cv.visit(...);
        ...
        cv.visitEnd();
        byte b[] = cw.toByteArray();
        ```
        - ASMifier将已经存在的class文件反编译成asm的生成代码
        ```
        java -classpath asm-7.0.jar:asm-util-7.0.jar org.objectweb.asm.util.ASMifier java.lang.Runnable(linux)
        java -classpath asm-7.0.jar;asm-util-7.0.jar org.objectweb.asm.util.ASMifier java.lang.Runnable(windows)
        ```
    
2. method
    - stack(28页)
    >Each frame contains two parts: a ***local variables part*** and an ***operand stack
     part***. The ***local variables part*** contains variables that can be accessed by their
     index, in random order. The ***operand stack part***, as its name implies, is a stack
     of values that are used as operands by bytecode instructions. This means that
     the values in this stack can only be accessed in Last In First Out order. ***Do
     not confuse the operand stack and the thread’s execution stack: each frame
     in the execution stack contains its own operand stack.***
    
    >The size of the local variables and operand stack parts depends on the method’s
     code. It is computed at compile time and is stored along with the bytecode
     instructions in compiled classes. As a consequence, all the frames that correspond to the invocation of a given method have the same size, but frames
     that correspond to different methods can have different sizes for their local
     variables and operand stack parts.
     
    >(39页)Instruction arguments must not be confused with instruction operands: argument values
     are statically known and are stored in the compiled code, while operand
     values come from the operand stack and are known only at runtime.
    
    - 命令说明在39页
    >The ILOAD, LLOAD, FLOAD, DLOAD, and ALOAD instructions read a local variable
     and push its value on the operand stack.
     
    >Symmetrically the ISTORE, LSTORE,
     FSTORE, DSTORE and ASTORE instructions pop a value from the operand stack
     and store it in a local variable designated by its index i.
     
    >(40/46)As said above, starting from Java 6, compiled classes contain, in addition to
     bytecode, a set of stack map frames. In order to save space, a compiled method
     does not contain one frame per instruction: in fact it contains only the frames
     for the instructions that correspond to jump targets or exception handlers, or
     that follow unconditional jump instructions. Indeed the other frames can be
     easily and quickly inferred from these ones.
     
     >In order to save even more space, each frame is compressed by storing only its
      difference compared to the previous frame, and the initial frame is not stored at all, because it can easily be deduced from the method parameter types. 
      
     - 44-45/50-51 ClassWriter构造函数的参数含义
     - tools
     >过时了，没有main方法<br>
     java -classpath asm-7.0.jar:asm-util-7.0.jar org.objectweb.asm.util.TraceClassVisitor java.lang.Void 
     
 # 加载修改后的字节码
  参见APPTest.java
  ```
        Method defineClass = Class.forName("java.lang.ClassLoader").getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
        defineClass.setAccessible(true);
        Class aClass = (Class) defineClass.invoke(classLoader, "org.dq.event.method.C", cw.toByteArray(), 0, cw.toByteArray().length);
        C c = (C) aClass.getConstructor().newInstance();
  ```
