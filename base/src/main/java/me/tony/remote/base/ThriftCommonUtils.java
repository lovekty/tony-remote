package me.tony.remote.base;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;

public final class ThriftCommonUtils {
    public static final String IFACE_TAG = "$Iface";
    public static final String PROCESSOR_TAG = "$Processor";
    public static final int IFACE_TAG_SIZE = IFACE_TAG.length();
    public static final String CLIENT_TAG = "$Client";
    public static final String CLIENT_FACTORY_TAG = "$Client$Factory";
    private static final ClassLoader CL = ThriftCommonUtils.class.getClassLoader();

    private ThriftCommonUtils() {
    }

    public static Class<?> ifaceFromService(Class<?> serviceType) {
        String ifaceTypeName = serviceType.getName() + IFACE_TAG;
        try {
            return CL.loadClass(ifaceTypeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find iface for %s", serviceType.getName()), e);
        }
    }

    public static Class<?> serviceFromIface(Class<?> ifaceType) {
        String ifaceTypeName = ifaceType.getName();
        if (!ifaceTypeName.endsWith(IFACE_TAG)) {
            throw new IllegalArgumentException(String.format("%s is not a thrift iface!", ifaceType.getName()));
        }
        String serviceTypeName = ifaceTypeName.substring(0, ifaceTypeName.length() - IFACE_TAG_SIZE);
        try {
            return CL.loadClass(serviceTypeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find base service of type %s", serviceTypeName), e);
        }
    }

    public static Class<? extends TProcessor> processorFromService(Class<?> serviceType) {
        String processorTypeName = serviceType.getName() + PROCESSOR_TAG;
        try {
            Class<?> clazz = CL.loadClass(processorTypeName);
            if (TProcessor.class.isAssignableFrom(clazz)) {
                return (Class<? extends TProcessor>) clazz;
            }
            throw new IllegalArgumentException(String.format("%s is not a TProcessor", processorTypeName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find a processor for %s", serviceType.getName()));
        }
    }

    public static Class<?> ifaceFromProvider(Object provider) {
        Class<?> providerType = provider.getClass();
        Class<?>[] interfaces = providerType.getInterfaces();
        for (Class<?> i : interfaces) {
            if (i.getName().endsWith(IFACE_TAG)) {
                return i;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not a thrift iface!", provider.getClass().getName()));
    }

    public static Class<? extends TServiceClient> clientFromService(Class<?> serviceType) {
        String clientTypeName = serviceType.getName() + CLIENT_TAG;
        try {
            Class<?> clazz = CL.loadClass(clientTypeName);
            if (TServiceClient.class.isAssignableFrom(clazz)) {
                return (Class<? extends TServiceClient>) clazz;
            }
            throw new IllegalArgumentException(String.format("%s is not a TServiceClient", clientTypeName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find client for %s", serviceType.getName()), e);
        }
    }

    public static Class<? extends TServiceClientFactory> clientFactoryFromService(Class<?> serviceType) {
        String clientFactoryTypeName = serviceType.getName() + CLIENT_FACTORY_TAG;
        try {
            Class<?> clazz = CL.loadClass(clientFactoryTypeName);
            if (TServiceClient.class.isAssignableFrom(clazz)) {
                return (Class<? extends TServiceClientFactory>) clazz;
            }
            throw new IllegalArgumentException(String.format("%s is not a TServiceClientFactory", clientFactoryTypeName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find client factory for %s", serviceType.getName()), e);
        }
    }
}
