package VoIP;

import Audio.AudioThread;
import Audio.OutputAudio;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * VoIP.UserAgent Class
 *
 * The VoIP.UserAgent is the client of SIP (VoIP.Session Initiation Protocol). The UA sends
 * VoIP.Request objects to the server (mjUA_1.8) through the SocketSourcePort on port 5080,
 * and receives VoIP.Response objects through socketDestinationPort on port 5070, on the
 * loopback address.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class UserAgent implements Runnable{
    public static InetAddress address = getAddress();
    public static int sourcePort = 5080;
    public static int destinationPort = 5070;
    public static DatagramSocket socketOutgoing = getSocketOutgoing();
    public static DatagramSocket socketIncoming = getSocketIncoming();

    /**
     * Class Constructor
     */
    public UserAgent() {
    }

    /**
     * Set the loopback address for a local VoIP Communication.
     */
    public static InetAddress getAddress() {
        try {
            return InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set the Sockets Port for Outgoing stream.
     * Outgoing Socket is used to send Request bytes
     */
    public static DatagramSocket getSocketOutgoing() {
        try {
            return new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set the Sockets Port for Incoming stream.
     * Incoming Socket is used to receive Response bytes
     */
    public static DatagramSocket getSocketIncoming() {
        try {
            DatagramSocket incoming = new DatagramSocket(destinationPort, getAddress());
            incoming.setSoTimeout(25000);
            return incoming;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send a request in byte to the Server mjUA_1.8
     *
     * @param request the request to send (in byte)
     */
    public static void send(byte[] request) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(request, request.length, getAddress(), sourcePort);
            socketOutgoing.send(sendPacket);
            Session.addRequest(sendPacket.getData());
            Session.addPacket(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Receive a response in byte from the Server and print
     * the related message
     */
    public static void receive() {
        try {
            byte[] response = new byte[1024];
            DatagramPacket received = new DatagramPacket(response, response.length, address, destinationPort);
            socketIncoming.receive(received);
            Response.setResponsePacket(received);
            Response.showMessage();
            Session.addPacket(received);
            Session.addResponse(received.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listen for a new DatagramPacket on the Incoming DatagramSocket mjUA_1.8
     */
    public static DatagramPacket listen() throws SocketTimeoutException {
        try {
            byte[] response = new byte[1024];
            DatagramPacket received = new DatagramPacket(response, response.length, address, destinationPort);
            socketIncoming.receive(received);
            Session.addPacket(received);
            Session.addResponse(received.getData());
            return received;
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException(e.getMessage());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Make a VoIP to the VoIP.UserAgent Bob mjUA listening on port 5080
     */
    public static void start() {
        /*
        System.out.println(" INVITE MESSAGE ");
        System.out.println(new String(Request.getInvite()));
        System.out.println(" INVITE SENT ");
        send(Request.getInvite());
        Response.showMessage();

        new Scanner(System.in).next();

        //System.out.println("STOP AUDIO");
        //OutputAudio.setSendingAudio(false);

        //new Scanner(System.in).next();
        System.out.println(" BYE MESSAGE ");
        System.out.println(new String(Request.getBye()));
        send(Request.getBye());

        System.out.println(" BYE SENT "); //here set the active call for stop RTP
        Response.showMessage();
        */

    }

    @Override
    public void run() {
        send(Request.getInvite());
        Response.showMessage();
    }
}