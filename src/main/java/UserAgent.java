import it.unipr.netsec.nemo.link.DataLink;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class UserAgent {
    public static void main(String[] args) throws IOException {
        // I read the content of invite.txt to get the INVITE message
        String invite = Files.readString(Paths.get("invite.txt"), StandardCharsets.UTF_8);

        String ack = Files.readString(Paths.get("ack.txt"), StandardCharsets.UTF_8);

        String bye = Files.readString(Paths.get("bye.txt"), StandardCharsets.UTF_8);

        // TODO Error management on received packages

        //take the invite message and convert it in bytes
        byte[] send = invite.getBytes();
        byte[] receive = new byte[1024];
        byte[] response = new byte[1024];

        InetAddress address = InetAddress.getByName("127.0.0.1");

        int length = send.length;
        int port1 = 5080;
        int port2 = 5070;

        // TODO libcap not working
        DataLink link = new DataLink();

        //creating the format message to send the SIP invite
        DatagramPacket alice = new DatagramPacket(send, length, address, port1);

        //creating the format message to for the answer
        DatagramPacket bob = new DatagramPacket(receive, receive.length, address, port2);

        //creating the ports for send and recive messages
        DatagramSocket socket_port1 = new DatagramSocket();
        DatagramSocket socket_port2 = new DatagramSocket(port2, address);

        //LoopbackInterface loopback = new LoopbackInterface(new SocketAddress(new Ip4Address(address), port1));
        //new LibpcapSniffer(loopback, LibpcapHeader.LINKTYPE_IPV4,"Johhny.pcap");

        //sending the message
        socket_port1.send(alice);

        String serveAnswer;
        //cicle created for the possible answers, if the SIP code start with 1 so we restard the cicle
        do {

            socket_port2.receive(bob);
            //taking SIP code form the answers(Byte format)
            byte[] serveAnswerB = Arrays.copyOfRange(bob.getData(), 8 ,11);
            serveAnswer = new String(serveAnswerB);

        }while(serveAnswer.charAt(0) == '1');

        // Switch that gives the type of response (whether it is an error) and its description.
        switch (serveAnswer.charAt(0)){
            case '2':
                byte [] send1 = ack.getBytes();

                DatagramPacket alice1 = new DatagramPacket(send1, send1.length, address, port1);
                socket_port1.send(alice1);
                break;

            case '3':

                break;

            case '4': //Response 4XX
                switch (serveAnswer.charAt(1)) {
                    case '0':
                        switch (serveAnswer.charAt(2)) {
                            case '0': //400 The request could not be understood due to malformed syntax.
                                System.out.println(serveAnswer + " BAD REQUEST");
                                break;
                            case '1': //401 The request requires user authentication. This response is issued by UASs and registrars.
                                System.out.println(serveAnswer + " UNAUTHORIZED");
                                break;
                            case '3': //403 The server understood the request, but is refusing to fulfill it. Sometimes the call has been rejected by the receiver.
                                System.out.println(serveAnswer + " FORBIDDEN");
                                break;
                            case '4': //404 The server has definitive information that the user does not exist at the domain specified in the Request-URI.
                                System.out.println(serveAnswer + " NOT FOUND");
                                break;
                            case '5': //405 The method specified in the Request-Line is understood, but not allowed for the address identified by the Request-URI.
                                System.out.println(serveAnswer + " METHOD NON ALLOWED");
                                break;
                            case '6': //406 The server doesn't accept the header field.
                                System.out.println(serveAnswer + " NOT ACCEPTABLE");
                                break;
                            case '8': //408 Couldn't find the user in time.
                                System.out.println(serveAnswer + " REQUEST TIMEOUT");
                                break;
                            case '9': //409 User already registered.
                                System.out.println(serveAnswer + " CONFLICT");
                                break;
                        }
                        break;
                    case '1':
                        switch (serveAnswer.charAt(2)) {
                            case '0': //410 The user existed once, but is not available here any more.
                                System.out.println(serveAnswer + " GONE");
                                break;
                            case '5': //415 Request body in a format not supported.
                                System.out.println(serveAnswer + " UNSUPPORTED MEDIA TYPE");
                                break;
                            case '8': //418 Any attempt to brew coffee with a teapot should result in the error code "418 I'm a teapot". The resulting entity body MAY be short and stout.
                                System.out.println(serveAnswer + " I'M A TEAPOT");
                        }
                        break;
                    case '2':
                        switch (serveAnswer.charAt(2)) {
                            case '0': //420 Bad SIP Protocol Extension used, not understood by the server.
                                System.out.println(serveAnswer + " GONE");
                                break;
                        }
                        break;
                    case '8':
                        switch (serveAnswer.charAt(2)) {
                            case '0': //480 Callee currently unavailable.
                                System.out.println(serveAnswer + " TEMPORARILY UNAVAILABLE");
                                break;
                            case '3': //483 Max-Forwards header has reached the value '0'.
                                System.out.println(serveAnswer + " TOO MANY HOPS");
                                break;
                            case '6': //486 Callee is busy.
                                System.out.println(serveAnswer + " BUSY HERE");
                                break;
                            case '7': //487 Request has terminated by bye or cancel.
                                System.out.println(serveAnswer + " REQUEST TERMINATED");
                                break;
                            case '8': //488 Some aspect of the session description or the Request-URI is not acceptable
                                System.out.println(serveAnswer + " NOT ACCEPTABLE HERE");
                                break;
                            case '9': //489 The server did not understand an event package specified in an Event header field.
                                System.out.println(serveAnswer + " BAD EVENT");
                                break;
                        }
                        break;
                    case '9':
                        switch (serveAnswer.charAt(2)) {
                            case '1': //491 Server has some pending request from the same dialog.
                                System.out.println(serveAnswer + " REQUEST PENDING");
                                break;
                        }
                        break;
                }
                break;

            case '5':
                switch (serveAnswer.charAt(1)) {
                    case '0':
                        switch (serveAnswer.charAt(2)) {
                            case '0': //500 The server could not fulfill the request due to some unexpected condition.
                                System.out.println(serveAnswer + " INTERNAL SERVER ERROR");
                                break;
                            case '1': //501 The server does not support the functionality required to fulfill the request.
                                System.out.println(serveAnswer + " NOT IMPLEMENTED");
                                break;
                            case '2': //502 The server, while acting as a gateway or proxy, received an invalid response from an inbound server it accessed while attempting to fulfill the request.
                                System.out.println(serveAnswer + " BAD GATEWAY");
                                break;
                            case '3': //503 The server is currently unable to handle the request due to a temporary overload or scheduled maintenance, which will likely be alleviated after some delay.
                                System.out.println(serveAnswer + " SERVICE UNAVAILABLE");
                                break;
                            case '4': //504 The server, while acting as a gateway or proxy, did not receive a timely response from an upstream server it needed to access in order to complete the request.
                                System.out.println(serveAnswer + " GATEWAY TIMEOUT");
                                break;
                            case '5': //505 The server does not support, or refuses to support, the major version of HTTP that was used in the request message.
                                System.out.println(serveAnswer + " VERSION NOT SUPPORTED");
                                break;
                        }
                        break;
                    case '1':
                        switch (serveAnswer.charAt(2))  {
                            case '3': //513 The request message length is longer than the server can process.
                                System.out.println(serveAnswer + " MESSAGE TOO LARGE");
                                break;
                        }
                    case '8':
                        switch (serveAnswer.charAt(8)) {
                            case '0': //580 The server is unable or unwilling to meet some constraints specified in the offer.
                                System.out.println(serveAnswer + " PRECONDITION FAILURE");
                                break;
                        }
                        break;
                }
                break;
        }
        DatagramPacket bob2 = new DatagramPacket(response, response.length, address, port2);
        byte[] close_call =  bye.getBytes();
        alice = new DatagramPacket(close_call, close_call.length, address, port1);
        try
        {
            Thread.sleep(5000);
            socket_port1.send(alice);
            socket_port2.receive(bob2);
            serveAnswer = new String(bob2.getData());
            System.out.println(serveAnswer);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}