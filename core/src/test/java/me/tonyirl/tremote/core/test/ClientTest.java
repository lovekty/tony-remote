package me.tonyirl.tremote.core.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.tonyirl.tremote.core.ClientEnhancer;
import me.tonyirl.tremote.core.ClientHolder;
import me.tonyirl.tremote.core.ClientProxy;
import me.tonyirl.tremote.core.ServiceInfo;
import me.tonyirl.tremote.core.test.thrift.DemoService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

/**
 * @author tony.zhuby
 */
@Slf4j
public class ClientTest {

    private static final ServiceInfo si = new ServiceInfo("my-demo-service");
    private static ClientHolder<DemoService.Iface> holder;

    @SneakyThrows
    @BeforeAll
    public static void init() {
        CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zk.start();
        zk.blockUntilConnected();
        ServiceDiscovery<ServiceInfo> discovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class).basePath("/tremote-services").client(zk).build();
        discovery.start();
        ServiceProvider<ServiceInfo> provider = discovery.serviceProviderBuilder().serviceName(si.defineName()).build();
        provider.start();
        holder = new ClientHolder<>(DemoService.Iface.class, si, provider);
    }

    @Test
    public void testJdkProxy() throws Exception {
        DemoService.Iface iface = new ClientProxy<>(DemoService.Iface.class, holder).getProxyInstance();
        log.info("result ======> {}", iface.queryByKey("hahaha"));
    }

    @Test
    public void testCglib() throws Exception {
        DemoService.Iface iface = new ClientEnhancer<>(DemoService.Iface.class, holder).create();
        log.info("result ======> {}", iface.queryByKey("hohoho"));
    }

    @Test
    public void testJdkProxyLoop() {
        DemoService.Iface iface = new ClientProxy<>(DemoService.Iface.class, holder).getProxyInstance();
        IntStream.range(0, 10).forEach(index -> {
            try {
                log.info("index:{} result ======> {}", index, iface.queryByKey("hahaha"));
            } catch (TException e) {
                log.error("index:{} error", index, e);
            }
        });
    }
}
