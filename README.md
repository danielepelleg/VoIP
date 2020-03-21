<!-- PROJECT LOGO -->
  <br />
    <p align="center">
  <a href="https://github.com/danielepelleg/VoIP">
    <img src="src/main/resources/images/voip.png" alt="Logo" width="130" height="130">
  </a>
  <h1 align="center">VoIP UserAgent</h1>
  <p align="center">
    Java implementation of SIP UserAgent for a VoIP Communication.
  </p>
  <p align="center">
    Maven Project, mjUA v1.8
  </p>
  
  <!-- TABLE OF CONTENTS -->
  ## Table of Contents
  
  - [Table of Contents](#table-of-contents)
  - [About The Project](#about-the-project)
    - [Application GUI](#application-gui) 
  - [SIP Requests](#sip-requests)
    - [INVITE](#INVITE)
    - [ACK](#ACK)
    - [BYE](#BYE)
  - [UML Diagrams](#uml-diagrams)
    - [Class Diagram](#class-diagram)
    - [Use Case Diagram](#use-case-diagram)
  - [Screenshots](#screenshots)
  - [Getting Started](#getting-started)
    - [Libraries](#Libraries)
    - [Updates](#updates)
  - [License](#license)
  - [Contributors](#contributors)
   
   <!-- ABOUT THE PROJECT -->
   ## About The Project
   **VoIP UserAgent** is a SIP Client UserAgent to use for a VoIP communication. The application can be used to call mjSIP User Agent and can 
   send the client some audio in RTP packets through the conversation. The Application first use *Session Initiation Protocol* (SIP) to set
   the conversation up, then the *Real-Time Transmission Protocol* (RTP) to send to the mjUA the RTP Packets that contain the audio. 
   
   The SIP communication is made up of a two DatagramSocket: one for the incoming responses, the other for the outcoming requests, 
   which are the core of the Client UserAgent. The application has a Session Recorder which is a sort of Logs Register: it records
   all the data (in byte) that flows through the conversation and save them as a string in an external file. In every moment during the communication,
   the user can see the current logs message and choose to save them on an external file in *resources/requests* folder.
   
   Every request sent and response received is recorded in the Session Recorder.

   The RTP communication is also made up of a two DatagramSocket: one for the incoming RTP Packets, the other for the outcoming ones. The Socket
   for the incoming RTP Packets is used when the UserAgent choose to send the mjUA the audio it receives. The bytes audio in the payload of this packet are edited
   randomly, and the headers remain the same. Once the packets are sent back to the mjUA it plays them and the result is a distorted noise.

   There are two other ways through which the UserAgent can send the mjUA the audio. The first is generating a mathematical sine wave of given frequency and amplitude 
   as a function of x that increments by a given value. The second is taking an external audio file in *resources/audio* folder and insert it in *n* packets of 160 bytes. When the user choose one of this two methods a new RTP header is instantiated and its sequence and the timestamp are incremented when sending every packet.

   ### Application GUI

   The system has a GUI that starts with the typical telephone interface with 2 buttons to take or hang up the call.
    
    - Press the green button to call the other mjUA UserAgent.
    
    - Once the conversation has start, you can navigate to the other tabs of the window to see the logs of the call, start and stop the sending of audio packets, see the requests the application has sent and the responses it received, and change some of the application settings.
    
    - You can hang up the call at any moment, by pressing the red button. This will send a BYE Request to the mjua, which will send a 200 OK response.
   
   <!-- SIP REQUESTS  -->
   ## SIP Requests

   ### INVITE
        INVITE sip:bob@127.0.0.1:5080 SIP/2.0
        Via: SIP/2.0/UDP 127.0.0.1:5070;branch=z9hG4bK5c3663b7
        Max-Forwards: 70
        To: "Bob" <sip:bob@127.0.0.1:5080>
        From: "Alice" <sip:alice@127.0.0.1:5070>;tag=691822153216
        Call-ID: 958219347383@127.0.0.1
        CSeq: 1 INVITE
        Contact: <sip:alice@127.0.0.1:5070>
        Expires: 3600
        User-Agent: mjsip 1.8
        Supported: 100rel,timer
        Allow: INVITE,ACK,OPTIONS,BYE,CANCEL,INFO,PRACK,NOTIFY,MESSAGE,UPDATE
        Content-Length: 129
        Content-Type: application/sdp

        v=0
        o=alice 0 0 IN IP4 127.0.0.1
        s=-
        c=IN IP4 127.0.0.1
        t=0 0
        m=audio 4070 RTP/AVP 0 8
        a=rtpmap:0 PCMU/8000
        a=rtpmap:8 PCMA/8000

   ### ACK
   Once the conversation is set and the system receives a 180 RINGING message, it sets the tag of Bob for the ACK and the BYE message.
   This tag is unique for Bob's identification and changes at every call.

        ACK sip:bob@127.0.0.1:5080 SIP/2.0
        Via: SIP/2.0/UDP 127.0.0.1:5070;branch=z9hG4bKdijq48al
        Max-Forwards: 70
        To: "Bob" <sip:bob@127.0.0.1:5080>;tag=c7483107c0dc85d2
        From: "Alice" <sip:alice@127.0.0.1:5070>;tag=965285618558
        Call-ID: 958219347383@127.0.0.1
        CSeq: 1 ACK
        Contact: <sip:alice@127.0.0.1:5070>
        Expires: 3600
        User-Agent: mjsip 1.8
        Content-Length: 0

   ### BYE 
        BYE sip:bob@127.0.0.1:5080 SIP/2.0
        Via: SIP/2.0/UDP 127.0.0.1:5070;branch=z9hG4bK2dmdtwnf
        Max-Forwards: 70
        To: "Bob" <sip:bob@127.0.0.1:5080>;tag=c7483107c0dc85d2
        From: "Alice" <sip:alice@127.0.0.1:5070>;tag=965285618558
        Call-ID: 813329132967@127.0.0.1
        CSeq: 2 BYE
        User-Agent: mjsip 1.8
        Content-Length: 0

   <!-- UML DIAGRAMS -->
   ## UML Diagrams

   ### Class Diagram
   <img src="Documentation/UML Diagrams/Class Diagram/Class_Diagram.png" alt="classDiagram">

   ### Use Case Diagram
   <p align="center"><img src="Documentation/UML Diagrams/Use Case Diagram/Use_Case_Diagram.png" alt="usecaseDiagram"></p>

   <!-- SCREENSHOTS -->
   ## Screenshots
   <p align="center">
   <img src="Documentation/Screenshots/Main Tab.png" alt="mainTab">
   <img src="Documentation/Screenshots/Audio Tab.png" alt="audioTab">
   <img src="Documentation/Screenshots/Logs Tab.png" alt="logsTab">
   </p>

   <!-- GETTING STARTED -->
   ## Getting Started
   Download the *mjSIP UA v1.8* at the following <a href="http://www.mjsip.org/download.html">download link</a> and follow the
   guide in the *"simple-call-HOW-TO.txt"* file and choose the basic ua-to-ua configuration. Copy the line and open the command prompt 
   in the root directory of mjua v1.8 and run the mjUA b, which stands for "Bob".

   You can now just clone this repository, import the libraries and run the *Program.java* file of the Application.
   
   ### Libraries
   The program requires a two libraries. Once downloaded the *mjUA v1.8.zip* open the *lib* folder and import the mjsip library. This library is used
   to convert the audio bytes to send with the PCMU Encoding, through the method of the class *G711.linear2ulaw()*. The other library to import is the
   *Java FX Windows SDK 11.0.2*, needed to run the GUI.

   
   - <a href="http://www.mjsip.org/download.html" title="mjsip">mjsip</a>
   - <a href="https://gluonhq.com/products/javafx/" title="JavaFX">Java FX</a>

   ### Updates
   Pull this repository for updates.
   
   <!-- LICENSE -->
   ## LICENSE
   Distributed under the GPL License. See `LICENSE` for more information.
   <!-- VOIP LOGO IMAGE / SETTINGS IMAGE -->
   <div>Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 
   title="Flaticon"> www.flaticon.com</a></div>
   <!-- AUDIO CONTROL BUTTON -->
   Icons made by <a href="https://www.flaticon.com/authors/eucalyp" title="Eucalyp">Eucalyp</a> from <a href="https://www.flaticon.com/" 
   title="Flaticon"> www.flaticon.com</a>
   <!-- FILE AUDIO BUTTON-->
   <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
   <!-- SINUSOIDAL AUDIO BUTTON-->
   <div>Icons made by <a href="https://www.flaticon.com/authors/phatplus" title="phatplus">phatplus</a> from <a href="https://www.flaticon.com/" 
   title="Flaticon">www.flaticon.com</a></div>
   <!-- SPOILED AUDIO BUTTON -->
   <div>Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 
   title="Flaticon">www.flaticon.com</a></div>
   <!-- STOP AUDIO BUTTON -->
   <div>Icons made by <a href="https://www.flaticon.com/authors/dinosoftlabs" title="DinosoftLabs">DinosoftLabs</a> from <a href="https://www.flaticon.com/" 
   title="Flaticon">www.flaticon.com</a></div>
   <!-- LOGS IMAGE -->
   <div>Icons made by <a href="https://www.flaticon.com/authors/itim2101" title="itim2101">itim2101</a> from <a href="https://www.flaticon.com/" 
   title="Flaticon">www.flaticon.com</a></div>

   
   <!-- CONTRIBUTORS -->
   ## CONTRIBUTORS
   [Daniele Pellegrini](https://github.com/danielepelleg) - 285240

   [Guido Soncini](https://github.com/gweedo) - 285140

   [Mattia Ricci](https://github.com/tiaringhio) - 285237
   
   
