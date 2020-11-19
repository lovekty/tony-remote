package me.tonyirl.tremote.core;

import lombok.experimental.UtilityClass;
import org.apache.curator.x.discovery.LocalIpFilter;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;

import java.net.Inet4Address;

/**
 * @author tony.zhuby
 */
@UtilityClass
public class CuratorHelper {

    private final LocalIpFilter defaultFilter = ServiceInstanceBuilder.getLocalIpFilter();

    public void useDefaultLocalIpFilter() {
        ServiceInstanceBuilder.setLocalIpFilter(defaultFilter);
    }

    public void ipv4Only() {
        ServiceInstanceBuilder.setLocalIpFilter((nif, adr) -> defaultFilter.use(nif, adr) && adr instanceof Inet4Address);
    }

}
