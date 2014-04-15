package cs342chatserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

/**
 * Created by Alexander on 4/13/14.
 */
public class Client extends JFrame implements ActionListener {

    private JPanel top;
    private JPanel bottom;
    protected static JButton connection;
    private JLabel address;
    private JLabel port;
    private JButton sendMessage;
    private TextArea chatArea;
    private TextField serverAddress;
    private TextField serverPort;
    private TextField chatMessage;
    private String username;
    private boolean listenToggle = false;


    public Client() throws IOException{

        top = new JPanel(new GridLayout());
        bottom = new JPanel(new GridLayout());
        connection = new JButton("Connect");
        sendMessage = new JButton("Send");
        connection.addActionListener(this);
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatMessage = new TextField(20);
        serverAddress = new TextField(20);
        address = new JLabel("Server Address", SwingConstants.RIGHT);
        serverPort = new TextField(20);
        port = new JLabel("Server Port", SwingConstants.RIGHT);
        top.add(address);
        top.add(serverAddress);
        top.add(port);
        top.add(serverPort);
        top.add(connection);
        bottom.add(chatMessage);
        bottom.add(sendMessage);
        add(top, BorderLayout.PAGE_START);
        add(chatArea, BorderLayout.CENTER);
        add(bottom, BorderLayout.PAGE_END);

        //username = JOptionPane.showInputDialog("Enter your username.");
        username = "testUser";

    }

    public static void main(String[] args) throws IOException {
        JFrame window = new Client();
        window.setTitle("Client");
        window.setVisible(true);
        window.setSize(500, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == connection){
            if( listenToggle == false){
                chatArea.append("\nConnecting...");

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
}
