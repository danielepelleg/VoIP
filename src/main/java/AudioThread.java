import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Random;

import org.zoolu.sound.codec.G711;
import org.zoolu.sound.codec.g711.G711Encoding;

public class AudioThread implements Runnable {
    private static int destinationPort = 4070;
    public static DatagramSocket socketIncoming = getSocketIncoming();
     //to handle the RTP session

    public static DatagramSocket getSocketIncoming() {
        try {
            return new DatagramSocket(destinationPort, UserAgent.getAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param state the state of the call
     */

<<<<<<< HEAD

=======
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
>>>>>>> c0336782d68f2a73b982922bf6b8d2b1fd12422c

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

<<<<<<< HEAD


=======
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
>>>>>>> c0336782d68f2a73b982922bf6b8d2b1fd12422c


    /**
     *
     */
    public static void receiveAudio() {
        byte[] response = new byte[172];
        byte[] toSend;
        try {
            DatagramPacket received = new DatagramPacket(response, response.length, UserAgent.getAddress(), destinationPort);
            while (OutputAudio.getActiveCall()) {
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

    @Override
    public void run() {
        receiveAudio();
    }
}
