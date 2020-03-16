import org.zoolu.sound.codec.G711;

public class AudiosinusoidalThread implements Runnable {


  private void sendSinuosiudal() {
    RTPHeader rtpHeader = new RTPHeader();
    byte[] rtpBody = new byte[160];
    byte[] rtpMessage = new byte[172];
    while (OutputAudio.getActiveCall()) {
      for (int time = 0; time < 8000; time += 5) {
        // rtpHeader.printHeader();
        for (int i = 0; i < 160; i++) {
          double rad = Math.toRadians(time);
          double sinValue = Math.sin(rad);
          int value = (int) (65536 * sinValue);
          int sinusoid = G711.linear2ulaw(value);
          rtpBody[i] = (byte) sinusoid;
        }

        System.arraycopy(rtpHeader.getHeader(), 0, rtpMessage, 0, 12);
        System.arraycopy(rtpBody, 0, rtpMessage, 12, rtpBody.length);
        rtpHeader.incrementSequence();
        rtpHeader.incrementTimeStamp();

        //  System.out.println("----------");
        OutputAudio.sendAudio(rtpMessage);
        //new Scanner(System.in).nextLine();
      }
    }
  }

    @Override
    public void run() {
    sendSinuosiudal();
    }
}
