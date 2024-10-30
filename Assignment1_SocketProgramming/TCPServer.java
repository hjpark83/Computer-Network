import java.io.*;
import java.net.*;

class TCPServer {
    public static void main(String argv[]) throws Exception {
        String serverName = "Server of John Q. Smith"; // Server name
        int port = 8888; // Server port number

        // Mission 2: Open TCP Socket to receive message from client
        // Fill#4, Initializing the server on a specified port 
        ServerSocket welcomeSocket = new ServerSocket(port); //Mission 2
        System.out.println("Server is running and waiting for connections...");
        
        while (true) {
            // Fill#5, Listen for a TCP connection request.
            Socket connectionSocket = welcomeSocket.accept(); //Mission 2
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(System.in)); 
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            // Fill#6, Read message from client
            String clientMessage = inFromClient.readLine(); //Mission 2
            System.out.println("Received: " + clientMessage);
            
            // Mission 3: Extract client's name and integer from message and send calculated message to client            
            // Extract client's name and integer
            String[] parts = clientMessage.split(": ");
            String clientName = parts[0];
            int clientNumber = Integer.parseInt(parts[1]);
       
            // Terminate the server if the client sends a number outside the range of 1 to 100
            if (clientNumber < 1 || clientNumber > 100) {
                connectionSocket.close();
                welcomeSocket.close();
                break;
            }
            
            System.out.print("Enter an integer between 1 and 100: ");
            int serverNumber = Integer.parseInt(inFromServer.readLine()); 

            // Fill#7, Calculate and print the sum of the client's and server's numbers
            int sum = clientNumber + serverNumber; // Mission 3
            System.out.println("Client number: " + clientNumber);  
            System.out.println("Server number: " + serverNumber);
            System.out.println("Sum: " + sum);

            // Fill#8, Send server's name and chosen number back to the client
            String serverMessage = serverName + ": " + serverNumber;  // Mission 3
            outToClient.writeBytes(serverMessage+ '\n'); // Mission 3
        }
    }
}
