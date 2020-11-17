package me.tonyirl.tremote.core.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tonyirl.tremote.core.ServiceInfo;
import me.tonyirl.tremote.core.ServicePublisher;
import me.tonyirl.tremote.core.test.thrift.DemoService;
import me.tonyirl.tremote.core.test.thrift.DemoStruct;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.LocalIpFilter;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;

import java.net.Inet4Address;

/**
 * @author tony.zhuby
 */
@Slf4j
public class ServerTest {


    static {
        final LocalIpFilter defaultFilter = ServiceInstanceBuilder.getLocalIpFilter();
        ServiceInstanceBuilder.setLocalIpFilter((nif, adr) -> defaultFilter.use(nif, adr) && adr instanceof Inet4Address);
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zk.start();
        zk.blockUntilConnected();
        ServiceDiscovery<ServiceInfo> discovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class).basePath("/tremote-services").client(zk).build();
        discovery.start();
        ServiceInfo si = new ServiceInfo("my-demo-service");
        ServicePublisher<Impl> p = new ServicePublisher<>(new Impl("service-instance-1"), si, discovery);
        p.publish();
        log.info("publish service:{} success", si);

        ServiceInfo si1 = new ServiceInfo("my-demo-service");
        si1.setPort(8888);
        ServicePublisher<Impl> p1 = new ServicePublisher<>(new Impl("service-instance-2"), si1, discovery);
        p1.publish();
        log.info("publish service:{} success", si1);
    }


    @RequiredArgsConstructor
    public static class Impl implements DemoService.Iface {

        private final String name;

        @Override
        public DemoStruct queryByKey(String key) {
            DemoStruct ret = new DemoStruct();
            ret.setKey(key);
            ret.setCode(key.hashCode());
            ret.setName("name:" + key);
            log.info(name + " provide service:query by key:{}, result:{}", key, ret);
            return ret;
        }
    }
}
