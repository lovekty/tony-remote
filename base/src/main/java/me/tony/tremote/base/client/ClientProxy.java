package me.tony.tremote.base.client;

import me.tony.tremote.base.ThriftCommonUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class ClientProxy implements InvocationHandler {

    private Class<?> serviceType;
    private Class<?> ifaceType;
    private ServerDescriptor serverDescriptor;

    public ClientProxy(Class<?> ifaceType, ServerDescriptor serverDescriptor) {
        this.ifaceType = Objects.requireNonNull(ifaceType);
        this.serverDescriptor = Objects.requireNonNull(serverDescriptor);
        this.serviceType = ThriftCommonUtils.serviceFromIface(ifaceType);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
