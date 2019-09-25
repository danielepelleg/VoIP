import it.unipr.netsec.nemo.link.DataLink;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class UserAgent {
    public static void main(String[] args) throws IOException {
        // I read the content of invite.txt to find the INVITE message
        String invite = Files.readString(Paths.get("invite.txt"), StandardCharsets.UTF_8);
        String ack = Files.readString(Paths.get("ack.txt"), StandardCharsets.UTF_8);
        String bye = Files.readString(Paths.get("ack.txt"), StandardCharsets.UTF_8);

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

        //LoopbackInterface loopback = new LoopbackInterface(new SocketAddress(new Ip4Address(address), port1));
        //new LibpcapSniffer(loopback, LibpcapHeader.LINKTYPE_IPV4,"Johhny.pcap");

        socket_port1.send(alice);
        String serveAnswer;
        do {
            socket_port2.receive(bob);
            byte[] serveAnswerB = Arrays.copyOfRange(bob.getData(), 8 ,11);
            serveAnswer = new String(serveAnswerB);
        }while(serveAnswer.charAt(0) == '1');




    }
}
