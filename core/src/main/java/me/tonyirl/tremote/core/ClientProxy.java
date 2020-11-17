package me.tonyirl.tremote.core;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tony.zhuby
 * @date 2020/5/21
 */
public class ClientProxy<Iface> implements InvocationHandler {

    private final Class<Iface> ifaceType;
    private final ClientHolder<Iface> holder;

    @SneakyThrows
    public ClientProxy(@NonNull Class<Iface> ifaceType, @NonNull ClientHolder<Iface> holder) {
        if (!ifaceType.isInterface()) {
            throw new RuntimeException("iface must be an interface! got: " + ifaceType.getName());
        }
        this.holder = holder;
        this.ifaceType = ifaceType;
    }


    public final Iface getProxyInstance() {
        return ifaceType.cast(Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{ifaceType}, this));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(holder.getClient(), args);
    }
}
