package Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

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
            OutputAudio.setRunning(true);
            File audioFile = new File("src/main/resources/audio/imperial_march.wav");

            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);      // Insert the audio in a InputStream
            ais = AudioSystem.getAudioInputStream(AudioFormat.Encoding.ULAW, ais);  // Remove the WAV Header

            while (OutputAudio.isRunning()) {                                          // Fill the RTPBody
                bytesRead = ais.read(rtpBody, 0, rtpBody.length);
                System.arraycopy(rtpHeader.getHeader(), 0, rtpMessage, 0, 12);
                System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);        // Create the RTP Message
                rtpHeader.incrementSequence();
                rtpHeader.incrementTimeStamp();
                Thread.sleep(20);                                                       // Sleep 20ms
                OutputAudio.sendAudio(rtpMessage);
                if(bytesRead == -1){
                    break;
                }
            }
            OutputAudio.setRunning(false);
        } catch (IOException | UnsupportedAudioFileException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
