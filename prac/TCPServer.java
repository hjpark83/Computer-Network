import java.io.*;
import java.net.*;

class TCPServer {
    public static void main(String argv[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;

        // Create server socket on port 6789
        ServerSocket welcomeSocket = new ServerSocket(6789);

        System.out.println("Server is running and waiting for connections...");

        while (true) {
            // Accept connection from client
            try (Socket connectionSocket = welcomeSocket.accept();
                 // Create input stream to receive data from client
                 BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                 // Create output stream to send data to client
                 DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream())) {

                // Read a line of data from client
                clientSentence = inFromClient.readLine();

                if (clientSentence == null) {
                    continue; // handle null case when the client disconnects unexpectedly
                }

                System.out.println("Received from client: " + clientSentence);

                // Convert received data to uppercase and add newline character
                capitalizedSentence = clientSentence.toUpperCase() + '\n';

                // Send converted data back to client
                outToClient.writeBytes(capitalizedSentence);

                System.out.println("Sent to client: " + capitalizedSentence);

            } catch (IOException e) {
                System.err.println("Error handling client connection: " + e.getMessage());
            }
        }
    }
}
