//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.util.Scanner;
//
//public class Client {
//
//	public static void main(String[] args) {
//		
//		Scanner sc = new Scanner(System.in);
//		
//		try {
//			DatagramSocket ds = new DatagramSocket();
//			
////			String str = "This is a Client.";
//			String str = "";
//			
//			InetAddress ip = InetAddress.getByName("jupiter.csit.rmit.edu.au");
//			
//			while(true) {
//				System.out.println("Enter the msg to Server: ");
//				str = sc.nextLine();
//				DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 61256);
//				
//				ds.send(dp);
//				
////				byte[] buffer = new byte[1024];
////				DatagramPacket dpReceive = new DatagramPacket(buffer, buffer.length);
////				ds.receive(dpReceive);
////				
////				String received = new String(dpReceive.getData());
////				System.out.println(received);
//			}
//			
//			
//		} catch (SocketException e) {
//			System.out.println(e.getMessage());
//		} catch (UnknownHostException e) {
//			System.out.println(e.getMessage());
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//	}
//	
//}



import java.io.*;
import java.net.*;

public class Client
{
   public static void main(String args[]) throws Exception
   {
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("jupiter.csit.rmit.edu.au");
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      
//      while(true) {
    	  System.out.println("Enter msg to Server: ");
	      String sentence = inFromUser.readLine();
	      sendData = sentence.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 61256);
	      clientSocket.send(sendPacket);
	      
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	      clientSocket.receive(receivePacket);
	      String modifiedSentence = new String(receivePacket.getData());
	      System.out.println("From Server:" + modifiedSentence);
	      
	      DatagramPacket lastPacket = new DatagramPacket(receiveData, receiveData.length);
	      clientSocket.receive(lastPacket);
	      String lastSentence = new String(lastPacket.getData());
	      System.out.println("From Server:" + lastSentence);
//      }
//	  clientSocket.close();
   }
}