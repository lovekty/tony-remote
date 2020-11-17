package me.tony.remote.base.client.factory;

import me.tony.tremote.base.client.ServerDescriptor;
import org.apache.thrift.TServiceClient;

public class ClientFactoryImpl<C extends TServiceClient> implements ClientFactory<C> {
    @Override
    public C buildClient(Class<C> clientType, ServerDescriptor serverDescriptor) {
        return null;
    }
}
