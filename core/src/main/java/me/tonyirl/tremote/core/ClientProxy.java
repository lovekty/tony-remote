package me.tonyirl.tremote.core;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tony.zhuby
 * @date 2020/5/21
 */
public class ClientProxy<T> implements InvocationHandler {

    private final Class<T> ifaceType;
    private final ServiceInfo serviceInfo;
    private final Class<?> serviceType;
    private final ServiceProvider<ServiceInfo> provider;
    private final Map<String, T> map = new ConcurrentHashMap<>();

    @SneakyThrows
    public ClientProxy(@NonNull Class<T> ifaceType, @NonNull ServiceInfo serviceInfo, @NonNull CuratorFramework zk) {
        this.ifaceType = ifaceType;
        this.serviceInfo = serviceInfo;
        this.serviceType = ThriftHelper.serviceFromIfaceType(ifaceType);
        ServiceDiscovery<ServiceInfo> discovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class).basePath("/tremote-services").client(zk).build();
        discovery.start();
        this.provider = discovery.serviceProviderBuilder().serviceName(serviceInfo.getName()).build();
        provider.start();
    }

    private T createClient(ServiceInstance<ServiceInfo> si) {
        String address = si.getAddress();
        int port = si.getPort();
        TTransport transport = new TNonblockingSocket(address, port);
        new
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(proxy, args);
        return null;
    }
}
