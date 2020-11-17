package me.tonyirl.tremote.core;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

/**
 * @author tony.zhuby
 * @date 2020/11/16
 */
public class ServicePublisher<T> {

    private final T service;
    private final ServiceInfo serviceInfo;
    private final Class<?> serviceType;
    private final Class<?> ifaceType;
    private final int port;
    private final ServiceDiscovery<ServiceInfo> discovery;
    private volatile TServer server;

    public ServicePublisher(@NonNull T service, @NonNull ServiceInfo serviceInfo, @NonNull CuratorFramework zk) {
        this.service = service;
        this.serviceInfo = serviceInfo;
        this.serviceType = ThriftHelper.serviceTypeFromService(service);
        this.ifaceType = ThriftHelper.ifaceFromService(service);
        this.port = servicePort();
        this.discovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class).basePath("/tremote-services").client(zk).build();
    }

    private int servicePort() {
        if (serviceInfo.getPort() > 0) {
            return serviceInfo.getPort();
        } else {
            return 9000 + serviceInfo.getName().hashCode() % 3607 + serviceType.hashCode() % 4639;
        }
    }

    @SneakyThrows
    public synchronized final void publish() {
        if (server != null) {
            throw new IllegalStateException("server is already published @port:" + port);
        }
        TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(port);
        TNonblockingServer.Args args = new TNonblockingServer.Args(serverSocket);
        args.protocolFactory(new TCompactProtocol.Factory());
        args.transportFactory(new TFramedTransport.Factory());
        TProcessor processor = ThriftHelper.processorFromServiceType(serviceType)
                .getConstructor(ifaceType).newInstance(service);
        args.processor(processor);
        server = new TNonblockingServer(args);
        server.serve();
        discovery.start();
        discovery.registerService(ServiceInstance.<ServiceInfo>builder().port(port).name(serviceInfo.defineName()).payload(serviceInfo).build());
    }
}
