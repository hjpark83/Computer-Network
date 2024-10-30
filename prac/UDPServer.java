import java.io.*; 
import java.net.*; 

class UDPServer {
	public static void main(String args[]) throws Exception {

		// Create server socket on port number
		DatagramSocket serverSocket = new DatagramSocket(9876);

		// Create byte arrays for receiving and sending data 
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while (true) {

			// Create packet to receive data (byte array to receive data)
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			// Receive packet from client
			serverSocket.receive(receivePacket);;
			
			
			// Convert received data to a string
			String sentence = new String(receivePacket.getData());

			// Get the client's IP address and port number
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();

			// Convert received data to uppercase
			String capitalizedSentence = sentence.toUpperCase();

			// Convert the modified string to a byte array
			sendData = capitalizedSentence.getBytes();

			// Create packet to send (data, data length, client IP address, port number)
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

			// Send packet to client
			serverSocket.send(sendPacket);
		
		}
	}
}
