package org.dq.event.clazz;

public class MyClassLoader extends ClassLoader {
    public Class defineClass(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }
}
