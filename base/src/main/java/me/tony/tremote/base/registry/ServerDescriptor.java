package me.tony.tremote.base.registry;

public interface ServerDescriptor {

    String getHost();

    int getPort();

    String getProtocol();

    String getTransportType();

}
