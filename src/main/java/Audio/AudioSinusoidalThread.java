package Audio;

import VoIP.RTPPacket;
import org.zoolu.sound.codec.G711;

/**
 * Audio.AudioSinusoidalThread Class
 *
 * This class implements runnable because it must be instantiated inside a thread.
 *  Once the connection is set, and the sendSinusoidal method is called, the class
 *  create a sinusoidal wave to send to the mjUA.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class AudioSinusoidalThread implements Runnable {

    private static double FREQUENCY = 200;
    private static double TIME_INCREMENTATION = 5;
    private static double AMPLITUDE = 4000;

    public static double getFREQUENCY() {
        return FREQUENCY;
    }
    public static void setFREQUENCY(double FREQUENCY) {
        AudioSinusoidalThread.FREQUENCY = FREQUENCY;
    }

    public static double getTimeIncrementation() {
        return TIME_INCREMENTATION;
    }

    public static void setTimeIncrementation(double timeIncrementation) {
        TIME_INCREMENTATION = timeIncrementation;
    }

    public static double getAMPLITUDE() {
        return AMPLITUDE;
    }

    public static void setAMPLITUDE(double AMPLITUDE) {
        AudioSinusoidalThread.AMPLITUDE = AMPLITUDE;
    }

    /**
     *  Create a sinusoidal wave given the Width of the wave, it's frequency and
     *    the time incrementation when calculating it. The method creates a new RTP Packet,
     *    then start calculating the values of the wave, and for each value obtained,
     *    compress it using the G711 method, for compressing byte with the PCM algorithm
     *    in the sip.jar library. Keep sending the wave until the sendingAudio value is set to false.
     */
    @Override
    public void run() {
        RTPPacket rtpPacket = new RTPPacket();
        byte[] rtpBody = new byte[160];
        byte[] rtpMessage = new byte[172];
        OutputAudio.setSendingAudio(true);
        int time = 0;
        int counter = 1;

        long start = System.currentTimeMillis();

        while (OutputAudio.isSendingAudio()) {                                     // while true the program sends audio

            //long duration = System.currentTimeMillis() - start;
            //if(System.currentTimeMillis() > 20*counter - start) {
            if (time > 8000)
                time = 0;
            else
                time += TIME_INCREMENTATION;                                             // time incrementation

            for (int index = 0; index < 160; index++) {                            // for every byte in the RTP body
                double x = (2 * Math.PI * time * FREQUENCY / 8000.0);
                double sinValue = Math.sin(x);                                     // create the sinusoidal wave
                int value = (int) (AMPLITUDE * sinValue);
                int sinusoid = G711.linear2ulaw(value);                              // compress the byte with PCM algorithm

                rtpBody[index] = (byte) sinusoid;                                    // replace it in every index of RTP body
            }

            System.arraycopy(rtpPacket.getHeader(), 0, rtpMessage, 0, 12);  // Array Copy set the RTP Header
            System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);      // and the RTP Body in RTP Message.
            rtpPacket.incrementSequence();
            rtpPacket.incrementTimeStamp();

            try {
                Thread.sleep(20);                           // Add a 20ms delay through one packet and another
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            OutputAudio.sendAudio(rtpMessage);
            counter++;
            //}
        }
    }
}
