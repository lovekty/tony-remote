package me.tony.tremote.base.server;

import me.tony.tremote.base.ThriftCommonUtils;
import me.tony.tremote.base.config.server.ServerConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class Server<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private Class<T> serviceType;
    private Class<?> ifaceType;
    private Class<? extends TProcessor> processorType;
    private Object provider;
    private ServerConfig config;
    private TServer server;
    private Thread thread;

    public Server(ServerConfig config, Class<T> serviceType, Object provider) {
        this.provider = Objects.requireNonNull(provider, "provider cannot be null!");
        this.serviceType = Objects.requireNonNull(serviceType, "service type cannot be null!");
        this.config = Objects.requireNonNull(config, "server config cannot be null!");
        ifaceType = ThriftCommonUtils.ifaceFromService(this.serviceType);
        if (!ifaceType.isAssignableFrom(provider.getClass())) {
            throw new IllegalArgumentException(String.format("provider of class: %s is not a %s", provider.getClass().getName(), ifaceType.getName()));
        }
        processorType = ThriftCommonUtils.processorFromService(serviceType);
        server = ServerBuilder.build(this.config, processorType, ifaceType, this.provider);
        thread = new Thread(() -> server.serve(), "ThriftServerThread-" + this.serviceType.getName());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.stop();
            LOGGER.info("{} stopped", thread.getName());
        }));
    }

    public void serve() {
        thread.start();
        LOGGER.info("{} started", server.getClass().getName());
    }

    public void stop() {
        server.stop();
        LOGGER.info("{} stopped", server.getClass().getName());
    }
}
