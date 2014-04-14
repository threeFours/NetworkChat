import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Alexander on 4/14/14.
 */
public class ServerLogic extends Thread{
    protected Socket clientSocket;

    public void main(String[] args){
        ServerLogic server = new ServerLogic(clientSocket);
    }

    public ServerLogic(Socket clientSoc){
        clientSocket = clientSoc;
        start();
    }
    public void run()
    {
        System.out.println ("New Communication Thread Started");

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                        true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader( clientSocket.getInputStream()));

                String inputLine;

                while (true)
                {
                    clientSocket.setSoTimeout(100000);
                    System.out.println ("Waiting for Input");
                    try {
                        if ((inputLine = in.readLine()) == null)
                            break;

                        System.out.println ("Server: " + inputLine);

                        if (inputLine.equals("?"))
                            inputLine = new String ("\"Bye.\" ends Client, " +
                                    "\"End Server.\" ends Server");

                        out.println(inputLine);

                        if (inputLine.equals("exit"))
                            break;

                        if (inputLine.equals("end server"))
                            Server.serverContinue = false;
                    }
                    catch (SocketTimeoutException ste)
                    {
                        System.out.println ("Timeout Occurred for Socket Read");
                    }
                }
                out.close();
                in.close();
                clientSocket.close();
            }
            catch (IOException e)
            {
                System.err.println("Problem with Communication Server");
                System.exit(1);
            }
    }
}
