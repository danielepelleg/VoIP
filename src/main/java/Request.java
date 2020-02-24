import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Request Class
 * Store the request type of SIP Client.
 * - INVITE Request
 * - ACK Request
 * - BYE Request
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class Request {
    public static byte[] INVITE = getInvite();
    public static byte[] ACK = getAck();
    public static byte[] BYE = getBye();

    /**
     * Class Default Constructor
     */
    public Request(){ }

    /**
     * Set the Receiver Tag in the ACK Request for uniquely identify
     * the UserAgent b (Bob)
     *
     * @param receiverTag Bob's tag, initialized once it receives the call
     */
    public static void setAck(String receiverTag) {
        String ack1 = "ACK sip:bob@127.0.0.1:5080 SIP/2.0\n" +
                "Via: SIP/2.0/UDP 127.0.0.1:5070;branch=z9hG4bK5c3663b7\n" +
                "Max-Forwards: 70\n" +
                "To: \"Bob\" <sip:bob@127.0.0.1:5080>;tag="+ receiverTag +"\n" +
                "From: \"Alice\" <sip:alice@127.0.0.1:5070>;tag=691822153216\n" +
                "Call-ID: 958219347383@127.0.0.1\n" +
                "CSeq: 1 ACK\n" +
                "Contact: <sip:alice@127.0.0.1:5070>\n" +
                "Expires: 3600\n" +
                "User-Agent: mjsip 1.8\n" +
                "Content-Length: 0\n";
        try(PrintWriter out = new PrintWriter("src/main/resources/requests/ack.txt")){
            out.flush();
            out.println(ack1);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Set the Receiver Tag in the BYE Request for uniquely identify
     * the UserAgent b (Bob)
     *
     * @param receiverTag Bob's tag, initialized once it receives the call
     */
    public static void setBye(String receiverTag) {
        String ack1 = "BYE sip:bob@127.0.0.1:5080 SIP/2.0\n" +
                "Via: SIP/2.0/UDP 127.0.0.1:5070;branch=z9hG4bK5c3663b7\n" +
                "Max-Forwards: 70\n" +
                "To: \"Bob\" <sip:bob@127.0.0.1:5080>;tag="+ receiverTag +"\n" +
                "From: \"Alice\" <sip:alice@127.0.0.1:5070>;tag=691822153216\n" +
                "Call-ID: 958219347383@127.0.0.1\n" +
                "CSeq: 2 BYE\n" +
                "User-Agent: mjsip 1.8\n" +
                "Content-Length: 0\n";
        try(PrintWriter out = new PrintWriter("src/main/resources/requests/bye.txt")){
            out.flush();
            out.println(ack1);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Read the invite.txt file
     *
     * @return the bytes of the string in the file
     */
    public static byte[] getInvite() {
        try {
            String invite = Files.readString(Paths.get("src/main/resources/requests/invite.txt"), StandardCharsets.UTF_8);
            return invite.getBytes();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new byte[1024];
    }

    /**
     * Read the ack.txt file
     *
     * @return the bytes of the string in the file
     */
    public static byte[] getAck() {
        try {
            String ack = Files.readString(Paths.get("src/main/resources/requests/ack.txt"), StandardCharsets.UTF_8);
            return ack.getBytes();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new byte[1024];
    }

    /**
     * Read the bye.txt file
     *
     * @return the bytes of the string in the file
     */
    public static byte[] getBye() {
        try {
            String bye = Files.readString(Paths.get("src/main/resources/requests/bye.txt"), StandardCharsets.UTF_8);
            return bye.getBytes();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new byte[1024];
    }
}
