import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * Response Class
 * Store the response of SIP Server -> mjUA_1.8
 * The class has 2 attribute, the responsePacket, which is a DatagramPacket, and
 * a message, a byte created from the index 8 to 11 of the DatagramPacket Incoming.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public abstract class Response {
    private static DatagramPacket responsePacket;
    private static byte[] message;

    /**
     * Class Constructor
     *
     * @param packet the packet received in response
     */
    public Response(DatagramPacket packet) {
        this.responsePacket = packet;
        setMessage();
    }

    /**
     * Set the Message from the incoming Packet.
     * The message consists in the bytes of the packet from the 8th to the 11th index.
     * It's basically a integer of 3 numbers that describes the result of a request
     * or an action performed by the client UserAgent.
     */
    public static void setMessage() {
        message = Arrays.copyOfRange(responsePacket.getData(), 8, 11);
    }

    /**
     * Set the new Response packet. This method is used when the system receives
     * some information responses or messages, such as "100 TRYING".
     *
     * In this case the application have to listen on the incoming port of the DatagramSocket
     * for a new message from the other UserAgent, and then sets it as the new Response Packet.
     *
     * @param response the new Response Packet to set
     */
    public static void setResponsePacket(DatagramPacket response) {
        responsePacket = response;
        setMessage();
    }

    public byte[] getMessage() {
        return message;
    }

    /**
     * Show the client the message he has received from the server
     * and a few lines of a description. Send ACK if needed.
     */
    public static void showMessage() {
        // Initialize a counter to set a newResponse after an Information Message.


        // String containing the Server Answer
        String serverAnswer;

        do {
            setResponsePacket(UserAgent.listen());          // Set a new Response Packet
            serverAnswer = new String(message);

            if (serverAnswer.equals("100"))                     // 100 TRYING
                System.out.println(serverAnswer + " TRYING");

            if (serverAnswer.equals("180"))                     // 180 RINGING
                System.out.println(serverAnswer + " RINGING");

            if (serverAnswer.charAt(1) == '8') {                // Take Receiver (Bob) Tag
                                                                // and set it in Request Class
                String receive = new String(responsePacket.getData());
                String receiverTag = receive.substring(receive.indexOf("tag=") + 4, (receive.indexOf("tag=") + 20));
                Request.setReceiverTag(receiverTag);
            }

            if(serverAnswer.charAt(0) != '2')                   // Print the Information Messages
                System.out.println(new String(responsePacket.getData()));


        }while (serverAnswer.charAt(0) == '1') ;                // Informational Responses

        switch (serverAnswer.charAt(0)) {                       // Other Responses


            // Success Responses
            case '2': //Receiving 200 OK
                System.out.println(serverAnswer + " OK");
                System.out.println(new String(responsePacket.getData()));
                String sequence = new String(responsePacket.getData());
                String cSequence = sequence.substring(sequence.indexOf("CSeq:") + 6,    // Takes the CSequence
                        (sequence.indexOf("CSeq:") + 11));

                if (!cSequence.equals("2 BYE")) {               // Check the Sequence of the Response, if is not a
                    UserAgent.send(Request.getAck());           // response of a BYE Request Send ACK
                    System.out.println(" ACK MESSAGE ");
                    System.out.println(new String(Request.getAck()));
                    System.out.println(" ACK SENT \n");

                    /**
                     *  Start thread
                    */
                    AudioThread thread = new AudioThread();
                    AudioThread.setActiveCall(true);            // set the active call true for start the RTP flush
                    new Thread(thread).start();
                }
                break;


            // Redirection Responses
            case '3':
                break;

            // Request Failures
            case '4': //Response 4XX

                switch (serverAnswer.charAt(1)) {
                    case '0':
                        switch (serverAnswer.charAt(2)) {

                            case '0':   //400 The request could not be understood due to malformed syntax.
                                System.out.println(serverAnswer + " BAD REQUEST");
                                break;

                            case '1':   // 401 The request requires user authentication.
                                        // This response is issued by UASs and registrars.
                                System.out.println(serverAnswer + " UNAUTHORIZED");
                                break;

                            case '3':   // 403 The server understood the request, but is refusing to fulfill it.
                                        // Sometimes the call has been rejected by the receiver.
                                System.out.println(serverAnswer + " FORBIDDEN");
                                break;

                            case '4':   // 404 The server has definitive information that
                                        // the user does not exist at the domain specified in the Request-URI.
                                System.out.println(serverAnswer + " NOT FOUND");
                                break;

                            case '5':   // 405 The method specified in the Request-Line is understood,
                                        // but not allowed for the address identified by the Request-URI.
                                System.out.println(serverAnswer + " METHOD NON ALLOWED");
                                break;

                            case '6':   // 406 The server doesn't accept the header field.
                                System.out.println(serverAnswer + " NOT ACCEPTABLE");
                                break;

                            case '8':   // 408 Couldn't find the user in time.
                                System.out.println(serverAnswer + " REQUEST TIMEOUT");
                                break;

                            case '9': //409 User already registered.
                                System.out.println(serverAnswer + " CONFLICT");
                                break;
                        }
                        break;

                    case '1':
                        switch (serverAnswer.charAt(2)) {
                            case '0':   // 410 The user existed once,
                                        // but is not available here any more.
                                System.out.println(serverAnswer + " GONE");
                                break;

                            case '5':   // 415 Request body in a format not supported.
                                System.out.println(serverAnswer + " UNSUPPORTED MEDIA TYPE");
                                break;

                            case '8':   // 418 Any attempt to brew coffee with a teapot
                                        // should result in the error code "418 I'm a teapot".
                                        // The resulting entity body MAY be short and stout.
                                System.out.println(serverAnswer + " I'M A TEAPOT");
                        }
                        break;

                    case '2':
                        switch (serverAnswer.charAt(2)) {
                            case '0':   // 420 Bad SIP Protocol Extension used,
                                        // not understood by the server.
                                System.out.println(serverAnswer + " GONE");
                                break;
                        }
                        break;

                    case '8':
                        switch (serverAnswer.charAt(2)) {
                            case '0':   // 480 Callee currently unavailable.
                                System.out.println(serverAnswer + " TEMPORARILY UNAVAILABLE");
                                break;

                            case '3':   // 483 Max-Forwards header has reached the value '0'.
                                System.out.println(serverAnswer + " TOO MANY HOPS");
                                break;

                            case '6':   // 486 Callee is busy.
                                System.out.println(serverAnswer + " BUSY HERE");
                                break;

                            case '7':   // 487 Request has terminated by bye or cancel.
                                System.out.println(serverAnswer + " REQUEST TERMINATED");
                                break;

                            case '8':   // 488 Some aspect of the session description
                                        // or the Request-URI is not acceptable
                                System.out.println(serverAnswer + " NOT ACCEPTABLE HERE");
                                break;

                            case '9':   // 489 The server did not understand an event package
                                        // specified in an Event header field.
                                System.out.println(serverAnswer + " BAD EVENT");
                                break;
                        }
                        break;

                    case '9':
                        switch (serverAnswer.charAt(2)) {
                            case '1':   // 491 Server has some pending request from the same dialog.
                                System.out.println(serverAnswer + " REQUEST PENDING");
                                break;
                        }
                        break;
                }
                break;

            // Server Errors
            case '5':
                switch (serverAnswer.charAt(1)) {

                    case '0':
                        switch (serverAnswer.charAt(2)) {
                            case '0':   // 500 The server could not fulfill the request
                                        // due to some unexpected condition.
                                System.out.println(serverAnswer + " INTERNAL SERVER ERROR");
                                break;

                            case '1':   // 501 The server does not support the functionality
                                        // required to fulfill the request.
                                System.out.println(serverAnswer + " NOT IMPLEMENTED");
                                break;

                            case '2':   // 502 The server, while acting as a gateway or proxy,
                                        // received an invalid response from an inbound server it accessed
                                        // while attempting to fulfill the request.
                                System.out.println(serverAnswer + " BAD GATEWAY");
                                break;

                            case '3':   // 503 The server is currently unable to handle the request
                                        // due to a temporary overload or scheduled maintenance,
                                        // which will likely be alleviated after some delay.
                                System.out.println(serverAnswer + " SERVICE UNAVAILABLE");
                                break;

                            case '4':   // 504 The server, while acting as a gateway or proxy,
                                        // did not receive a timely response from an upstream server
                                        // it needed to access in order to complete the request.
                                System.out.println(serverAnswer + " GATEWAY TIMEOUT");
                                break;

                            case '5':   // 505 The server does not support, or refuses to support,
                                        // the major version of HTTP that was used in the request message.
                                System.out.println(serverAnswer + " VERSION NOT SUPPORTED");
                                break;
                        }
                        break;

                    case '1':
                        switch (serverAnswer.charAt(2))  {
                            case '3':   // 513 The request message length is longer than the server can process.
                                System.out.println(serverAnswer + " MESSAGE TOO LARGE");
                                break;
                        }

                    case '8':
                        switch (serverAnswer.charAt(8)) {
                            case '0':   // 580 The server is unable or unwilling to meet
                                        // some constraints specified in the offer.
                                System.out.println(serverAnswer + " PRECONDITION FAILURE");
                                break;
                        }
                        break;
                }
                break;
        }
    }
}

