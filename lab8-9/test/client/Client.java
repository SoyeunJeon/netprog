
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	
	public BufferedReader inputReader;
	public DatagramSocket clientSocket;
	public InetAddress IPAddress;
	public String host = "netprog1.csit.rmit.edu.au";
	public int port = 61256;
	
	byte[] sendData;
	byte[] receiveData;

	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
		
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		
		sendData = new byte[1024];
		receiveData = new byte[1024];
		
		try {
			clientSocket = new DatagramSocket();
			IPAddress = InetAddress.getByName(host);
		
			System.out.println("Enter msg to Server: ");
			sendData = inputReader.readLine().getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			System.out.println("From Server: " + new String(receivePacket.getData()));
			
			DatagramPacket lastPacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(lastPacket);
			System.out.println("From Server: " + new String(lastPacket.getData()));
		
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    if(clientSocket !=null){
		        try {clientSocket.close();}catch(Exception ex){}
		    }
		}
	}
}

