package me.tony.remote.base.server.factory;

import me.tony.remote.base.config.server.ServerConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;

public interface ServerFactory {

    TServer buildServer(ServerConfig config, Class<? extends TProcessor> processorType, Class<?> ifaceType, Object provider);

    ServerConfig.ServerType supportedServerType();
}
