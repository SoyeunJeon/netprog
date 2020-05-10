
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Server {
	
	public static void main(String [] args) {
        new Server();
    }

    public Server() {
		System.out.println("Server running");
		DatagramSocket serverSocket = null;
		byte[] receivedData = new byte[1024];
		
		ArrayList<ClientHandler> connections = new ArrayList<>();
		
		try {
			serverSocket = new DatagramSocket(61256);
			
			while(true) {
				DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				serverSocket.receive(receivedPacket);
				
				if (receivedPacket.getLength() != 0) {
					connections.add(new ClientHandler(receivedPacket)); // Create a connection between server and client
	                connections.get(connections.size()-1).run();
				}
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
		    if(serverSocket !=null){
		        try {serverSocket.close();}catch(Exception ex){}
		      }
		    }
	}
}
