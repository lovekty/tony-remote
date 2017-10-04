package me.tony.tremote.base.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ClientProxy<T> implements InvocationHandler {

    private Class<T> serviceType;
    private Class<?> ifaceType;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
