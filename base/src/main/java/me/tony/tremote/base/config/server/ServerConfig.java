package me.tony.tremote.base.config.server;

import me.tony.tremote.base.config.protocol.ProtocolConfig;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.io.Serializable;

public class ServerConfig implements Serializable {
    private static final long serialVersionUID = 5879890405954763262L;
    private int port;
    private int timeout;
    private ServerType serverType;
    private ProtocolConfig protocol;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public ProtocolConfig getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }

    public enum ServerType {
        SIMPLE(1, TSimpleServer.class, TServer.Args.class, TServerSocket.class),
        NONBLOCKING(2, TNonblockingServer.class, TNonblockingServer.Args.class, TNonblockingServerSocket.class),
        HSHA(3, THsHaServer.class, THsHaServer.Args.class, TNonblockingServerSocket.class),
        SELECTOR(4, TThreadedSelectorServer.class, TThreadedSelectorServer.Args.class, TNonblockingServerSocket.class),
        POOL(5, TThreadPoolServer.class, TThreadPoolServer.Args.class, TServerSocket.class);

        private int id;
        private Class<? extends TServer> serverClass;
        private Class<? extends TServer.AbstractServerArgs> argsClass;
        private Class<? extends TServerTransport> transportClass;

        ServerType(int id,
                   Class<? extends TServer> serverClass,
                   Class<? extends TServer.AbstractServerArgs> argsClass,
                   Class<? extends TServerTransport> transportClass) {
            this.id = id;
            this.serverClass = serverClass;
            this.argsClass = argsClass;
            this.transportClass = transportClass;
        }

        public int getId() {
            return id;
        }

        public Class<? extends TServer> getServerClass() {
            return serverClass;
        }

        public Class<? extends TServer.AbstractServerArgs> getArgsClass() {
            return argsClass;
        }

        public Class<? extends TServerTransport> getTransportClass() {
            return transportClass;
        }
    }
}
