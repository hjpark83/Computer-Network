import java.io.*;
import java.net.*;

class TCPClient {
    public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;

        // Create BufferedReader to read input from user
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // Create socket to connect to server ("hostname" and port number 6789)
        Socket clientSocket = new Socket("127.0.0.1", 6789);

        // Create output stream to send data to server
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        // Create input stream to receive data from server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Read a line of input from user
        System.out.print("Enter a sentence: ");
        sentence = inFromUser.readLine();

        // Send the sentence to the server
        outToServer.writeBytes(sentence + '\n');

        // Read reply from clientSocket
        modifiedSentence = inFromServer.readLine();

        // Print the reply from the server
        System.out.println("FROM SERVER: " + modifiedSentence);

        // Close the socket to end the connection
        clientSocket.close();
    }
}
