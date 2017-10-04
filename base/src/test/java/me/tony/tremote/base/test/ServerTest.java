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
        DemoService.Iface provider = new DemoServiceImpl();

        ServerConfig sc = new ServerConfig();
        sc.setPort(11101);
        sc.setProtocol(pc);
        sc.setServerType(ServerConfig.ServerType.SELECTOR);
        sc.setTimeout(100);
        Server<DemoService> server = new Server<>(sc, DemoService.class, provider);
        server.serve();

        ServerConfig scHSHA = new ServerConfig();
        scHSHA.setPort(11102);
        scHSHA.setProtocol(pc);
        scHSHA.setServerType(ServerConfig.ServerType.HSHA);
        scHSHA.setTimeout(100);
        Server<DemoService> serverHSHA = new Server<>(scHSHA, DemoService.class, provider);
        serverHSHA.serve();

        ServerConfig scNB = new ServerConfig();
        scNB.setPort(11103);
        scNB.setProtocol(pc);
        scNB.setServerType(ServerConfig.ServerType.NONBLOCKING);
        scNB.setTimeout(100);
        Server<DemoService> serverNB = new Server<>(scNB, DemoService.class, provider);
        serverNB.serve();

        ServerConfig scPOOL = new ServerConfig();
        scPOOL.setPort(11104);
        scPOOL.setProtocol(pc);
        scPOOL.setServerType(ServerConfig.ServerType.POOL);
        scPOOL.setTimeout(100);
        Server<DemoService> serverPOOL = new Server<>(scPOOL, DemoService.class, provider);
        serverPOOL.serve();

        ServerConfig scSIMPLE = new ServerConfig();
        scSIMPLE.setPort(11105);
        scSIMPLE.setProtocol(pc);
        scSIMPLE.setServerType(ServerConfig.ServerType.SIMPLE);
        scSIMPLE.setTimeout(100);
        Server<DemoService> serverSIMPLE = new Server<>(scSIMPLE, DemoService.class, provider);
        serverSIMPLE.serve();
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
