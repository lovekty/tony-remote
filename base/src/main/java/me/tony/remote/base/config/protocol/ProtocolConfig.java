package me.tony.remote.base.config.protocol;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

import java.io.Serializable;

public class ProtocolConfig implements Serializable {
    private static final long serialVersionUID = -6582514939533629776L;
    private ProtocolType protocolType;

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public enum ProtocolType {
        BINARY(1, TBinaryProtocol.class, TBinaryProtocol.Factory.class),
        JSON(2, TJSONProtocol.class, TJSONProtocol.Factory.class);

        private int id;
        private Class<? extends TProtocol> protocolClass;
        private Class<? extends TProtocolFactory> factoryClass;

        ProtocolType(int id,
                     Class<? extends TProtocol> protocolClass,
                     Class<? extends TProtocolFactory> factoryClass) {
            this.id = id;
            this.protocolClass = protocolClass;
            this.factoryClass = factoryClass;
        }

        public int getId() {
            return id;
        }

        public Class<? extends TProtocol> getProtocolClass() {
            return protocolClass;
        }

        public Class<? extends TProtocolFactory> getFactoryClass() {
            return factoryClass;
        }
    }

}
