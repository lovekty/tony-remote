package me.tonyirl.tremote.core.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

/**
 * @author tony.zhuby
 */
public class CommonTest {

    @SneakyThrows
    @Test
    public void testLocalHost() {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("address:" + new String(localHost.getAddress()));
        System.out.println("hostname:" + localHost.getHostName());
        System.out.println("host address:" + localHost.getHostAddress());
        System.out.println("canonical hostname:" + localHost.getCanonicalHostName());
    }
}
