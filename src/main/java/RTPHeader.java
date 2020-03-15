import java.util.Random;

public class RTPHeader {
    //size of the RTP header:
    static int HEADER_SIZE = 12;

    //Fields that compose the RTP header
    public int Version;
    public int Padding;
    public int Extension;
    public int CC;
    public int Marker;
    public int PayloadType;
    public int SequenceNumber;
    public int TimeStamp;
    public int Ssrc;

    //Bitstream of the RTP header
    public byte[] header;

    //size of the RTP payload
    public int payload_size;
    //Bitstream of the RTP payload
    public byte[] payload;

    /**
     * Class Constructor
     *
     */
    public RTPHeader(){
        //fill by default header fields:
        Version = 2;
        Padding = 0;
        Extension = 0;
        CC = 0;
        Marker = 0;
        Ssrc = 0;    // Identifies the server
        PayloadType = 0;

        //fill changing header fields:
        Random random = new Random();
        String sequence = "";
        String time = "";
        for(int i = 0; i < 5; i++ ){
            sequence += random.nextInt(10);
        }
        for(int i = 0; i < 9; i++ ){
            time += random.nextInt(10);
        }
        SequenceNumber = Integer.parseInt(sequence);
        TimeStamp = Integer.parseInt(time);

        //build the header bistream:
        header = new byte[HEADER_SIZE];

        //fill the header array of byte with RTP header fields
        header[0] = (byte)(Version << 6 | Padding << 5 | Extension << 4 | CC);
        header[1] = (byte)(Marker << 7 | PayloadType & 0x000000FF);
        header[2] = (byte)(SequenceNumber >> 8);
        header[3] = (byte)(SequenceNumber & 0xFF);
        header[4] = (byte)(TimeStamp >> 24);
        header[5] = (byte)(TimeStamp >> 16);
        header[6] = (byte)(TimeStamp >> 8);
        header[7] = (byte)(TimeStamp & 0xFF);
        header[8] = (byte)(Ssrc >> 24);
        header[9] = (byte)(Ssrc >> 16);
        header[10] = (byte)(Ssrc >> 8);
        header[11] = (byte)(Ssrc & 0xFF);

        //fill the payload bitstream:
        payload_size = 160;
        //payload = new byte[data_length];

        //fill payload array of byte from data (given in parameter of the constructor)
        //payload = Arrays.copyOf(data, payload_size);
    }

    /**
     * Class Constructor
     *
     * @param packet packet bit-stream
     * @param packet_size packet size
     */
    public RTPHeader(byte[] packet, int packet_size)
    {
        //fill default fields:
        Version = 2;
        Padding = 0;
        Extension = 0;
        CC = 0;
        Marker = 0;
        Ssrc = 0;

        //check if total packet size is lower than the header size
        if (packet_size >= HEADER_SIZE)
        {
            //get the header bitsream:
            header = new byte[HEADER_SIZE];
            for (int i=0; i < HEADER_SIZE; i++)
                header[i] = packet[i];

            //get the payload bitstream:
            payload_size = packet_size - HEADER_SIZE;
            payload = new byte[payload_size];
            for (int i=HEADER_SIZE; i < packet_size; i++)
                payload[i-HEADER_SIZE] = packet[i];

            //interpret the changing fields of the header:
            Version = (header[0] & 0xFF) >>> 6;
            PayloadType = header[1] & 0x7F;
            SequenceNumber = (header[3] & 0xFF) + ((header[2] & 0xFF) << 8);
            TimeStamp = (header[7] & 0xFF) + ((header[6] & 0xFF) << 8) + ((header[5] & 0xFF) << 16) + ((header[4] & 0xFF) << 24);
        }
    }

    public byte[] getHeader() {
        return header;
    }

    /**
     * Increment the Sequence value by one unit.
     */
    public void incrementSequence(){
        this.SequenceNumber++;
    }

    /**
     * Increment the Timestamp value by 160 units.
     */
    public void incrementTimeStamp(){
        this.TimeStamp += 160;
    }

    /**
     * Print RTPPacket's information
     */
    public void printHeader() {
        System.out.print("[RTP-Header]\n");
        System.out.println("Version: " + Version
                + ",\nPadding: " + Padding
                + ",\nExtension: " + Extension
                + ",\nCC: " + CC
                + ",\nMarker: " + Marker
                + ",\nPayloadType: " + PayloadType
                + ",\nSequenceNumber: " + SequenceNumber
                + ",\nTimeStamp: " + TimeStamp);
        System.out.println("\nPayload Size: " + payload_size);
        System.out.println("Payload: " + payload);

    }
}
