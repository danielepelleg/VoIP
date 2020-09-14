package VoIP;

import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * VoIP.Listen4Close Class
 * This Class listen for any UA Bob request after the RTP connection has been set.
 *  It implements Runnable because it stays active for any Request as the program runs,
 *  it is instantiated in a Thread once Alice receives the 200 OK message and send the
 *  ACK message to start the RTP flux.
 *
 * The class has 2 attribute, the responsePacket, which is a DatagramPacket, and
 * a message, a byte created from the index 8 to 11 of the DatagramPacket Incoming.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class Listen4Close implements Runnable {
    private static DatagramPacket responsePacket;
    private static byte[] message;
    private static String serverAnswer;

    /**
     * Class Constructor
     *
     * @param packet the packet received in response
     */
    public Listen4Close(DatagramPacket packet) {
        responsePacket = packet;
        setMessage();
    }

    public Listen4Close() { }

    /**
     * Set the Message from the incoming Packet.
     * The message consists in the bytes of the packet from the 8th to the 11th index.
     * It's basically a integer of 3 numbers that describes the result of a request
     * or an action performed by the client VoIP.UserAgent.
     */
    public static void setMessage() {
        message = Arrays.copyOfRange(responsePacket.getData(), 8, 11);
    }

    /**
     * Set the new VoIP.Response packet. This method is used when the system receives
     * some information responses or messages, such as "100 TRYING".
     * <p>
     * In this case the application have to listen on the incoming port of the DatagramSocket
     * for a new message from the other VoIP.UserAgent, and then sets it as the new VoIP.Response Packet.
     *
     * @param response the new VoIP.Response Packet to set
     */
    public static void setResponsePacket(DatagramPacket response) {
        responsePacket = response;
        setMessage();
    }

    /**
     * Listen for Any Request sent by Bob and Close the Call.
     *  To close the call send and ACK message as a confirm to the BYE Request received.
     */
    public void run() {
        setResponsePacket(UserAgent.listenClose());
        serverAnswer = new String(message);
        UserAgent.send(Request.getAck());
        Program.controller.setConnectionLabel("BYE");
    }
}
