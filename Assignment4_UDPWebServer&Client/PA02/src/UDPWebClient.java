import java.io.*; 
import java.net.*; 
 
/** WebClient class implements a simple web client. 
* Its primary responsibilities include: 
* 1. Initializing the udp connection to web server 
* 2. send HTTP request and receive HTTP response 
*/ 
public class UDPWebClient { 
    public static void main(String[] args) { 
        String host = "localhost"; 
        int port = 8888; 
        String resource = "/index.html"; 
 
        try { 
            // Mission 1. Fill in #11 define InetAddress with host ip and initial DatagramSocket 
            InetAddress address = InetAddress.getByName(host); 
            DatagramSocket socket = new DatagramSocket(); 
 
/** 
* Improve your HTTP Client to provide other request Methods(POST, DELETE, ...) 
* and also improve to handle headers(Content-Type, User-Agent, ...) 
*/ 
            // Mission 2. Fill in #10 Create Request MSG 
            //Fill in #10 Transfter to Byte and put in datagramPacket, and set it. 
            String request = "GET " + resource + " HTTP/1.1\r\n" +
                             "Host: " + host + "\r\n" +
                             "\r\n";
            byte[] requestBytes = request.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(requestBytes, requestBytes.length, address, port);
            socket.send(datagramPacket);
 
            // Mission 3. Fill in #11 Initialize byte and datagrampacket to receive data 
            byte[] receiveData = new byte[8192];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
 
            // Fill in #12 Get string from datagrampacket to display 
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(response);

            socket.close(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
}