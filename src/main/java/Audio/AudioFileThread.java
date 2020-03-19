package Audio;

import VoIP.RTPPacket;
import VoIP.UserAgent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AudioFileThread implements Runnable{

    /**
     * Send an Audio file in byte to the Server mjUA_1.8
     */
    @Override
    public void run(){
        try {
            RTPPacket rtpHeader = new RTPPacket();
            byte [] rtpMessage = new byte[172];
            byte[] rtpBody = new byte[160];
            int bytesRead = 0;
            OutputAudio.setSendingAudio(true);
            File audioFile = new File("src/main/resources/audio/imperial_march.wav");

            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);      // Insert the audio in a InputStream
            ais = AudioSystem.getAudioInputStream(AudioFormat.Encoding.ULAW, ais);  // Remove the WAV Header

            double nosofpackets = Math.ceil(((int) audioFile.length()) / 160);      // Number of packets to be created

            AudioFormat audioFormat =
                    new AudioFormat(8000, 8, 1, true, false);
            double sleepTime = (160/audioFormat.getSampleRate());
            long sleepTimeMillis= (long)(sleepTime*1000);           // Initialize the Audio Format to get the sleep time


            while (OutputAudio.isSendingAudio()) {           // Fill the RTPBody
                ais.read(rtpBody, 0, rtpBody.length);
                System.arraycopy(rtpHeader.getHeader(), 0, rtpMessage, 0, 12);
                System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);       // Create the RTP Message
                rtpHeader.incrementSequence();
                rtpHeader.incrementTimeStamp();
                long startTime = System.currentTimeMillis();
                Thread.sleep(20);                      // Sleep 20ms
                OutputAudio.sendAudio(rtpMessage);
            }
            Objects.requireNonNull(UserAgent.getSocketOutgoing()).disconnect();
        } catch (IOException | UnsupportedAudioFileException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
