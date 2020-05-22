import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread {

    private Socket connection;
	private DataInputStream dis;
    private DataOutputStream dos;
    
    public static int MAX_GUESS = 4;
    private int guess_counter = 0;
    private int answer;
    private String playerN;
    

    public ClientHandler(Socket connection) {
    	this.connection = connection;
	}

	@Override
    public void run() {
    	
		answer = Server.getAnswer();
        try {
        	// init input and output stream
            dis = new DataInputStream(connection.getInputStream()); 
            dos = new DataOutputStream(connection.getOutputStream());
            
            // register player by their name
            dos.writeUTF("Player name? = ");
            playerN = dis.readUTF();
            System.out.println(playerN + " started guessing game.");
            Server.writeLog("SERVER>> " + playerN + " registered.");
            
			System.out.println("Generated random number is : "+answer);
			
			gameBody();
			
			// when the game finishes, add the player in the ranking list
			Server.addRank(playerN);
			
			// ask client to play new game or quit the game
			dos.writeUTF("Do you want to play again? Enter 'p' to play again or 'q' to quit: ");
			String resp = dis.readUTF();
			if (resp.equals("p")) {
				Server.joinQueue(connection);
			} else if (resp.equals("q")) {
				
			}
			
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
				if(dis != null) dis.close();
			    if(dos != null) dos.close();
                if(connection != null) connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
    }
    
    public void gameBody() {
    	try {
    		
			dos.writeUTF("Guess the number between 0 and 12: ");
			Server.writeLog("SERVER>> Guess the number:");
			
			// ask clients to guess the number, max guess is 4 and it will ends after 4 attempts if each answer is wrong
			while(guess_counter < MAX_GUESS) {
				guess_counter++;
				
				int guessed = validate(dis.readUTF());
				
				if (guessed == -1) { // invalid input
					dos.writeUTF("Invalid input. Please enter your guess again: ");
					Server.writeLog("SERVER>> Invalid input. Please enter your guess again:");
					guess_counter--;
					continue;
				}
				
				System.out.println("[" + playerN + "'s attempt" + guess_counter + "] " + "Player guessed number = " + guessed);
				Server.writeLog("[" + playerN + "'s attempt" + guess_counter + "] " + "Player guessed number = " + guessed);
				
				if (guessed == answer) {
					dos.writeUTF("Correct!");
					Server.writeLog("SERVER>> Correct!");
					
					System.out.println(playerN + " finished game.");
					Server.writeLog("SERVER>> " + playerN + " finished game. " + Server.timeLog());
					break;
				} else if (guessed > answer && guess_counter != 4){ // client response is bigger than the answer
					dos.writeUTF("Answer is smaller. Guess attempt " + guess_counter + ": ");
					Server.writeLog("SERVER>> Answer is smaller. Guess attempt " + guess_counter + ": ");
				} else if (guessed < answer && guess_counter != 4) { // client response is smaller than the answer
					dos.writeUTF("Answer is bigger. Guess attempt " + guess_counter + ": ");
					Server.writeLog("SERVER>> Answer is bigger. Guess attempt " + guess_counter + ": ");
				} else if (guess_counter == 4) {
					dos.writeUTF("No more guess counter left. Guess failed.");
					Server.writeLog("SERVER>> No more guess counter left. Guess failed.");
					
					System.out.println(playerN + " finished game.");
					Server.writeLog("SERVER>> " + playerN + " finished game. " + Server.timeLog());
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    // validate the input to make sure its only in number between 0 to 12
	public int validate(String input) {
		String regex = "[0-9]+";
		if (input.matches(regex)) {
			int temp = Integer.parseInt(input);
			if (temp <= 12 && temp >= 0) {
				return temp;
			}
		} 
		return -1;
	}
	
}
