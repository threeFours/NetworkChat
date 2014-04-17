package cs342chatserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*----------------------------------------------------------------------------
 * Network Chat
 *
 * Class: CS 342 Software Design
 *
 * Created by Alex Schlake
 * April 2014
 ----------------------------------------------------------------------------*/
/*
 * Handles everything within the client, including the GUI.
 *
 */
public class Client implements Runnable {

    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress;
    private Socket socket;
    private int serverPort;
    private Thread t;


    public Client(String server, int port) throws IOException{
        serverAddress = server;
        serverPort = port;
        this.t = new Thread(this);
    }

    public void start(){
        this.t.start();
    }

    public static void main(String[] args) throws IOException {
        ClientGUI window = new ClientGUI();
        window.setTitle("Client");
        window.setVisible(true);
        window.setSize(500, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getServerAddress(){ return serverAddress; }

    public int getServerPort(){ return serverPort; }

    public void run(){
        try{
            // for testing
            //serverAddress = "127.0.0.1";
            //serverPort = 10007;
            socket = new Socket(serverAddress, serverPort);
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.print(new UserMessage("joe",0).toString());
            System.out.println(in.read());
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            if(this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
