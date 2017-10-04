package me.tony.tremote.base.server.factory;

import me.tony.tremote.base.config.server.ServerConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;

public interface ServerFactory {

    TServer buildServer(ServerConfig config, Class<? extends TProcessor> processorType, Class<?> ifaceType, Object provider);

    ServerConfig.ServerType supportedServerType();
}
