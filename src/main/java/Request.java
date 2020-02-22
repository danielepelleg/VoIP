import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Request Class
 * Store the request type of SIP Client.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class Request {
    public static byte[] INVITE = getInvite();
    public static byte[] ACK = getAck();
    public static byte[] BYE = getBye();

    public Request(){
    }

    public static byte[] getInvite() {
        try {
            String invite = Files.readString(Paths.get("invite.txt"), StandardCharsets.UTF_8);
            return invite.getBytes();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new byte[1024];
    }

    public static byte[] getAck() {
        try {
            String ack = Files.readString(Paths.get("ack.txt"), StandardCharsets.UTF_8);
            return ack.getBytes();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new byte[1024];
    }

    public static byte[] getBye() {
        try {
            String bye = Files.readString(Paths.get("bye.txt"), StandardCharsets.UTF_8);
            return bye.getBytes();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new byte[1024];
    }
}
