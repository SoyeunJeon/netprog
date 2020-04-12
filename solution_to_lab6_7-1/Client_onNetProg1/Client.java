import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client
{

    // You should run client on netprog1, and server on netprog2
    // change localhost to the where the server is running, which is netprog2
    private static final String HOST = "netprog2.csit.rmit.edu.au";
    private static final int PORT = 61174; // make sure the client and server running
                                    // on the same port, this should be one of the two
                                    // port numbers that you are allocated to

    public static void main(String [] args)
    {
        new Client();

        // create multiple cliens, each client runs on a separate thread
        
    }

    public Client()
    {
        IO io = new IO();
        io.start(); // Starts the IO thread
        Socket client= null;

        try
        {
            client = new Socket(HOST, PORT); // Creates a socket connection
            OutputStream outputStream = client.getOutputStream();
            System.out.println("Sending file");
            io.join(); // Waits for the IO Complete its task
            outputStream.write(io.getBuffer()); // Writes the complete data to the server
            outputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                if(client != null) client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
