package Audio;

import VoIP.UserAgent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Output Audio
 *
 * This is the base-audio class. It stores all the necessary to set up the RTP Connection.
 *  It has the sourcePort attribute, which is the port used by the VoIP.UserAgent to send
 *  RTP Packets to the mjUA "Bob", and DatagramSocket socketOutgoing, previously initialized
 *  in the VoIP.UserAgent Class, used to send Requests to the other mjUA. Then it has boolean attribute
 *  sendingAudio, which is initially set as false, and turn to true once the VoIP.UserAgent starts to send audio.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public abstract class OutputAudio {
    private static int sourcePort = 4080;
    private static DatagramSocket socketOutgoing = UserAgent.getSocketOutgoing();
    private static volatile boolean isRunning = false;

    /**
     * Set sendingAudio
     *
     * @param value, true if the program is sending audio, false otherwise
     */
    public static void setSendingAudio(boolean value) {
        isRunning = value;
    }

    /**
     * Get method to sendingAudio boolean value
     *
     * @return the value of sendingAudio
     */
    public static boolean isSendingAudio(){
        return isRunning;
    }

    /**
     * Send audio (in a RTP packet, in bytes[]) to the mjUA "Bob"
     *
     * @param toSend the byte to send
     */
    public static void sendAudio(byte[] toSend) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, UserAgent.getAddress(), sourcePort);
            socketOutgoing.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
