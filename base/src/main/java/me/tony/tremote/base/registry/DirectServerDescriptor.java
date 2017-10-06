package me.tony.tremote.base.registry;

import java.io.Serializable;
import java.util.Objects;

public class DirectServerDescriptor implements ServerDescriptor, Serializable {

    private static final long serialVersionUID = 1971280923764049586L;
    private String host;
    private int port;
    private String protocol;
    private String transportType;

    public DirectServerDescriptor(String host, int port, String protocol, String transportType) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.transportType = transportType;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getTransportType() {
        return transportType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectServerDescriptor)) return false;
        DirectServerDescriptor that = (DirectServerDescriptor) o;
        return getPort() == that.getPort() &&
                Objects.equals(getHost(), that.getHost()) &&
                Objects.equals(getProtocol(), that.getProtocol()) &&
                Objects.equals(getTransportType(), that.getTransportType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHost(), getPort(), getProtocol(), getTransportType());
    }
}
