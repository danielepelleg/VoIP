package VoIP;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * VoIP.Session Class
 *
 * The VoIP.Session class stores the logs (information) about the VoIP conversation
 *  between the VoIP.UserAgent (SIP Client) and mjUA_1.8 (SIP Server).
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
    private static boolean active = false;

    /**
     * Get methods for the lists
     *
     * @return the list
     */
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
     * Set the value of the boolean attribute active,
     *  true if the VoIP.UserAgent has received a 200 OK after the Invite and has sent the ACK,
     *  and so the session is active, false otherwise.
     */
    public static void setActive(boolean value) {
        Session.active = value;
    }

    /**
     * Get method of the boolean value active, if true the session is active,
     *  if false the session is not active.
     *
     * @return active
     */
    public static boolean isActive() {
        return active;
    }

    /**
     * Add a VoIP.Request to the requests List
     *
     * @param newRequest the request to add
     */
    public static void addRequest(byte[] newRequest){
        requests.add(newRequest);
    }

    /**
     * Add a VoIP.Response to the responses List
     *
     * @param newResponse the response to add
     */
    public static void addResponse(byte[] newResponse){
        responses.add(newResponse);
    }

    /**
     * Add a Packet to the packets List
     *
     * @param newPacket the packet to add
     */
    public static void addPacket(DatagramPacket newPacket){
        packets.add(newPacket);
    }

    /**
     * Save the Requests on a file
     */
    public static void saveRequestFile(String request, String fileName){
        request = request.substring(0, request.length()-1);
        try (PrintWriter out = new PrintWriter("src/main/resources/requests/"+fileName+".txt")){
            out.flush();
            out.println(request);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear all the lists
     */
    public static void clear(){
        requests.clear();
        responses.clear();
        packets.clear();
    }
    /**
     * Save the VoIP conversation's flow in logs.txt file.
     */
    public static void save(){
        try (PrintWriter out = new PrintWriter("src/main/resources/requests/logs.txt")){
            out.flush();
            out.println(Session.logsMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforms a DatagramPacket into a String
     *
     * @param datagramPacket The DatagramPacket as input
     * @return The resulting String
     */
    public static String packetToString(DatagramPacket datagramPacket) {
        byte [] buffer = datagramPacket.getData();
        return new String(buffer, 0, datagramPacket.getLength());
    }

    /**
     * Returns a String combining every packet in a List of DatagramPackets with a delimiter after each packet
     *
     * @return The resulting String
     */
    public static String logsMessage() {
        return packets.stream().map(Session::packetToString).collect(Collectors.joining("---- End of Message ----\n\r"));
    }

}
