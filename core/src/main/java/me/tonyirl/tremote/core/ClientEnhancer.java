package me.tonyirl.tremote.core;

import lombok.NonNull;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author tony.zhuby
 */
public class ClientEnhancer<Iface> implements MethodInterceptor {

    private final Class<Iface> ifaceType;
    private final ClientHolder<Iface> holder;

    public ClientEnhancer(@NonNull Class<Iface> ifaceType, @NonNull ClientHolder<Iface> holder) {
        this.ifaceType = ifaceType;
        this.holder = holder;
    }

    public Iface create() {
        Enhancer enhancer = new Enhancer();
        if (ifaceType.isInterface()) {
            enhancer.setInterfaces(new Class<?>[]{ifaceType});
        } else {
            enhancer.setSuperclass(ifaceType);
        }
        enhancer.setCallback(this);
        return ifaceType.cast(enhancer.create());
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proxy.invoke(holder.getClient(), args);
    }
}
