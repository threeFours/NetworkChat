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
public class Client extends JFrame implements ActionListener {
    private static ExecutorService pool = Executors.newCachedThreadPool();

    private JPanel top;
    private JPanel bottom;
    private JButton connection;
    private JLabel address;
    private JLabel port;
    private TextArea chatArea;
    private TextField serverAddressInput;
    private TextField serverPortInput;
    private TextField chatMessage;
    private boolean listenToggle = false;
    private JFrame window = new JFrame();
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private ClientLogOn c;


    public Client() throws IOException{

        window.setVisible(true);
        window.setSize(500, 400);
        window.setTitle("Client");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        top = new JPanel(new GridLayout());
        bottom = new JPanel(new GridLayout());
        connection = new JButton("Connect");
        connection.addActionListener(this);
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatMessage = new TextField(20);
        serverAddressInput = new TextField(20);
        address = new JLabel("Server Address", SwingConstants.RIGHT);
        serverPortInput = new TextField(20);
        port = new JLabel("Server Port", SwingConstants.RIGHT);
        top.add(address);
        top.add(serverAddressInput);
        top.add(port);
        top.add(serverPortInput);
        top.add(connection);
        bottom.add(chatMessage);
        window.add(top, BorderLayout.PAGE_START);
        window.add(chatArea, BorderLayout.CENTER);
        window.add(bottom, BorderLayout.PAGE_END);

        username = getUsername();

        chatMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatArea.append("\n" + username + ": " + chatMessage.getText());
                chatMessage.setText("");
                c.sendMessage();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }

    private String getUsername() {
        return JOptionPane.showInputDialog(
                window,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == connection){
            if( listenToggle == false){
                chatArea.append("\nConnecting...");
                    pool.execute(c = new ClientLogOn());
            }else{
                chatArea.append("\nDisconnecting...");
                c.stop();
                connection.setText("Connect");
                listenToggle = false;
            }
        }
    }

    private class ClientLogOn implements Runnable{

        PrintWriter out;
        BufferedReader in;
        String serverAddress;
        Socket socket;
        int serverPort;

        public void run(){
            try{
                // Make connection and initialize streams
                serverAddress = serverAddressInput.getText();
                serverPort = Integer.parseInt(serverPortInput.getText());
                socket = new Socket(serverAddress, serverPort);
                chatArea.append("\nConnected to " + serverAddress + ":" + serverPort);
                connection.setText("Disconnect");
                listenToggle = true;
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.print(new UserMessage("joe",0).toString());
                System.out.println(in.read());
                out.flush();
            }catch(IOException e){
                chatArea.append("Unable to connect to server. Check address and port.");
            }
        }

        private synchronized void sendMessage(){
            System.out.println(chatMessage.getText());
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
}
