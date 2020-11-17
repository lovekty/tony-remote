package me.tony.remote.base.registry;

public interface ServerDescriptor {

    String getHost();

    int getPort();

    String getProtocol();

    String getTransportType();

}
