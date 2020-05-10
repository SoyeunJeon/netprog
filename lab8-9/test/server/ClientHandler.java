
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientHandler extends Thread{
	
	private DatagramPacket receivedPacket;
	
	public ClientHandler(DatagramPacket receivedPacket) {
		this.receivedPacket = receivedPacket;
	}

	@Override
	public void run() {
        
        try {
        	
        	InetAddress IPAddress = receivedPacket.getAddress();
            int port = receivedPacket.getPort();
            
            System.out.println("Received from Client >> Address: " + IPAddress.toString() 
            + " Port: " + port + " msg: " + new String(receivedPacket.getData()));
            
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] sendData = new byte[1024];
//			
//			String warningMsg = "Server will be closed in 5 seconds";
//			sendData = warningMsg.getBytes();
//			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//			serverSocket.send(sendPacket);
//			
//			Thread.sleep(5000);
//			
//			String greet = "Server is about to be closed. Bye";
//			sendData = greet.getBytes();
//			DatagramPacket lastPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//			serverSocket.send(lastPacket);
//			
//			serverSocket.close();
						
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} 
//        catch (InterruptedException e) {
//			e.printStackTrace();
		} 
        catch (IOException e) {
			e.printStackTrace();
		}
	}

}
