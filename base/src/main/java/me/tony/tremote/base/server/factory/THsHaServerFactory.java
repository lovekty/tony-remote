package me.tony.tremote.base.server.factory;


import me.tony.tremote.base.config.server.ServerConfig;

public class THsHaServerFactory extends AbstractServerFactory {
    @Override
    public ServerConfig.ServerType supportedServerType() {
        return ServerConfig.ServerType.HSHA;
    }
}
