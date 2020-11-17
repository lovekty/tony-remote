package me.tony.remote.base.config.server;

import me.tony.remote.base.config.protocol.ProtocolConfig;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
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
        SIMPLE(1, TSimpleServer.class, TServer.AbstractServerArgs.class,
                TServer.Args.class, TServerTransport.class,
                TServerSocket.class),
        NONBLOCKING(2, TNonblockingServer.class, AbstractNonblockingServer.AbstractNonblockingServerArgs.class,
                TNonblockingServer.Args.class, TNonblockingServerTransport.class,
                TNonblockingServerSocket.class),
        HSHA(3, THsHaServer.class, THsHaServer.Args.class,
                THsHaServer.Args.class, TNonblockingServerTransport.class,
                TNonblockingServerSocket.class),
        SELECTOR(4, TThreadedSelectorServer.class, TThreadedSelectorServer.Args.class,
                TThreadedSelectorServer.Args.class, TNonblockingServerTransport.class,
                TNonblockingServerSocket.class),
        POOL(5, TThreadPoolServer.class, TThreadPoolServer.Args.class,
                TThreadPoolServer.Args.class, TServerTransport.class,
                TServerSocket.class);

        private int id;
        private Class<? extends TServer> serverClass;
        private Class<?> serverConstructorParameter;
        private Class<? extends TServer.AbstractServerArgs> argsClass;
        private Class<?> argsConstructorParameter;
        private Class<? extends TServerTransport> transportClass;

        ServerType(int id,
                   Class<? extends TServer> serverClass,
                   Class<?> serverConstructorParameter,
                   Class<? extends TServer.AbstractServerArgs> argsClass,
                   Class<?> argsConstructorParameter,
                   Class<? extends TServerTransport> transportClass) {
            this.id = id;
            this.serverClass = serverClass;
            this.serverConstructorParameter = serverConstructorParameter;
            this.argsClass = argsClass;
            this.argsConstructorParameter = argsConstructorParameter;
            this.transportClass = transportClass;
        }

        public int getId() {
            return id;
        }

        public Class<? extends TServer> getServerClass() {
            return serverClass;
        }

        public Class<?> getServerConstructorParameter() {
            return serverConstructorParameter;
        }

        public Class<? extends TServer.AbstractServerArgs> getArgsClass() {
            return argsClass;
        }

        public Class<?> getArgsConstructorParameter() {
            return argsConstructorParameter;
        }

        public Class<? extends TServerTransport> getTransportClass() {
            return transportClass;
        }
    }
}
