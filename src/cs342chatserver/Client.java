package cs342chatserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

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

    private JPanel top;
    private JPanel bottom;
    private JButton connection;
    private JLabel address;
    private JLabel port;
    private JButton sendMessage;
    private TextArea chatArea;
    private TextField serverAddressInput;
    private TextField serverPort;
    private TextField chatMessage;
    private boolean listenToggle = false;
    private JFrame window = new JFrame();
    private BufferedReader in;
    private PrintWriter out;
    private String username;


    public Client() throws IOException{

        window.setVisible(true);
        window.setSize(500, 400);
        window.setTitle("Client");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        top = new JPanel(new GridLayout());
        bottom = new JPanel(new GridLayout());
        connection = new JButton("Connect");
        sendMessage = new JButton("Send");
        connection.addActionListener(this);
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatMessage = new TextField(20);
        serverAddressInput = new TextField(20);
        address = new JLabel("Server Address", SwingConstants.RIGHT);
        serverPort = new TextField(20);
        port = new JLabel("Server Port", SwingConstants.RIGHT);
        top.add(address);
        top.add(serverAddressInput);
        top.add(port);
        top.add(serverPort);
        top.add(connection);
        bottom.add(chatMessage);
        bottom.add(sendMessage);
        window.add(top, BorderLayout.PAGE_START);
        window.add(chatArea, BorderLayout.CENTER);
        window.add(bottom, BorderLayout.PAGE_END);

        chatMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatArea.append("\n" + chatMessage.getText());
                chatMessage.setText("");
            }
        });
        username = getUsername();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == connection){
            if( listenToggle == false){
                chatArea.append("\nConnecting...");
                try{
                run();
                }catch(IOException f){
                    System.out.println("Error connecting to server.");
                    chatArea.append("\nError connection to server.");
                }
                connection.setText("Disconnect");
                listenToggle = true;
            }else{
                chatArea.append("\nDisconnecting...");

                connection.setText("Connect");
                listenToggle = false;
            }
        }
        if(e.getSource() == sendMessage){

        }
    }

    private void run() throws IOException {

        // Make connection and initialize streams
        String serverAddress = serverAddressInput.getText();
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("Please enter username: ")) {
                out.println(getName());
            } else if (line.startsWith("UserName accepted!")) {
                chatMessage.setEditable(true);
            } else if (line.startsWith("Message: ")) {
                chatArea.append(line.substring(8) + "\n");
            }
        }
    }

    private String getUsername() {
        return JOptionPane.showInputDialog(
                window,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }
}
