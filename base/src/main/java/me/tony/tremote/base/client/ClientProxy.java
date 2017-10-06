package me.tony.tremote.base.client;

import me.tony.tremote.base.ThriftCommonUtils;
import me.tony.tremote.base.registry.ServerDescriptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ClientProxy<Iface> implements InvocationHandler {

    private Class<?> serviceType;
    private Class<Iface> ifaceType;
    private ServerDescriptor serverDescriptor;
//    private ConcurrentHashMap<>

    public ClientProxy(Class<Iface> ifaceType, ServerDescriptor serverDescriptor) {
        this.ifaceType = Objects.requireNonNull(ifaceType);
        this.serverDescriptor = Objects.requireNonNull(serverDescriptor);
        this.serviceType = ThriftCommonUtils.serviceFromIface(ifaceType);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
