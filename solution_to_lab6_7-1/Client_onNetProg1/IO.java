import java.io.*;

public class IO implements Runnable
{
    // You must keep the 100_megabyte_file.txt in the same folder as IO.java file
    private static final String ONE_HUNDRED_MEGABYTE_FILE = "100_megabyte_file_.txt";

    private FileInputStream fileInputStream;
    private BufferedInputStream bufferedInputStream;
    private byte [] buffer;
    private Thread thread;

    /*
    * Starts the IO thread, which will run in parallel to the Socket connection
    * */
    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }

    /*
     * Prevents the race condition cause between the IO thread and the main thread that's connecting to the server
     * */
    public void join()
    {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Asynchronous reading
     * */
    public void run()
    {
        read();
    }

    /*
     * Returns 100 megabytes
     * */
    public byte[] getBuffer()
    {
        return buffer;
    }

    /*
     * Read 100 megabytes and assigns the data to the buffer
     * */
    private void read()
    {
        System.out.println(ONE_HUNDRED_MEGABYTE_FILE);
        File file = new File(ONE_HUNDRED_MEGABYTE_FILE);

        this.buffer = new byte[(int)file.length()];

        try
        {
            fileInputStream = new FileInputStream(ONE_HUNDRED_MEGABYTE_FILE);
            bufferedInputStream = new BufferedInputStream(fileInputStream);

            bufferedInputStream.read(this.buffer,0 , this.buffer.length);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (bufferedInputStream != null) bufferedInputStream.close();
            }
            catch (IOException e)
            {

            }
        }
    }
}
