package me.tony.remote.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

interface MyIface {
    String sayHello(String name);
}

/**
 * @author tony.zhuby
 * @date 2020/5/24
 */
public class ProxyTest {

    public static void main(String[] args) {
        var proxy = new MyProxy<>(MyIface.class, new MyImpl());
        MyIface ins = proxy.newProxyInstance();
        System.out.println(ins.sayHello("tony"));
    }


}

class MyImpl implements MyIface {

    @Override
    public String sayHello(String name) {
        return "hello! " + name;
    }
}

class MyProxy<T> implements InvocationHandler {

    private final Class<T> clazz;
    private final T obj;

    public MyProxy(Class<T> clazz, T obj) {
        this.clazz = clazz;
        this.obj = Objects.requireNonNull(obj);
    }

    public T newProxyInstance() {
        return clazz.cast(Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before invoke");
        Object ret = method.invoke(obj, args);
        System.out.println("after invoke");
        return ret;
    }
}
