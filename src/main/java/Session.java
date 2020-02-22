import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

/**
 * Session Class
 *
 * The Session class stores the logs (information) about the VoIP conversation
 *  between the UserAgent (SIP Client) and mjua_1.8 (SIP Server).
 *  It stores the requests sent by the client, the responses sent and received from
 *  the server and the datagram Packets sent through the socket connection.
 *  It has a method to record the conversation in a WireShark capture.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class Session {
    private static List<Request> requests = new ArrayList<>();
    private static List<Response> responses = new ArrayList<>();
    private static List<DatagramPacket> packets = new ArrayList<>();

    public Session(){}

    public static List<Request> getRequests() {
        return requests;
    }

    public static List<Response> getResponses() {
        return responses;
    }

    public static List<DatagramPacket> getPackets() {
        return packets;
    }

    public static void addRequest(Request newRequest){
        requests.add(newRequest);
    }

    public static void addResponse(Response newResponse){
        responses.add(newResponse);
    }

    public static void addPacket(DatagramPacket newPacket){
        packets.add(newPacket);
    }

    /**
     * Record the VoIP conversation flow in a WireShark capture.
     */
    // TODO Ask for advice for a sniffer on the loopback interface
    public void save(){
        // code here

        // -- libcap not working --
        // -- DataLink link = new DataLink(); --

        // LoopbackInterface loopback = new LoopbackInterface(new SocketAddress(new Ip4Address(address), port1));
        // new LibpcapSniffer(loopback, LibpcapHeader.LINKTYPE_IPV4,"Johhny.pcap");
    }
}
