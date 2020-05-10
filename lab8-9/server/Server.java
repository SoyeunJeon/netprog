//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.SocketException;
//import java.util.ArrayList;
//
//public class Server {
//
//	public static void main(String[] args) {
//		new Server();
//	}
//	
////	public Server()
////    {
////        System.out.println("Server running");
////        DatagramSocket ds = null;
////
////        // this is an array list of ClientHandlers objects
////        ArrayList<ClientHandler> connections = new ArrayList<>();
////
////        try {
////                                      
////            ds = new DatagramSocket(61256); 
////
////            while(true) {
////                connections.add(new ClientHandler(61256)); // Create a connection between server and client
////                connections.get(connections.size()-1).start();
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        finally {
////            ds.close();
////        }
////    }
//	
//	public Server() {
//		System.out.println("Server running..");
//		try {
//			DatagramSocket ds = new DatagramSocket(61256);
////			byte[]buffer = new byte[1024]; 	
//			
////			DatagramPacket dp = null;
//			
//			while(true) {
//				
//				byte[] buffer = new byte[1024]; 	
//				DatagramPacket dp = new DatagramPacket(buffer,0,buffer.length);
//				ds.receive(dp);
//				
//				String msg = new String(dp.getData());
//				System.out.println(msg);
//				
//				InetAddress address = dp.getAddress();
//				int port = dp.getPort();
//				
//				System.out.println(address.toString() + " " + port);
////				
////				String msgToClient = "Server received msg : " + msg;
////				
////				DatagramPacket dpSend = new DatagramPacket(msgToClient.getBytes(), msgToClient.length(), address, 61256);
////				ds.send(dpSend);
//			}
//			
//		} catch (SocketException e) {
//			System.out.println("socketexception");
//		} catch (IOException e) {
//			System.out.println("ioexception");
//		}
//		
//		
//		
//	}
//}


import java.io.*;
import java.net.*;

public class Server {
	public static void main(String args[]) throws Exception {
		 DatagramSocket serverSocket = new DatagramSocket(61256);
//		 BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		    byte[] receiveData = new byte[1024];
		    byte[] sendData = new byte[1024];
//		    while(true) {
	          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	          System.out.println("before receive");
	          serverSocket.receive(receivePacket);
	          System.out.println("after received");
	          
	          if (receivePacket.getLength() != 0) {
	        	  System.out.println("length != 0");
	          }
	          
	          String sentence = new String( receivePacket.getData());
	          System.out.println("Received from Client: " + sentence);
	          
	          InetAddress IPAddress = receivePacket.getAddress();
	          int port = receivePacket.getPort();
	          
//	          System.out.println("Enter msg to Client: ");
//	          String capitalizedSentence = inFromUser.readLine();
//	          sendData = capitalizedSentence.getBytes();
//	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//	          serverSocket.send(sendPacket);
//		   }
	          String capitalizedSentence = "Server will be closed in 5 seconds";
	          sendData = capitalizedSentence.getBytes();
	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	          serverSocket.send(sendPacket); 
	          
	          Thread.sleep(5000);
	          String bye = "Server is about to be closed. Bye";
	          sendData = bye.getBytes();
	          DatagramPacket lastPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	          serverSocket.send(lastPacket); 
	          serverSocket.close();
      }
}