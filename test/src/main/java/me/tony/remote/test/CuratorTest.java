package me.tony.remote.test;

import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.stream.IntStream;

public class CuratorTest {

    public static void main(String[] args) {
        try {
            final var zk = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new RetryOneTime(1000));
            zk.start();

            final var discovery = ServiceDiscoveryBuilder
                    .builder(Void.class).basePath("/trservice").client(zk)
                    .build();
            discovery.start();

            IntStream.range(0, 5).forEach(i -> {
                ServiceInstance<Void> service;
                try {
                    service = ServiceInstance.<Void>builder().name("myservice").port(10086 + i).build();
                    discovery.registerService(service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            var provider = discovery.serviceProviderBuilder().serviceName("myservice").build();
            provider.start();

            IntStream.range(0,10).forEach(ignore -> {
                ServiceInstance<Void> instance = null;
                try {
                    instance = provider.getInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(instance);
            });
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
