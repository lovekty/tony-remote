package me.tonyirl.tremote.core;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author tony.zhuby
 * @date 2020/11/16
 */
@Slf4j
public class ServicePublisher<Iface> {

    private final Iface service;
    private final ServiceInfo serviceInfo;
    private final Class<?> serviceType;
    private final Class<?> ifaceType;
    private final int port;
    private final ServiceDiscovery<ServiceInfo> discovery;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private volatile TServer server;

    public ServicePublisher(@NonNull Iface service, @NonNull ServiceInfo serviceInfo, @NonNull ServiceDiscovery<ServiceInfo> discovery) {
        this.service = service;
        this.serviceInfo = serviceInfo;
        this.serviceType = ThriftHelper.serviceFromServiceInstance(service);
        this.ifaceType = ThriftHelper.ifaceFromServiceInstance(service);
        this.port = servicePort();
        this.discovery = discovery;
    }

    private int servicePort() {
        if (serviceInfo.getPort() > 0) {
            return serviceInfo.getPort();
        } else {
            return 9000 + Math.abs(serviceInfo.defineName().hashCode() % 3607 + serviceType.hashCode() % 4639);
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
        TProcessor processor = ThriftHelper.processorFromService(serviceType)
                .getConstructor(ifaceType).newInstance(service);
        args.processor(processor);
        server = new TNonblockingServer(args);
        executor.execute(server::serve);
        ServiceInstance<ServiceInfo> instance = ServiceInstance.<ServiceInfo>builder().port(port).name(serviceInfo.defineName()).payload(serviceInfo).build();
        discovery.registerService(instance);
        log.info("【register service success】 service instance:{}", instance);
    }
}
