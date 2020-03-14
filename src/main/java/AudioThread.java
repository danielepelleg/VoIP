import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Random;

import org.zoolu.sound.codec.g711.G711Encoding;

public class AudioThread implements Runnable {
    private static int sourcePort = 4080;
    private static int destinationPort = 4070;
    public static DatagramSocket socketOutgoing = UserAgent.getSocketOutgoing();
    public static DatagramSocket socketIncoming = getSocketIncoming();
    public static Boolean activeCall = false; //to handle the RTP session

    public static DatagramSocket getSocketIncoming() {
        try {
            return new DatagramSocket(destinationPort, UserAgent.getAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * set if the program can start or when finish the session RTP
     *
     * @param state the state of the call
     */
    public static void setActiveCall(Boolean state) {
        activeCall = state;
    }


    public static void sendAudio(byte[] request) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(request, request.length, UserAgent.getAddress(), sourcePort);
            socketOutgoing.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendSinuosiudal() {
        RTPHeader rtpHeader = new RTPHeader();
        byte[] rtpBody = new byte[160];
        byte [] rtpMessage = new byte[172];
        float time = 0;
        while (activeCall) {
           // rtpHeader.printHeader();

            for (int i = 0; i < 160; i++) {
                double sinusoid = 256 * Math.sin(Math.toRadians(time));
                time += 0.005;
                rtpBody[i] = (byte) sinusoid;
            }
            System.arraycopy(rtpHeader.getHeader(), 0, rtpMessage, 0, 12);
            System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);
            rtpHeader.incrementSequence();
            rtpHeader.incrementTimeStamp();

          //  System.out.println("----------");
            sendAudio(rtpMessage);
            //new Scanner(System.in).nextLine();
        }
    }


    /**
     * Send a RTP dataragram packet for the audio to the Server mjUA 1.8
     *
     * @param
     * @return
     */
    public static void receiveAudio() {
        byte[] response = new byte[172];
        byte[] toSend;
        try {
            DatagramPacket received = new DatagramPacket(response, response.length, UserAgent.getAddress(), destinationPort);
            while (activeCall) {
                socketIncoming.receive(received);
                //System.out.println(new String(received.getData()));
                toSend = received.getData();
                Random random = new Random();
                for (int i = 1; i < 120; i++)
                    toSend[random.nextInt(160) + 12] = (byte) random.nextInt();
                sendAudio(toSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return response;
    }

    @Override
    public void run() {
        sendSinuosiudal();
    }
}
