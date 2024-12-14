import java.net.* ; 
 
public final class UDPWebServer { 
    public static void main(String argv[]) throws Exception { 
        //Mission 1. Fill in #1 Create DatagramSocket. 
        DatagramSocket socket = new DatagramSocket(8888); // Changed ServerSocket to DatagramSocket 
         
        // Process HTTP service requests in an infinite loop. 
        while (true) { 
        //Mission  1, Fill in #2 Init receiveData  
        // Fill in #2 Construct an object to process the HTTP request message.(Changed to DatagramPacket) 
 
            byte[] receiveData = new byte[8192]; 
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
            socket.receive(receivePacket); 
             
            // Fill in #3 Listen for a UDP packet. 
            // Fill in #3 Construct an object to process the HTTP request message(From changed constructor) 
            
            UDPHttpRequest request = new UDPHttpRequest(receivePacket, socket);  // Changed constructor 
            // Create a new thread to process the request. 
            Thread thread = new Thread(request); 
             
            // Start the thread. 
            thread.start(); 
        } 
    } 
}