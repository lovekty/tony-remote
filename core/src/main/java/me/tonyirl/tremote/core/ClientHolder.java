package me.tonyirl.tremote.core;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tony.zhuby
 */
public class ClientHolder<Iface> {

    private final Class<Iface> ifaceType;
    private final ServiceInfo serviceInfo;
    private final Class<?> serviceType;
    private final ServiceProvider<ServiceInfo> provider;
    private final Map<String, Iface> holder = new ConcurrentHashMap<>();

    public ClientHolder(@NonNull Class<Iface> ifaceType, @NonNull ServiceInfo serviceInfo, @NonNull ServiceProvider<ServiceInfo> provider) {
        this.ifaceType = ifaceType;
        this.serviceType = ThriftHelper.serviceFromIface(ifaceType);
        this.serviceInfo = serviceInfo;
        this.provider = provider;
    }

    @SneakyThrows
    private Iface createClient(ServiceInstance<ServiceInfo> si) {
        String address = si.getAddress();
        int port = si.getPort();
        TTransport transport = new TFramedTransport(new TSocket(address, port));
        TServiceClient client = ThriftHelper.clientFactoryFromService(serviceType).getConstructor().newInstance().getClient(new TCompactProtocol(transport));
        transport.open();
        return ifaceType.cast(client);
    }

    @SneakyThrows
    public Iface getClient() {
        ServiceInstance<ServiceInfo> si = provider.getInstance();
        if (si == null) {
            throw new RuntimeException("service info:" + serviceInfo + " get null service node");
        }
        if (serviceInfo.getPort() > 0 && !serviceInfo.getPort().equals(si.getPort())) {
            throw new RuntimeException("service info:" + serviceInfo + " port not match:" + si.getPort());
        }
        String key = si.getAddress() + ":" + si.getPort();
        return holder.computeIfAbsent(key, whatever -> createClient(si));
    }
}
