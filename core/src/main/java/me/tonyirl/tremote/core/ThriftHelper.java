package me.tonyirl.tremote.core;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;

/**
 * @author tony.zhuby
 * @date 2020/11/16
 */
@UtilityClass
public class ThriftHelper {
    public static final String IFACE_SUFFIX = "$Iface";
    public static final String PROCESSOR_SUFFIX = "$Processor";
    public static final int IFACE_SUFFIX_SIZE = IFACE_SUFFIX.length();
    public static final String CLIENT_SUFFIX = "$Client";
    public static final String CLIENT_FACTORY_SUFFIX = "$Client$Factory";
    private static final ClassLoader CL = ThriftHelper.class.getClassLoader();

    public Class<?> ifaceFromService(@NonNull Class<?> serviceType) {
        String ifaceTypeName = serviceType.getName() + IFACE_SUFFIX;
        try {
            return CL.loadClass(ifaceTypeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find iface for %s", serviceType.getName()), e);
        }
    }

    public Class<?> serviceFromIface(@NonNull Class<?> ifaceType) {
        String ifaceTypeName = ifaceType.getName();
        if (!ifaceTypeName.endsWith(IFACE_SUFFIX)) {
            throw new IllegalArgumentException(String.format("%s is not a thrift iface!", ifaceType.getName()));
        }
        String serviceTypeName = ifaceTypeName.substring(0, ifaceTypeName.length() - IFACE_SUFFIX_SIZE);
        try {
            return CL.loadClass(serviceTypeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find base service of type %s", serviceTypeName), e);
        }
    }

    public Class<? extends TProcessor> processorFromService(@NonNull Class<?> serviceType) {
        String processorTypeName = serviceType.getName() + PROCESSOR_SUFFIX;
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

    public Class<?> ifaceFromServiceInstance(@NonNull Object instance) {
        Class<?> serviceType = instance.getClass();
        Class<?>[] interfaces = serviceType.getInterfaces();
        for (Class<?> i : interfaces) {
            if (i.getName().endsWith(IFACE_SUFFIX)) {
                return i;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not a thrift iface!", instance.getClass().getName()));
    }

    public Class<?> serviceFromServiceInstance(@NonNull Object instance) {
        Class<?> providerType = instance.getClass();
        Class<?>[] interfaces = providerType.getInterfaces();
        for (Class<?> i : interfaces) {
            if (i.getName().endsWith(IFACE_SUFFIX)) {
                String ifaceTypeName = i.getName();
                try {
                    return CL.loadClass(ifaceTypeName.substring(0, ifaceTypeName.length() - IFACE_SUFFIX_SIZE));
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(String.format("cannot find base service of type %s", providerType.getName()), e);
                }
            }
        }
        throw new IllegalArgumentException(String.format("%s is not a thrift iface!", instance.getClass().getName()));
    }

    public Class<? extends TServiceClient> clientFromService(@NonNull Class<?> serviceType) {
        String clientTypeName = serviceType.getName() + CLIENT_SUFFIX;
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

    public Class<? extends TServiceClientFactory<?>> clientFactoryFromService(@NonNull Class<?> serviceType) {
        String clientFactoryTypeName = serviceType.getName() + CLIENT_FACTORY_SUFFIX;
        try {
            Class<?> clazz = CL.loadClass(clientFactoryTypeName);
            if (TServiceClientFactory.class.isAssignableFrom(clazz)) {
                return (Class<? extends TServiceClientFactory<?>>) clazz;
            }
            throw new IllegalArgumentException(String.format("%s is not a TServiceClientFactory", clientFactoryTypeName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("cannot find client factory for %s", serviceType.getName()), e);
        }
    }
}
