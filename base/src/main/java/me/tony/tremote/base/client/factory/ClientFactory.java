package me.tony.tremote.base.client.factory;

import me.tony.tremote.base.client.ServerDescriptor;
import org.apache.thrift.TServiceClient;

public interface ClientFactory<C extends TServiceClient> {

    C buildClient(Class<C> clientType, ServerDescriptor serverDescriptor);
}
