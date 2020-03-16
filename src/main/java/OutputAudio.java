import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public abstract class OutputAudio {

    private static int sourcePort = 4080;
    public static DatagramSocket socketOutgoing = UserAgent.getSocketOutgoing();
    public static Boolean activeCall = false;

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

    public static boolean getActiveCall(){
        return activeCall;
    }

}
