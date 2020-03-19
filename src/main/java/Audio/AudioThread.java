package Audio;

import VoIP.RTPPacket;
import VoIP.UserAgent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
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
 *  with random values and send it back to the original sender. These two actions
 *  are done simultaneously, that's why it needs to be used in a thread.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class AudioThread implements Runnable {
    private static int destinationPort = 4070;
    public static DatagramSocket socketIncoming = getSocketIncoming();

    /**
     * Get the Datagram Socket Incoming used to receive data from the VoIP.UserAgent.
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
     * Send an Audio file in byte to the Server mjUA_1.8
     */
    public static void sendFile() {
        try {
            RTPPacket rtpHeader = new RTPPacket();
            byte [] rtpMessage = new byte[172];
            byte[] rtpBody = new byte[160];
            int bytesRead = 0;
            File audioFile = new File("src/main/resources/audio/imperial_march.wav");

            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);      // Insert the audio in a InputStream
            ais = AudioSystem.getAudioInputStream(AudioFormat.Encoding.ULAW, ais);  // Remove the WAV Header

            double nosofpackets = Math.ceil(((int) audioFile.length()) / 160);      // Number of packets to be created

            AudioFormat audioFormat =
                    new AudioFormat(8000, 8, 1, true, false);
            double sleepTime = (160/audioFormat.getSampleRate());
            long sleepTimeMillis= (long)(sleepTime*1000);           // Initialize the Audio Format to get the sleep time


            while ((bytesRead = ais.read(rtpBody, 0, rtpBody.length))!= -1) {           // Fill the RTPBody
                System.arraycopy(rtpHeader.getHeader(), 0, rtpMessage, 0, 12);
                System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);       // Create the RTP Message
                rtpHeader.incrementSequence();
                rtpHeader.incrementTimeStamp();
                OutputAudio.sendAudio(rtpMessage);
                Thread.sleep(sleepTimeMillis);                                            // Sleep 20ms
            }
        } catch (IOException | InterruptedException | UnsupportedAudioFileException e) {
            e.printStackTrace();
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
     *  number in an array randomly. Sending it back, the VoIP.UserAgent plays the audio in the packet, which will be a
     *  disturbed sound, like a distorted rumor.
     *
     *  These compression's algorithms reduce the dynamic range of an audio signal.
     */
    @Override
    public void run() {
            byte[] response = new byte[172];
            byte[] toSend;
            OutputAudio.setSendingAudio(true);                  // here set the active call for start the RTP flush
            try {
                DatagramPacket received = new DatagramPacket(response, response.length, UserAgent.getAddress(), destinationPort);
                while (OutputAudio.isSendingAudio()) {
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
