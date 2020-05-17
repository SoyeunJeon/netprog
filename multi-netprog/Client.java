import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	private static final String HOST = "jupiter.csit.rmit.edu.au";
	private static final int PORT = 61256;
	
	private DataInputStream dis;
    private DataOutputStream dos;
	
	private BufferedReader brOut;
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
		
		Socket client = null;
				
		try {
			while(true) {
				client = new Socket(HOST, PORT);
				
				// init input and output streams and reader to get user input
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
				
				brOut = new BufferedReader(new InputStreamReader(System.in));
			
				// get msg from server that will register player with their first name
				System.out.println("<<<<<-- Welcome to Guessing Game -->>>>>");
				System.out.println("Game will start soon. Please wait....");
				System.out.println(dis.readUTF());
				dos.writeUTF(brOut.readLine());
				
				// play guessing game, getting msg from server and send the response back 
				System.out.println("<- Game Started ->");
				String msg = "";
				while (true) {
					msg = dis.readUTF();
					System.out.println(msg);
					if (msg.contains("Correct!") || msg.contains("failed")) { // game finish after 4 attempts or success
						break;
					}
					dos.writeUTF(brOut.readLine());
				}
				
				// ask user to quit game or play new game
				System.out.println(dis.readUTF());
				msg = brOut.readLine();
				dos.writeUTF(msg);
				if (msg.equals("q")) {
					System.exit(0);
				} else if (msg.equals("p")) {
					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
	          if(client != null) client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
