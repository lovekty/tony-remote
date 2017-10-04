package me.tony.tremote.base.client;

public interface ServerDescriptor {

    String getHost();

    int getPort();

    String getProtocol();

    String getTransportType();
}
