import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClientHandler extends Thread {
	
	private final int port;
	
	public ClientHandler(int port) {
		this.port = port;
	}
	
	@Override
    public void run() {
		try {
//			System.out.println();
			DatagramSocket ds = new DatagramSocket(port);
			byte[] buffer = new byte[1024];
			
			while (true) {
				DatagramPacket dp = new DatagramPacket(buffer, 0, buffer.length);
				ds.receive(dp);
				
				String msg = new String(dp.getData());
				System.out.println(msg);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
