import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Server {
    public static void main(String [] args) {
        new Server();
    }

    private ServerSocket serverSocket;
    
    protected static PrintWriter logWriter = null;
    protected static FileWriter logFile = null;
    
    public int currPlayer = 0;
    private int clientCounter = 0;
    private static int answer = 0;
    
    private static ArrayList<ClientHandler> waitingList;
    private static ArrayList<String> ranking = new ArrayList<>();
    
    public Server() {
        System.out.println("Server running");
        serverSocket = null;
        
        // init buffer, list, and socket to start the server client communication
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        waitingList = new ArrayList<>();

        try {
        	// init log file to write the game log between server and client
        	logFile = new FileWriter("guessing-game-log.txt");
            logWriter = new PrintWriter(logFile, true);
        	
            serverSocket = new ServerSocket(61256); // Binds to the server port
            serverSocket.setSoTimeout(15000); // socket will time out in 15 min which means max time of the game to be played
            
            String input = "";
            
            while(true) {
            	
            	outer:
            	while(!input.equals("y")) {
            		Socket socket = serverSocket.accept(); // accept new connection
            		System.out.println("New player connected : host= " + socket.getInetAddress().getHostName());
            		writeLog("New player connected : host= " + socket.getInetAddress().getHostName());

            		if (clientCounter%3==0) {
            			randomNumberGen();
            		}

            		joinQueue(socket); // add new connection in the queue to wait for the game to start
            		clientCounter++;

            		System.out.println("There are Currently " + waitingList.size() + " players in the queue.");
            		System.out.println("Do you want to start a game? Enter y to start, n to wait for another player: ");
            		input = reader.readLine();
            		if (input.equals("y")) {
        				break outer;
        			} else {
        				System.out.println("Waiting for more players....");
        				continue;
        			}
            	}
            
            	// to make sure there is more than 1 player to start the game
            	if (waitingList.size()==0) {
            		System.out.println("There are no players to start a game. Please wait......");
            		input = "";
            		continue;
            	} 
            	
            	startGame(); 
            	
            	// after all players finish game, print ranking of the players
            	String temp = "";
            	for (int i=0; i<ranking.size();i++) {
            		int r = i+1;
            		temp = " " + r + "." + ranking.get(i) + temp;
            	}
            	System.out.println("Game finished. The ranking of the game is: " + temp + "\n");
            	Server.writeLog("SERVER>> Game finished. The ranking of the game is: " + temp);
            	ranking.clear();
            	
            	// remove players from the queue after game finished
            	for (int j=0;j<currPlayer;j++) {
            		waitingList.remove(0);
            	}
            	
            	// to move on to next
            	System.out.println("Do you want to start another game? "
            			+ "Enter 'y' to start, 'n' to wait for another player. If you want to close the server, enter 'e': ");
            	input = reader.readLine();
            	while (!input.equals("y") && !input.equals("n") && !input.equals("e")) {
            		System.out.println("Enter valid input: ");
            		input = reader.readLine();
            	}
            	if (input.equals("n")) {
            		System.out.println("Waiting for more players....");
    			} else if (input.equals("e")) {
    				System.exit(0);
    			}
            }
        } catch (SocketTimeoutException e) {
        	System.out.println("Game has ended due to no response.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void startGame() {
    
    	System.out.println("-----Game Started-----");
    	writeLog("New game started : " + timeLog());
    	
    	// start each game thread, depends on the number of players max of 3
    	if (waitingList.size() == 1) { // when there is only one player
    		currPlayer = 1;
    		waitingList.get(0).start();
    		try {
    			waitingList.get(0).join(); // join thread to make sure to wait for other threads
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	} else if (waitingList.size() == 2) { // when there are 2 players in the queue
    		currPlayer = 2;
    		waitingList.get(0).start();
    		waitingList.get(1).start();
    		try {
    			waitingList.get(0).join();
    			waitingList.get(1).join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	} else if (waitingList.size() > 3) { // when there are more than 3 players in the queue, max player 3
    		currPlayer = 3;
    		waitingList.get(0).start();
    		waitingList.get(1).start();
    		waitingList.get(2).start();
    		try {
    			waitingList.get(0).join();
    			waitingList.get(1).join();
    			waitingList.get(2).join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    // add thread in arraylist to join the game queue
    public static synchronized void joinQueue(Socket socket) {
    	waitingList.add(new ClientHandler(socket, answer)); 
    }
    
    // generate random number for the game
    private void randomNumberGen() {
    	Random random = new Random();
		answer = random.nextInt(13);
    }
    
    // add player in order when they finish game on the list to rank
    public static synchronized void addRank(String name) {
    	ranking.add(name);
    }
    
    // log and time writer
    public static synchronized void writeLog(String msg) {
        logWriter.println(msg);
     }
    
    public static String timeLog() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date).toString();
     }
}