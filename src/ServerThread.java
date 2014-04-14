import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

/**
 * Created by Alexander on 4/14/14.
 */
public class ServerThread extends Thread {

    public void main(String[] args){
        ServerThread server = new ServerThread();
    }

    public ServerThread(){
        start();
    }

    public void run(){
        if( Server.listenToggle == false){
            System.out.println("Starting listening...");
            try {
                Server.serverSocket = new ServerSocket(10007);
                // serverSocket = new ServerSocket(0);
                System.out.println ("Connection Socket Created on port " + Server.serverSocket.getLocalPort());
                Server.listenToggle = true;
                Server.listening.setText("Stop");
                try {
                    while (Server.serverContinue)
                    {
                        Server.serverSocket.setSoTimeout(100000);
                        System.out.println ("Waiting for Connection");
                        try{
                            new ServerLogic(Server.serverSocket.accept());
                        }catch(SocketTimeoutException ste){
                            System.out.println("Timeout.");
                        }
                    }
                }
                catch (IOException f)
                {
                    System.err.println("Accept failed.");
                }
            }
            catch (IOException f)
            {
                System.err.println("Could not listen on port: " + Server.serverSocket.getLocalPort());
            }
            finally
            {
                try {
                    System.out.println("Closing server connection socket.");
                    Server.serverSocket.close();
                }
                catch (IOException f)
                {
                    System.err.println("Could not close port: " + Server.serverSocket.getLocalPort());
                    System.exit(1);
                }
            }
        }
        else{
            try {
                System.out.println("Closing server connection socket.");
                Server.serverSocket.close();
            }
            catch (IOException f)
            {
                System.err.println("Could not close port: " + Server.serverSocket.getLocalPort());
            }
            Server.listenToggle = false;
            Server.listening.setText("Start");
        }
    }
}