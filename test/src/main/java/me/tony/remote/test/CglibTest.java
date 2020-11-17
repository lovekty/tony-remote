package me.tony.remote.test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author tony.zhuby
 * @date 2020/5/24
 */
public class CglibTest {

    public static void main(String[] args) {
        MyBiz myBiz = new MyCglibProxy().create(MyBiz.class);
        System.out.println(myBiz.sayHello("tony"));
    }

}

class MyCglibProxy implements MethodInterceptor {

    <T> T create(Class<T> clazz) {
        var enhancer = new Enhancer();
        enhancer.setInterfaces(clazz.getInterfaces());
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return clazz.cast(enhancer.create(new Class[]{String.class}, new Object[]{"hehe"}));
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("before proxy");
        var ret = proxy.invokeSuper(obj, args);
        System.out.println("after proxy");
        return ret;
    }
}

class MyBiz {
    private final String greetings;

    public MyBiz(String greetings) {
        this.greetings = greetings;
    }

    String sayHello(String name) {
        return greetings + "! " + name;
    }
}