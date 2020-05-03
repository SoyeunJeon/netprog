import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	
	private static final String HOST = "titan.csit.rmit.edu.au";
	private static final int PORT = 61256;
	
    private OutputStream outputStream;
    private InputStream inputStream;
	
	private BufferedReader brOut;
	
	public static void main(String[] args) {
		new Client();
	}

	class Reminder extends TimerTask {
	    public void run() {
	       System.out.println("Please enter your response."); 
	    }
	}
	
	public Client() {
		Socket client = null;
		String userInput = null;
		
		try {
			client = new Socket(HOST, PORT);
			
			outputStream = client.getOutputStream();
			inputStream = client.getInputStream();
			brOut = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				System.out.println(receive(inputStream));
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new Reminder(), 5000, 5000);
				userInput = brOut.readLine();
				timer.cancel();
				send(userInput);
				
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
	
	private void send(String msg) {
        try{
            outputStream.write((msg).getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private String receive(InputStream inputStream) {
		
        int character = 0;
        StringBuffer stringBuffer = new StringBuffer();
        byte [] buffer = new byte[1024];

        try {
            inputStream.read(buffer);

            for(int i=0; i < 1024; i++) {
                if(buffer[i] == 0)
                    break;

                stringBuffer.append((char)buffer[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 

        return stringBuffer.toString();
    }
	
}
