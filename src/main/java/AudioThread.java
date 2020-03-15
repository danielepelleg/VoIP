import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

/**
 * AudioThread Class
 *
 * The AudioThread class is instantiated once the Client send the first ACK, and the
 *  real time communication has been set. The class implements the Runnable interface
 *  because it is instantiated inside a thread. The class usually receives the packets
 *  from the UserAgent, edit some byte in the payload, and send it back to it.
 *
 * The RTP connection is set on localhost, on the port 4080 and 4070. On the first one,
 *  called source port, the UserAgent Bob receives the data, so the Client use it to send
 *  RTP packets; the second one, called destination port, is used by the Client to receive
 *  packets, so the UserAgent Bob use it to sends packets. The connection has also the
 *  DatagramSocket attributes, which are the channels used for the communication, and a
 *  boolean value activeCall, which is set as true once the RTP flow starts.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class AudioThread implements Runnable {
    private static int sourcePort = 4080;
    private static int destinationPort = 4070;
    public static DatagramSocket socketOutgoing = UserAgent.getSocketOutgoing();
    public static DatagramSocket socketIncoming = getSocketIncoming();
    public static Boolean activeCall = false;

    /**
     * Get the Datagram Socket Incoming used to receive data from the UserAgent.
     *
     * @return the Datagram Socket
     */
    public static DatagramSocket getSocketIncoming() {
        try {
            return new DatagramSocket(destinationPort, UserAgent.getAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set the call state, true if the call is Active,
     *  false otherwise.
     *
     * @param state the state of the call
     */
    public static void setActiveCall(Boolean state) {
        activeCall = state;
    }

    /**
     * Send audio in a byte request.
     *
     * @param audio the audio to send
     */
    public static void sendAudio(byte[] audio) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(audio, audio.length, UserAgent.getAddress(), sourcePort);
            socketOutgoing.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send an Audio file in byte to the Server mjUA_1.8
     */
    public static void sendFile() {
        try {
            RTPHeader rtpHeader = new RTPHeader();
            byte [] rtpMessage = new byte[172];
            byte[] rtpBody = new byte[160];
            File audioFile = new File("src/main/resources/audio/imperial_march.wav");
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(audioFile));
            double nosofpackets = Math.ceil(((int) audioFile.length()) / 160);
            for (double i = 0; i < nosofpackets + 1; i++) {
                bis.read(rtpBody, 0, rtpBody.length);
                //System.out.println("Packet:" + (i + 1));
                System.arraycopy(rtpHeader.getHeader(), 0, rtpMessage, 0, 12);
                System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);
                rtpHeader.incrementSequence();
                rtpHeader.incrementTimeStamp();
                sendAudio(rtpMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a sinusoidal wave in a RTP packet. The sinusoid is generated mathematically, choosing a
     *  starting frequency and taking the period. It is then zipped through a PCMU algorithm
     *  to permit the UserAgent to correctly unzip it and reproduce its original sound.
     */
    private void sendSinusoidal() {
        RTPHeader rtpHeader = new RTPHeader();
        byte[] rtpBody = new byte[160];
        byte [] rtpMessage = new byte[172];
        float time = 0;
        while (activeCall) {
            for (int i = 0; i < 160; i++) {
                double sinusoid = 256 * Math.sin(Math.toRadians(time));
                time += 0.008;
                rtpBody[i] = (byte) sinusoid;
            }
            System.arraycopy(rtpHeader.getHeader(), 0, rtpMessage, 0, 12);
            System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);
            rtpHeader.incrementSequence();
            rtpHeader.incrementTimeStamp();
            sendAudio(rtpMessage);
        }
    }


    /**
     * Receive RTP datagram packet. An RTP Datagram Packet is made of a 12byte RTP Header and
     *  a 160byte of payload, which contains the audio itself. The compression algorithm is usually
     *  PCMA (used in Europe for the telephone's network) or the PCMU. Both stands for 1 byte for sample.
     *  This means 8000 sample per second, so 8000 byte per second.
     *
     *  How many byte in every packet? It depends from sender to sender. The more byte in every packet, the more
     *  I have to wait to send it to the receiver, so it brings delay to the conversation. In the telephone network
     *  usually send 20ms of audio in every packet, so -> # BYTE = 8000*20ms = 8000*20/1000 = 160 byte in every packet.
     *
     *  Once the packet has been received, take the payload and edit (randomly) the byte array, by replacing every
     *  number in an array randomly. Sending it back, the UserAgent plays the audio in the packet, which will be a
     *  disturbed sound, like a distorted rumor.
     *
     *  These compression's algorithms reduce the dynamic range of an audio signal.
     *
     */
    public static void receiveAudio() {
        byte[] response = new byte[172];
        byte[] toSend;
        try {
            DatagramPacket received = new DatagramPacket(response, response.length, UserAgent.getAddress(), destinationPort);
            while (activeCall) {
                socketIncoming.receive(received);
                toSend = received.getData();
                Random random = new Random();
                for (int i = 1; i < 120; i++)
                    toSend[random.nextInt(160) + 12] = (byte) random.nextInt();
                sendAudio(toSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sendSinusoidal();
    }
}
