package me.tony.remote.base.server.factory;


import me.tony.remote.base.config.server.ServerConfig;

public class TThreadPoolServerFactory extends AbstractServerFactory {

    @Override
    public ServerConfig.ServerType supportedServerType() {
        return ServerConfig.ServerType.POOL;
    }

}

