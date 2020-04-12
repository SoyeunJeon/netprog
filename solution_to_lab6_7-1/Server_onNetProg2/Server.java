import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server
{
    public static void main(String [] args)
    {
        new Server();
    }

    public Server()
    {
        System.out.println("Server running");
        ServerSocket serverSocket = null;

        // this is an array list of ClientHandlers objects
        ArrayList<ClientHandler> connections = new ArrayList<>();

        try {
                                        // use the port number that you are allocated to
            serverSocket = new ServerSocket(61174); // Binds to the server port

            while(true)
            {
                connections.add(new ClientHandler(serverSocket.accept())); // Create a connection between server and client
                connections.get(connections.size()-1).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
