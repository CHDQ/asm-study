# _asm学习_

## 1. 字节码

>源码编译成字节码的时候会做一下几步

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