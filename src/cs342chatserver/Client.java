package cs342chatserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
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
    private String input;
    private Thread t;
    private int hash;


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

    public void run(){

        String runningstring = new String();
        Random rand = new Random();
        int temp;
        int jump;
        try{
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.print(new UserMessage(username, rand.nextInt((10000 - 1) + 1) + 1).toString());
            out.flush();

            while(true){
                if(in.ready()){
                    break;
                }
            }
            while(true){
                temp = in.read();
                if(temp != -1){
                    char c = (char) temp;
                    if(c != ':'){
                        runningstring = runningstring.concat(Character.toString(c));
                    } else {
                        jump = Integer.parseInt(runningstring);
                        break;
                    }
                } else {
                    continue;
                }
            }
            runningstring = new String();
            while(runningstring.length() < jump){
                temp = in.read();
                if(temp != -1){
                    runningstring = runningstring.concat(Character.toString((char) temp));
                }
            }
            String s = new String(runningstring);

            runningstring = new String();
            while(true){
                temp = in.read();
                if(temp != -1){
                    char c = (char) temp;
                    if(c != ':'){
                        runningstring = runningstring.concat(Character.toString(c));
                    } else {
                        jump = Integer.parseInt(runningstring);
                        break;
                    }
                } else {
                    continue;
                }
            }
            runningstring = new String();
            while(runningstring.length() < jump){
                temp = in.read();
                if(temp != -1){
                    runningstring = runningstring.concat(Character.toString((char) temp));
                }
            }
            String t = new String(runningstring);

            runningstring = new String();
            while(true){
                temp = in.read();
                if(temp != -1){
                    char c = (char) temp;
                    if(c != ':'){
                        runningstring = runningstring.concat(Character.toString(c));
                    } else {
                        jump = Integer.parseInt(runningstring);
                        break;
                    }
                } else {
                    continue;
                }
            }
            runningstring = new String();
            while(runningstring.length() < jump){
                temp = in.read();
                if(temp != -1){
                    runningstring = runningstring.concat(Character.toString((char) temp));
                }
            }
            hash = Integer.parseInt(runningstring);

        }catch(IOException e){
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

    public synchronized void sendMessage(String messageType, String message){

        String runningstring = new String();
        String b = new String();
        int temp;
        int jump;

        switch (messageType){
            case "chat":
                System.out.println("chat");
                out.print(new ChatMessage(username, hash, "room", message).toString());
                out.flush();
                try{
                    while(true){
                        if(in.ready()){
                            break;
                        }
                    }

                    while(true){
                        temp = in.read();
                        if(temp != -1){
                            char c = (char) temp;
                            if(c != ':'){
                                runningstring = runningstring.concat(Character.toString(c));
                            } else {
                                jump = Integer.parseInt(runningstring);
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    runningstring = new String();
                    while(runningstring.length() < jump){
                        temp = in.read();
                        if(temp != -1){
                            runningstring = runningstring.concat(Character.toString((char) temp));
                        }
                    }
                    String s = new String(runningstring);
                    System.out.println(s);
                    runningstring = new String();
                    while(true){
                        temp = in.read();
                        if(temp != -1){
                            char c = (char) temp;
                            if(c != ':'){
                                runningstring = runningstring.concat(Character.toString(c));
                            } else {
                                jump = Integer.parseInt(runningstring);
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    runningstring = new String();
                    while(runningstring.length() < jump){
                        temp = in.read();
                        if(temp != -1){
                            runningstring = runningstring.concat(Character.toString((char) temp));
                        }
                    }
                    int h = Integer.parseInt(runningstring);

                    runningstring = new String();
                    while(true){
                        temp = in.read();
                        if(temp != -1){
                            char c = (char) temp;
                            if(c != ':'){
                                runningstring = runningstring.concat(Character.toString(c));
                            } else {
                                jump = Integer.parseInt(runningstring);
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    runningstring = new String();
                    while(runningstring.length() < jump){
                        temp = in.read();
                        if(temp != -1){
                            runningstring = runningstring.concat(Character.toString((char) temp));
                        }
                    }
                    String r = new String(runningstring);

                    runningstring = new String();
                    while(true){
                        temp = in.read();
                        if(temp != -1){
                            char c = (char) temp;
                            if(c != ':'){
                                runningstring = runningstring.concat(Character.toString(c));
                            } else {
                                jump = Integer.parseInt(runningstring);
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    runningstring = new String();
                    while(runningstring.length() < jump){
                        temp = in.read();
                        if(temp != -1){
                            runningstring = runningstring.concat(Character.toString((char) temp));
                        }
                    }
                    b = new String(runningstring);
                }catch(IOException e){
                    e.printStackTrace();
                }
                System.out.println(b);
                break;
            case "room":
                out.print(new RoomMessage("room", username, 0, "action"));
                break;
            default:
                break;
        }
    }
}
