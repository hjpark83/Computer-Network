import java.io.*;
import java.net.*;
import java.util.*;

/**
 * HttpRequest class handles HTTP request messages from clients.
 * It parses the request, determines the appropriate response,
 * and sends the response back to the client.
 */
final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n"; // Carriage Return Line Feed
    Socket socket;

    // Constructor
    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    // Implement the run() method of the Runnable interface.
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * ProcessRequest Method class handles HTTP Request Messages
     * 1. Receive and send HTTP Request and HTTP Response
     * 2. Parse HTTP request line and save
     * 3. Parse HTTP Header line and save
     *
     * The server runs indefinitely, continuously accepting new connections
     * and spawning threads to process HTTP requests.
     */
    private void processRequest() throws Exception {
        // Mission 2: parse the HTTP request (Fill #5 ~ #7)

        // Fill #5: Create input stream from socket to receive data from client
        InputStream is = socket.getInputStream();

        // Fill #6: Create output stream via socket to send data to client
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        // Fill #7: BufferedReader filter around the input stream to parse HTTP Request
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // Mission 2(2-A, 2-B, 2-C): parse the HTTP request (Fill #8 ~ #9)

        // Fill #8: Get the request line of the HTTP request message
        String requestLine = br.readLine();

        // Check if the requestLine is null or empty to handle 400 Bad Request
        if (requestLine == null || requestLine.isEmpty()) {
            sendBadRequest(os);
            closeConnections(os, br);
            return;
        }

        // Fill #9: Use StringTokenizer to parse the HTTP request
        StringTokenizer tokens = new StringTokenizer(requestLine);
        String method;
        String fileName;
        String version;

        try {
            method = tokens.nextToken(); // Mission 2-A: Get method information
            fileName = tokens.nextToken(); // Mission 2-B: Get URI information
            version = tokens.hasMoreTokens() ? tokens.nextToken() : "HTTP/1.0"; // Mission 2-C: Get HTTP Version information
        } catch (NoSuchElementException e) {
            // If any token is missing, send 400 Bad Request
            sendBadRequest(os);
            closeConnections(os, br);
            return;
        }

        // Only support HTTP/1.0 and HTTP/1.1
        if (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
            sendBadRequest(os);
            closeConnections(os, br);
            return;
        }

        // Prepend a "." so that file request is within the current directory.
        fileName = "." + fileName;

        // Open the requested file.
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        // Debug info for private use
        System.out.println("Incoming!!!");
        System.out.println(requestLine);
        String headerLine;
        while ((headerLine = br.readLine()) != null && headerLine.length() != 0) {
            System.out.println(headerLine);
        }

        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String contentLengthLine = "";
        String entityBody = "";

        /**
         * Mission 3. Analyze the request and send an appropriate response
         * Mission 3. If HTTP response message consisting of the requested file, make the code with 200 OK
         * If the requested file is not present in the server, the server should send an HTTP “404 Not Found” message back to the client.
         * If the request message is not proper, the server should send an HTTP “400 BAD REQUEST” message back to the client.
         * and make more response codes for your HTTP web server
         * Optional Projects. Not only for the Method “GET”, you also have to consider handling other Methods.
         */

        if (!method.equals("GET")) {
            // Handle unsupported methods
            statusLine = "HTTP/1.0 501 Not Implemented" + CRLF; // Mission 3-C: Status Code 501 Not Implemented
            contentTypeLine = "Content-Type: text/html" + CRLF;
            entityBody = "<HTML>" +
                         "<HEAD><TITLE>Not Implemented</TITLE></HEAD>" +
                         "<BODY>501 Not Implemented: " + method + " method is not supported.</BODY></HTML>";
        } else if (fileExists) {
            // Fill #10: When requested file exists, Status Code 200 OK
            statusLine = "HTTP/1.0 200 OK" + CRLF; // Corrected to 200 OK

            // Mission 3-A: Status Code 200 OK
            contentTypeLine = "Content-Type: " + contentType(fileName) + CRLF;

            contentLengthLine = "Content-Length: " + getFileSizeBytes(fileName) + CRLF;
        } else {
            // Fill #11: When requested file doesn’t exist, Status Code 404 NOT FOUND
            statusLine = "HTTP/1.0 404 Not Found" + CRLF; // Mission 3-B: Status Code 404 Not Found
            contentTypeLine = "Content-Type: text/html" + CRLF;
            entityBody = "<HTML>" +
                         "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                         "<BODY>404 Not Found</BODY></HTML>";
        }

        // Send the status line.
        os.writeBytes(statusLine);

        // Send the content type line.
        os.writeBytes(contentTypeLine);

        // Send the content length line if applicable.
        if (fileExists && method.equals("GET")) {
            os.writeBytes(contentLengthLine);
        }

        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);

        // Send the entity body.
        if (fileExists && method.equals("GET")) {
            sendBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(entityBody); // Mission 3: Send appropriate entity body
        }

        // Close streams and socket.
        closeConnections(os, br);
    }

    /**
     * Method to send a 400 Bad Request response
     *
     * @param os DataOutputStream to client
     * @throws IOException If an I/O error occurs
     */
    private void sendBadRequest(DataOutputStream os) throws IOException {
        String statusLine = "HTTP/1.0 400 Bad Request" + CRLF;
        String contentTypeLine = "Content-Type: text/html" + CRLF;
        String entityBody = "<HTML>" +
                            "<HEAD><TITLE>Bad Request</TITLE></HEAD>" +
                            "<BODY>400 Bad Request</BODY></HTML>";

        os.writeBytes(statusLine);
        os.writeBytes(contentTypeLine);
        os.writeBytes("Content-Length: " + entityBody.length() + CRLF);
        os.writeBytes(CRLF);
        os.writeBytes(entityBody);
    }

    /**
     * Method to close DataOutputStream and BufferedReader
     *
     * @param os DataOutputStream to client
     * @param br BufferedReader from client
     * @throws IOException If an I/O error occurs
     */
    private void closeConnections(DataOutputStream os, BufferedReader br) throws IOException {
        os.close();
        br.close();
        socket.close();
    }

    /**
     * Method which sends the content
     *
     * @param fis FileInputStream to transfer
     * @param os  OutputStream to client
     * @throws Exception If an error occurs during sending
     */
    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    /**
     * Method to return appropriate Content-Type based on file extension
     *
     * @param fileName The name of the file
     * @return The MIME type as a String
     */
    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".json")) {
            return "application/json";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (fileName.endsWith(".mp3")) {
            return "audio/mpeg"; // Fill #12: Audio MIME types
        } else if (fileName.endsWith(".wav")) {
            return "audio/wav"; // Fill #12: Audio MIME types
        } else if (fileName.endsWith(".aac")) {
            return "audio/aac"; // Fill #12: Audio MIME types
        } else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp"; // Fill #13: Image MIME types
        } else if (fileName.endsWith(".tiff")) {
            return "image/tiff"; // Fill #13: Image MIME types
        } else if (fileName.endsWith(".webp")) {
            return "image/webp"; // Fill #13: Image MIME types
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * Get the File size in bytes
     *
     * @param fileName The name of the file
     * @return The size of the file in bytes
     * @throws IOException If an I/O error occurs
     */
    private static long getFileSizeBytes(String fileName) throws IOException {
        File file = new File(fileName);
        return file.length();
    }
}
