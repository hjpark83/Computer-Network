import java.io.*;
import java.net.*;

class UDPClient {
    public static void main(String args[]) throws Exception {

        // Create BufferedReader to read input from user
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // Create UDP socket
        DatagramSocket clientSocket = new DatagramSocket();

        // Get the server's IP address (replace "hostname" with actual host or IP)
        InetAddress IPAddress = InetAddress.getByName("hostname");

        // Create byte arrays for sending and receiving data
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        // Read a line of input from user
        System.out.print("Enter message to send: ");
        String sentence = inFromUser.readLine();

        // Convert the input string to a byte array
        sendData = sentence.getBytes();

        // Create a packet to send (data, data length, IP address, port number 9876)
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        // Send the packet to the server
        clientSocket.send(sendPacket);

        // Create a packet to receive data (byte array to receive data)
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // Receive the packet from the server
        clientSocket.receive(receivePacket);

        // Convert the received data to a string, trim to remove extra buffer space
        String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

        // Print Sentence received from the server
        System.out.println("FROM SERVER: " + modifiedSentence);

        // Close the socket to end the connection
        clientSocket.close();
    }
}
