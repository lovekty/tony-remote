package me.tony.tremote.base.test;

import me.tony.tremote.base.config.protocol.ProtocolConfig;
import me.tony.tremote.base.config.server.ServerConfig;
import me.tony.tremote.base.server.Server;
import me.tony.tremote.base.test.gen.DemoService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import java.io.IOException;

public class ServerTest {

    public static void main(String[] args) {
        ProtocolConfig pc = new ProtocolConfig();
        pc.setProtocolType(ProtocolConfig.ProtocolType.BINARY);
        ServerConfig sc = new ServerConfig();
        sc.setPort(11101);
        sc.setProtocol(pc);
        sc.setServerType(ServerConfig.ServerType.SELECTOR);
        sc.setTimeout(100);
        Server<DemoService> server = new Server<>(sc, DemoService.class, new DemoServiceImpl());
        server.serve();
    }

    @Test
    public void testClient() throws TException, IOException {
//        TTransport transport = new TSocket("localhost", 11101, 20);
//        transport.open();
        TTransport transport = new TFramedTransport(new TSocket("localhost", 11101, 20));
        transport.open();
        DemoService.Client client = new DemoService.Client(new TBinaryProtocol(transport));
        System.out.println(client.getName("Tony"));
    }
}
