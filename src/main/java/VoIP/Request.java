package VoIP;

import java.util.Random;

/**
 * VoIP.Request Class
 * Store the request type of SIP Client.
 * - INVITE VoIP.Request
 * - ACK VoIP.Request
 * - BYE VoIP.Request
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public abstract class Request {
    private static String CALL_ID = generateCallID();
    private static String SENDER_NAME = "Alice";
    private static String SENDER_TAG = generateSenderTag();
    private static String receiverTag;

    public static String getCallId(){
        return CALL_ID;
    }

    public static String getReceiverTag() {
        return receiverTag;
    }

    /**
     * Generates a random CallID
     */
    public static String generateCallID() {
        if (CALL_ID != null)
            return CALL_ID;
        else {
            Random random = new Random();
            String identifier = "";
            for (int i = 0; i < 12; i++) {
                identifier += random.nextInt(10);
            }
            return identifier + "@127.0.0.1";
        }
    }

    /**
     * Capitalize the first letter of the value
     * and set the new Sender Name
     *
     * @param newValue the new sender name
     */
    public static void setSenderName(String newValue){
        String firstChar = newValue.charAt(0)+"";
        String otherChar = "";
        for (int index = 1; index < newValue.length(); index++){
            otherChar += newValue.charAt(index);
        }
        otherChar = otherChar.toLowerCase();
        SENDER_NAME = firstChar + otherChar;
    }

    /**
     * Generates a random Sender Tag
     */
    public static String generateSenderTag() {
        if (SENDER_TAG != null)
            return  SENDER_TAG;
        else {
            Random random = new Random();
            String identifier = "tag=";
            for (int i = 0; i < 12; i++) {
                identifier += random.nextInt(10);
            }
            return identifier;
        }
    }

    /**
     * Generates a random Branch Tag, keeping an initial
     * "magic number" such as <i>z9hG4bK</i>  for been identified by Bob
     *  as a trusted branch
     */
    public static String generateBranch() {
        Random random = new Random();
        String magicNumber = "z9hG4bK";
        StringBuilder identifier = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        for(int i = 0; i < 8; i++ ){
            identifier.append(characters.charAt(random.nextInt(characters.length())));
        }
        return "branch="+magicNumber+identifier;
    }

    /**
     * Set the receiverTag attribute after have received it from Bob
     *  in the 200 OK VoIP.Response.
     *
     * @param tag the receiver tag
     */
    public static void setReceiverTag(String tag){
        receiverTag = "tag=" + tag;
    }

    /**
     * Get the Invite VoIP.Request once have set the callID, the senderTag and a
     *  pseudo-random generated branch
     */
    public static byte[] getInvite() {
        String invite = "INVITE sip:bob@127.0.0.1:5080 SIP/2.0\n" +
                "Via: SIP/2.0/UDP 127.0.0.1:5070;"+ generateBranch() +"\n" +
                "Max-Forwards: 70\n" +
                "To: \"Bob\" <sip:bob@127.0.0.1:5080>\n" +
                "From: \""+ SENDER_NAME +"\" <sip:" + SENDER_NAME.toLowerCase() +"@127.0.0.1:5070>;"+ SENDER_TAG +"\n" +
                "Call-ID: "+ CALL_ID +"\n" +
                "CSeq: 1 INVITE\n" +
                "Contact: <sip:" + SENDER_NAME.toLowerCase() + "@127.0.0.1:5070>\n" +
                "Expires: 3600\n" +
                "User-Agent: mjsip 1.8\n" +
                "Supported: 100rel,timer\n" +
                "Allow: INVITE,ACK,OPTIONS,BYE,CANCEL,INFO,PRACK,NOTIFY,MESSAGE,UPDATE\n" +
                "Content-Length: 129\n" +
                "Content-Type: application/sdp\n" +
                "\n" +
                "v=0\n" +
                "o=alice 0 0 IN IP4 127.0.0.1\n" +
                "s=-\n" +
                "c=IN IP4 127.0.0.1\n" +
                "t=0 0\n" +
                "m=audio 4070 RTP/AVP 0 8\n" +
                "a=rtpmap:0 PCMU/8000\n" +
                "a=rtpmap:8 PCMA/8000\n";
        Session.saveRequestFile(invite, "invite");
        return invite.getBytes();
    }

    /**
     * Get the ACK VoIP.Request after have set the Receiver Tag for uniquely identify
     * the VoIP.UserAgent b (Bob).
     */
    public static byte[] getAck(){
        String ack = "ACK sip:bob@127.0.0.1:5080 SIP/2.0\n" +
                "Via: SIP/2.0/UDP 127.0.0.1:5070;"+ generateBranch() +"\n" +
                "Max-Forwards: 70\n" +
                "To: \"Bob\" <sip:bob@127.0.0.1:5080>;"+ receiverTag +"\n" +
                "From: \"" + SENDER_NAME +"\" <sip:"+ SENDER_NAME.toLowerCase() + "@127.0.0.1:5070>;"+ SENDER_TAG +"\n" +
                "Call-ID: "+ CALL_ID +"\n" +
                "CSeq: 1 ACK\n" +
                "Contact: <sip:"+ SENDER_NAME.toLowerCase() + "@127.0.0.1:5070>\n" +
                "Expires: 3600\n" +
                "User-Agent: mjsip 1.8\n" +
                "Content-Length: 0\r\n\n";
        Session.saveRequestFile(ack, "ack");
        return ack.getBytes();
    }

    /**
     * Get the BYE VoIP.Request after have set the Receiver Tag for uniquely identify
     * the VoIP.UserAgent b (Bob).
     */
    public static byte[] getBye() {
        String bye = "BYE sip:bob@127.0.0.1:5080 SIP/2.0\n" +
                "Via: SIP/2.0/UDP 127.0.0.1:5070;"+ generateBranch() +"\n" +
                "Max-Forwards: 70\n" +
                "To: \"Bob\" <sip:bob@127.0.0.1:5080>;"+ receiverTag +"\n" +
                "From: \"" + SENDER_NAME + "\" <sip:" + SENDER_NAME.toLowerCase() + "@127.0.0.1:5070>;"+ SENDER_TAG +"\n" +
                "Call-ID: "+ CALL_ID +"\n" +
                "CSeq: 2 BYE\n" +
                "User-Agent: mjsip 1.8\n" +
                "Content-Length: 0\r\n\n";
        Session.saveRequestFile(bye, "bye");
        return bye.getBytes();
    }
}
