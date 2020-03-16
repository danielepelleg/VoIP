import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Session Class
 *
 * The Session class stores the logs (information) about the VoIP conversation
 *  between the UserAgent (SIP Client) and mjUA_1.8 (SIP Server).
 *  It stores the requests sent by the client, the responses sent and received from
 *  the server and the datagram Packets sent through the socket connection.
 *  It has a method to record the conversation in a WireShark capture.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public abstract class Session {
    private static List<byte[]> requests = new ArrayList<>();
    private static List<byte[]> responses = new ArrayList<>();
    private static List<DatagramPacket> packets = new ArrayList<>();

    public Session(){}

    public static List<byte[]> getRequests() {
        return requests;
    }

    public static List<byte[]> getResponses() {
        return responses;
    }

    public static List<DatagramPacket> getPackets() {
        return packets;
    }

    /**
     * Add a Request to the requests List
     *
     * @param newRequest the request to add
     */
    public static void addRequest(byte[] newRequest){
        requests.add(newRequest);
    }

    /**
     * Add a Response to the responses List
     *
     * @param newResponse the response to add
     */
    public static void addResponse(byte[] newResponse){
        responses.add(newResponse);
    }

    /**
     *
     * @param newPacket the packet to add
     */
    public static void addPacket(DatagramPacket newPacket){
        packets.add(newPacket);
    }

    /**
     * Save the Requests on a file
     */
    public static void saveRequestFile(String request, String name){
        request = request.substring(0, request.length()-1);
        try (PrintWriter out = new PrintWriter("src/main/resources/requests/"+name+".txt")){
            out.flush();
            out.println(request);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Record the VoIP conversation's flow in a WireShark capture.
     */
    public void save(){


    }
}
