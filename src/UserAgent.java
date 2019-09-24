import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import it.unipr.netsec.ipstack.analyzer.LibpcapSniffer;
import it.unipr.netsec.ipstack.analyzer.LibpcapHeader;
import it.unipr.netsec.ipstack.analyzer.Sniffer;
import it.unipr.netsec.ipstack.ip4.Ip4Address;
import it.unipr.netsec.ipstack.ip4.Ip4Prefix;
import it.unipr.netsec.ipstack.ip4.SocketAddress;
import it.unipr.netsec.ipstack.link.EthTunnelInterface;
import it.unipr.netsec.ipstack.net.Address;
import it.unipr.netsec.ipstack.net.LoopbackInterface;
import it.unipr.netsec.ipstack.net.NetInterface;
import it.unipr.netsec.nemo.ip.Ip4Host;
import it.unipr.netsec.nemo.ip.Ip4Router;
import it.unipr.netsec.nemo.ip.IpLink;
import it.unipr.netsec.nemo.link.DataLink;
import it.unipr.netsec.nemo.link.PromiscuousLinkInterface;
import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;

import static org.pcap4j.core.Pcaps.getDevByAddress;

public class UserAgent {
    public static void main(String[] args) throws IOException, PcapNativeException, TimeoutException, NotOpenException {
        String invite = Files.readString(Paths.get("invite.txt"), StandardCharsets.UTF_8);
        System.out.println(invite);
        // TODO ACK
        // TODO Error management on received packages
        byte[] send = invite.getBytes();
        byte[] receive = new byte[1024];

        InetAddress address = InetAddress.getByName("127.0.0.1");

        int length = send.length;
        int port1 = 5080;
        int port2 = 5070;

        // TODO libcap not working
        DataLink link = new DataLink();

        DatagramPacket alice = new DatagramPacket(send, length, address, port1);
        DatagramPacket bob = new DatagramPacket(receive, receive.length, address, port2);

        DatagramSocket socket_port1 = new DatagramSocket();
        DatagramSocket socket_port2 = new DatagramSocket(port2, address);

        LoopbackInterface loopback = new LoopbackInterface(new SocketAddress(new Ip4Address(address), port1));
       // new PromiscuousLinkInterface(address)
        new LibpcapSniffer(loopback, LibpcapHeader.LINKTYPE_IPV4,"Johhny.pcap");

        socket_port1.send(alice);
        socket_port2.receive(bob);
        socket_port2.receive(bob);
    }
}
