import java.io.*;
import java.net.*;

/**
 * WebClient class implements a simple web client.
 * Its primary responsibilities include:
 * 1. Initializing the TCP connection to web server
 * 2. Sending HTTP request and receiving HTTP response
 */
public class WebClient {
    public static void main(String[] args) {
        // Set the host, port and resource to send HTTP Request
    	String host = "example.com"; 
    	int port = 80;
    	String resource = "/index.html"; // 존재하지 않는 페이지

        // Mission 1: Establish a socket connection to Server
        try (
            // Fill #1, Set TCP socket to HTTP Web Server
            Socket socket = new Socket(host, port);
            // Fill #2, create PrintWriter instance with socket’s OutputStream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Fill #3, Get input stream from server, and insert it to BufferedReader instance
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            /**
             * Improve your HTTP Client to provide other request Methods (POST, DELETE, …)
             * and also improve to handle headers (Content-Type, User-Agent, …)
             */

            // Mission 2: Send HTTP GET Request and Read and display the response
            // Fill #4, Send HTTP GET request
            out.println("GET " + resource + " HTTP/1.1");
            out.println("Host: " + host);
            out.println("Connection: Close");
            out.println(); // Blank line to indicate end of request headers

            // Mission 3: Read and display the response
            // Fill #5, Read and display the response
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
