package Audio;

import VoIP.UserAgent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

/**
 * Audio.AudioThread Class
 *
 * This class implements runnable because it must be instantiated inside a thread.
 *  Once the connection is set, the mjUA Bob sends RTP Packets to the UseraAgent on port
 *  4070, as specified in the invite request. Then, the UA edits some of its byte
 *  with random values and sends it back to the original sender. These two actions
 *  are done simultaneously, that's why it needs to be used in a thread.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class AudioThread implements Runnable {
    private int destinationPort = 4070;
    public DatagramSocket socketIncoming = getSocketIncoming();

    /**
     * Get the Datagram Socket Incoming used to receive data from the VoIP.UserAgent.
     *
     * @return the Datagram Socket
     */
    public DatagramSocket getSocketIncoming() {
        try {
            return new DatagramSocket(destinationPort, UserAgent.getAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Receive RTP datagram packet. An RTP Datagram Packet is made of a 12byte RTP Header and
     *  160byte of payload, which contains the audio itself. The compression algorithm is usually
     *  PCMA (used in Europe for the telephone's network) or PCMU. Both stands for 1 byte for sample.
     *  This means 8000 sample per second, so 8000 byte per second.
     *
     *  How many bytes in every packet? It depends from sender to sender. The more bytes in every packet, the more
     *  I have to wait to send it to the receiver, therefore it brings delay to the conversation. In the telephone network
     *  20ms of audio are usually sent in every packet, so -> # BYTE = 8000*20ms = 8000*20/1000 = 160 byte in every packet.
     *
     *  Once the packet has been received, take the payload and edit (randomly) the byte array, by replacing every
     *  number in an array randomly. Sending it back, the VoIP.UserAgent plays the audio in the packet, which will be a
     *  disturbed sound, like a distorted rumor.
     *
     *  These compression's algorithms reduce the dynamic range of an audio signal.
     */
    @Override
    public void run() {
            byte[] response = new byte[172];
            byte[] toSend;
            OutputAudio.setRunning(true);                  // here set the active call for start the RTP flush
            try {
                DatagramPacket received = new DatagramPacket(response, response.length, UserAgent.getAddress(), destinationPort);
                while (OutputAudio.isRunning()) {
                    socketIncoming.receive(received);
                    toSend = received.getData();
                    Random random = new Random();
                    for (int i = 1; i < 120; i++)
                        toSend[random.nextInt(160) + 12] = (byte) random.nextInt();
                    OutputAudio.sendAudio(toSend);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
