//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.net.SocketException;
import java.io.PrintWriter;

public class Server {
	
	private InputStream inputStream;
    private OutputStream outputStream;
    private Socket connection;
    
    public static int MAX_GUESS = 4;
    private int guess_counter = 0;
	private int answer = 0;

	public static void main(String[] args) {
		new Server();
	}
	
	public Server() {
			
		ServerSocket server = null;
		
		try {
			
			System.out.println("Server running.....");
			server = new ServerSocket(61256);
			
			
			while(true) {
				
				connection = server.accept();
				System.out.println("connection established");
				
				outputStream = connection.getOutputStream();
                inputStream = connection.getInputStream();
				
				writeToClient("Player name? = ");
				
				String player = readFromClient(inputStream);
				
				System.out.println(player + " started guessing game.");
				Random random = new Random();
				answer = random.nextInt(13);
				System.out.println("Random number generated: " + answer);
				writeToClient("guess the number : ");
					
				while(guess_counter < MAX_GUESS) {

					guess_counter++;
					
					if (!connection.isConnected()) {
						System.out.println("connection dismissed");
						break;
					}
					
					int guessed=0;
					
					try{guessed = Integer.parseInt(readFromClient(inputStream));}
					catch (NumberFormatException nfe) {}
					
					if (guessed > 12 || guessed < 0) {
						writeToClient("Invalid input. Please enter your guess again: ");
						guess_counter--;
						continue;
					}
					
					if (guessed == answer) {
						System.out.println("Player got the answer!");
						writeToClient("Congratulation!");
						break;
					} else if (guessed > answer && guess_counter != 4){
						System.out.println(guess_counter + " player guessed number = " + guessed);
						writeToClient("Answer is smaller. Guess again: ");
					} else if (guessed < answer && guess_counter != 4) {
						System.out.println(guess_counter + " player guessed number = " + guessed);
						writeToClient("Answer is bigger. Guess again: ");
					} else if (guess_counter == 4) {
						writeToClient("No more guess counter left.");
						System.out.println("Player failed to guess. Game ended");
						break;
					}
					
					
				}
			}
			
		}catch (IOException e) {	
			System.out.println("IOException");
		} finally {
            try {
                if(server != null) server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	private String readFromClient(InputStream inputStream) {
		
        StringBuffer stringBuffer = new StringBuffer();
        byte [] buffer = new byte[1024];

        try {
			
            inputStream.read(buffer);

            for(int i=0; i < 1024; i++) {
                if(buffer[i] == 0)
                    break;
				
                stringBuffer.append((char)buffer[i]);
            }
			
        }catch (IOException e) {
            System.out.println("Cannot read from Client");
        } 
		return stringBuffer.toString();
        
    }
	
	private void writeToClient(String msg) {
		
        try {
            outputStream.write((msg).getBytes());
            outputStream.flush();
        }
        catch (IOException e) {
            System.out.println("Cannot write to Client");
        } 
    }
	
}
