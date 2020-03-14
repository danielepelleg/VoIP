public class AudiosinusoidalThread implements Runnable {


  /*  private void sendSinuosiudal(){
        RTPHeader rtpHeader = new RTPHeader();
        byte[] rtpMessage;
        byte[] toSend = new byte[RTPHeader.HEADER_SIZE + 160];
        while (activeCall) {
            rtpHeader.printHeader();

            rtp.incrementSequence();
            rtp.incrementTimeStamp();
            for (int i = 0; i < 160; i++) {
                double sinusoid = Math.sin(2 * Math.PI * 500 * i);
                toSend[i + 12] = (byte) sinusoid;
            }
            System.out.println("----------");
            sendAudio(toSend);
            //new Scanner(System.in).nextLine();
    }*/
    @Override
    public void run() {

    }
}
