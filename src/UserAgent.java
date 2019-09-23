import java.io.IOException;
import java.net.*;
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
import it.unipr.netsec.ipstack.net.Packet;
import it.unipr.netsec.nemo.ip.Ip4Host;
import it.unipr.netsec.nemo.ip.Ip4Router;
import it.unipr.netsec.nemo.ip.IpLink;
import it.unipr.netsec.nemo.link.DataLink;
import it.unipr.netsec.nemo.link.PromiscuousLinkInterface;

public class UserAgent {
    public static void main(String[] args) throws IOException {
        String msg = ("INVITE sip:bob@127.0.0.1:5080 SIP/2.0\n" +
                "Via: SIP/2.0/UDP 160.78.175.190:5070;branch=z9hG4bK9c821e1a\n" +
                "Max-Forwards: 70\n" +
                "To: \"Bob\" <sip:bob@127.0.0.1:5080>\n" +
                "From: \"Alice\" <sip:alice@160.78.175.190:5070>;tag=634583768317\n" +
                "Call-ID: 591112818114@160.78.175.190\n" +
                "CSeq: 1 INVITE\n" +
                "Contact: <sip:alice@160.78.175.190:5070>\n" +
                "Expires: 3600\n" +
                "User-Agent: mjsip 1.8\n" +
                "Supported: 100rel,timer\n" +
                "Allow: INVITE,ACK,OPTIONS,BYE,CANCEL,INFO,PRACK,NOTIFY,MESSAGE,UPDATE\n" +
                "Content-Length: 147\n" +
                "Content-Type: application/sdp\n" +
                "\n" +
                "v=0\n" +
                "o=alice 0 0 IN IP4 160.78.175.190\n" +
                "s=-\n" +
                "c=IN IP4 160.78.175.190\n" +
                "t=0 0\n" +
                "m=audio 4070 RTP/AVP 0 8\n" +
                "a=rtpmap:0 PCMU/8000\n" +
                "a=rtpmap:8 PCMA/8000");
        // TODO Read string from file
        // TODO ACK
        // TODO Error management on received packages
        byte[] send = msg.getBytes();
        byte[] receive = new byte[1024];

        InetAddress address = InetAddress.getByName("127.0.0.1");
        PcapNewtworkInterface ni = Pcaps.getDevByAddress
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
