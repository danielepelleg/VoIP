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
     * set if the program can start or when finish the session RTP
     *
     * @param state the state of the call
     */








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
            while (OutputAudio.getActiveCall()) {
                socketIncoming.receive(received);
                //System.out.println(new String(received.getData()));
                toSend = received.getData();
                Random random = new Random();
                for (int i = 1; i < 120; i++)
                    toSend[random.nextInt(160) + 12] = (byte) random.nextInt();
                OutputAudio.sendAudio(toSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return response;
    }

    @Override
    public void run() {
        receiveAudio();
    }
}
