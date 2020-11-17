package me.tony.remote.base.server;

import me.tony.remote.base.config.server.ServerConfig;
import me.tony.remote.base.server.factory.ServerFactory;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public final class ServerBuilder {

    private static final Map<ServerConfig.ServerType, ServerFactory> SERVER_TYPE_SERVER_FACTORY_MAP;

    static {
        ServiceLoader<ServerFactory> serviceLoader = ServiceLoader.load(ServerFactory.class, null);
        Map<ServerConfig.ServerType, ServerFactory> map = new HashMap<>();
        serviceLoader.forEach(factory -> {
            if (factory.supportedServerType() != null) {
                map.put(factory.supportedServerType(), factory);
            }
        });
        SERVER_TYPE_SERVER_FACTORY_MAP = Collections.unmodifiableMap(map);
    }

    private ServerBuilder() {
    }

    public static TServer build(ServerConfig config, Class<? extends TProcessor> processorType, Class<?> ifaceType, Object provider) {
        ServerFactory factory = SERVER_TYPE_SERVER_FACTORY_MAP.get(config.getServerType());
        if (factory == null) {
            throw new IllegalArgumentException("cannot find factory for " + config.getServerType());
        }
        return factory.buildServer(config, processorType, ifaceType, provider);
    }
}
