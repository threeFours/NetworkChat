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
 * Handles everything within the client.
 *
 */
public class Client implements Runnable {

    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress;
    private Socket socket;
    private int serverPort;
    private String username;
    private Thread t;


    public Client(String server, int port, String user) throws IOException{
        serverAddress = server;
        serverPort = port;
        username = user;
        this.t = new Thread(this);
    }

    public void start(){
        this.t.start();
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

    public static void main(String[] args) throws IOException {
        ClientGUI window = new ClientGUI();
        window.setTitle("Client");
        window.setVisible(true);
        window.setSize(500, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getServerAddress(){ return serverAddress; }

    public int getServerPort(){ return serverPort; }

    public void sendMessage(String messageType, String message){
        switch (messageType){
            case "user":
                out.print(new UserMessage(username, 0).toString());
                break;
            case "chat":
                out.print(new ChatMessage(username, 0, "room", message));
                break;
            case "room":
                out.print(new RoomMessage("room", username, 0, "action"));
                break;
            default:
                break;
        }
    }

    public void run(){
        try{
            socket = new Socket(serverAddress, serverPort);
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
