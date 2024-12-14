import java.io.* ;
import java.net.* ; 
import java.util.* ; 
 
final class UDPHttpRequest implements Runnable { 
    final static String CRLF = "\r\n"; 
    // Mission 1. Fill in #4 Changed to DatagramSocket 
    //  Fill in #4 Added to store the received packet 
    private DatagramPacket packet; 
    private DatagramSocket socket;

    // Fill in #5 Constructor should be changed. Socket and packet information should be transferred 
    public UDPHttpRequest(DatagramPacket packet, DatagramSocket socket) { 
        this.socket = socket;
        this.packet = packet;     
    } 
     
    // Implement the run() method of the Runnable interface. 
    public void run() { 
        try { 
            processRequest(); 
        } catch (Exception e) { 
            System.out.println(e); 
        } 
    } 
 
    private void processRequest() throws Exception { 
        // Mission 2. #Fill in #6 Get the request data from the packet 
        // Received DatagramPacket → Byte → byteArrayInputStream → InputStream → BufferedReader 
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        InputStream inputStream = new BufferedInputStream(byteArrayInputStream);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
 
        // Get the request line of the HTTP request message. 
        String requestLine = br.readLine();
        if (requestLine == null) {
            System.out.println("Request line is null");
            return;
        }
 
        // Extract the filename from the request line. 
        StringTokenizer tokens = new StringTokenizer(requestLine); 
        String method = tokens.nextToken(); 
        String fileName = tokens.nextToken(); 
         
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
    
        // Construct the file path relative to the server's directory
        String filePath = "PA02/src/" + fileName;
    
        
        // Prepend a "." so that file request is within the current directory. 
        // fileName = "." + fileName; 
 
        // Open the requested file. 
        FileInputStream fis = null; 
        boolean fileExists = true; 
        try { 
            fis = new FileInputStream(filePath); 
        } catch (FileNotFoundException e) { 
            fileExists = false; 
        } 
 
        // Construct the response message. 
        String statusLine; 
        String contentTypeLine; 
        String contentLengthLine; 
        String entityBody = null; 
 
        if (fileExists) { 
            statusLine = "HTTP/1.0 200 OK" + CRLF; 
            contentTypeLine = "Content-Type: " + contentType(fileName) + CRLF; 
            contentLengthLine = "Content-Length: " + getFileSizeBytes(filePath) + CRLF; 
        } else { 
            statusLine = "HTTP/1.0 404 Not Found" + CRLF; 
            contentTypeLine = "Content-Type: text/html" + CRLF; 
            entityBody = "<HTML><HEAD><TITLE>Not Found</TITLE></HEAD><BODY>Not Found</BODY></HTML>"; 
            contentLengthLine = "Content-Length: " + entityBody.length() + CRLF; 
        } 
 
        // Mission 3. Fill in #7 Create the header line as bytes with get Bytes 
        // Fill in #7 create and write to ByteArrayOutputStream to send header line 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(statusLine.getBytes());
        baos.write(contentTypeLine.getBytes());
        baos.write(contentLengthLine.getBytes());
        baos.write(CRLF.getBytes());
 
 
        // transfer entity to ByteArrayOutputStream 
        if (fileExists) { 
            sendBytes(fis, baos); 
            fis.close(); 
        } else { 
            baos.write(entityBody.getBytes()); 
        }  
 
       // Mission 3. Fill in #8 Create the byte to send stream(header and entity body) 
       // Fill in #8 send Datagram with UDP Socket. 
       // (ByteArrayOutputStream → ByteArray →  DatagramPacket) 
        byte[] byteArray = baos.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(byteArray, byteArray.length, packet.getAddress(), packet.getPort());
        socket.send(datagramPacket);
 
        // Close streams and socket. 
        br.close(); 
    } 
 
    /** 
     * Method which sends the context 
     * @param fis FileInputStream to transfer 
     * @param os outputstream to client 
     */ 
    private static void sendBytes(FileInputStream fis,  
      OutputStream os) throws Exception { 
        // Construct a 1K buffer to hold bytes on their way to the socket. 
        byte[] buffer = new byte[1024]; 
        int bytes; 
  
        // Copy requested file into the socket's output stream. 
        while ((bytes = fis.read(buffer)) != -1) { 
            os.write(buffer, 0, bytes); 
        } 
    } 
 
    /** 
     * Method to return appropriate 
     * @param fileName  
     */ 
    private static String contentType(String fileName) { 
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) { 
            return "text/html"; 
        } 
        /** 
         * create an HTTP response message consisting of the requested file preceded by header lines 
         * Now, you are just handling text/html, is there any more context-types? Find and make codes for it. 
         */ 
        if(fileName.endsWith(".ram") || fileName.endsWith(".ra")) { 
            return "audio/x-pn-realaudio"; 
        }  
        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) { 
            return "image/jpeg"; 
        } 
        return "application/octet-stream" ; 
    } 
 
    /** 
     * Get the File name, and through the file name, get the size of the file. 
     *.@param fileName 
    */ 
    private static long getFileSizeBytes(String fileName) throws IOException { 
        File file = new File(fileName); 
        return file.length(); 
    } 
  // This method returns the size of the specified file in bytes. 
}