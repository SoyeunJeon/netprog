import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private static final String ONE_HUNDRED_MEGABYTE_FILE = "100_megabyte_file_out.txt";

    private Socket connection;



    public ClientHandler(Socket connection)
    {
        this.connection = connection;
    }

    @Override
    public void run()
    {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            inputStream = connection.getInputStream(); // Access the read stream
            byte[] buffer = new byte[1024];
            int bytesRead;

            File file = new File(ONE_HUNDRED_MEGABYTE_FILE);
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            /* Reads and write the byte stream from the client onto the server disk */
            do {
                bytesRead = inputStream.read(buffer, 0, buffer.length);

                if (bytesRead <= 0)
                    break;

                bufferedOutputStream.write(buffer, 0, bytesRead);
                bufferedOutputStream.flush();
            }
            while (true);
        }
        catch (IOException e)
        {
            System.out.println("Error, with a stream");
            e.printStackTrace();
        }
        finally
        {
            try {
                bufferedOutputStream.close();
                inputStream.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
