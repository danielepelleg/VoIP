package Audio;

import VoIP.RTPPacket;
import org.zoolu.sound.codec.G711;

/**
 * Audio.AudioSinusoidalThread Class
 * <p>
 * This class implements runnable because it must be instantiated inside a thread.
 * Once the connection is set, and the sendSinusoidal method is called, the class
 * create a sinusoidal wave to send to the mjUA.
 *
 * @author Daniele Pellegrini <daniele.pellegrini@studenti.unipr.it> - 285240
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class AudioSinusoidalThread implements Runnable {

    private static double frequency = 200;
    private static double amplitude = 4000;

    /**
     * Get and Set methods
     */
    public static double getFrequency() {
        return frequency;
    }

    public static void setFrequency(double newValue) {
        frequency = newValue;
    }

    public static double getAmplitude() {
        return amplitude;
    }

    public static void setAmplitude(double newValue) {
        amplitude = newValue;
    }

    /**
     * Create a sinusoidal wave given the Width of the wave, it's frequency and
     * the time incrementation when calculating it. The method creates a new RTP Packet,
     * then start calculating the values of the wave, and for each value obtained,
     * compress it using the G711 method, for compressing byte with the PCM algorithm
     * in the sip.jar library. Keep sending the wave until the sendingAudio value is set to false.
     */
    @Override
    public void run() {
        RTPPacket rtpPacket = new RTPPacket();
        byte[] rtpBody = new byte[160];
        byte[] rtpMessage = new byte[172];
        OutputAudio.setRunning(true);
        int time = 1;
        // int counter = 1;

        long start = System.currentTimeMillis();

        while (OutputAudio.isRunning()) {                                     // while true the program sends audio

            if (System.currentTimeMillis() < start + (20 * time))
                continue;

            if (time > 8000) {
                time = 1;
                start = System.currentTimeMillis();
            } else
                time += 1;                                             // time incrementation


            for (int index = 0; index < 160; index++) {                            // for every byte in the RTP body
                double x = (2 * Math.PI * time * frequency / 8000.0);
                double sinValue = Math.sin(x);                                     // create the sinusoidal wave
                int value = (int) (amplitude * sinValue);
                int sinusoid = G711.linear2ulaw(value);                              // compress the byte with PCM algorithm

                rtpBody[index] = (byte) sinusoid;                                    // replace it in every index of RTP body
            }

            System.arraycopy(rtpPacket.getHeader(), 0, rtpMessage, 0, 12);  // Array Copy set the RTP Header
            System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);      // and the RTP Body in RTP Message.
            rtpPacket.incrementSequence();
            rtpPacket.incrementTimeStamp();

            /*try {
                Thread.sleep(20);                           // Add a 20ms delay through one packet and another
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }*/

            OutputAudio.sendAudio(rtpMessage);
            //counter++;
            //}
        }
    }
}
