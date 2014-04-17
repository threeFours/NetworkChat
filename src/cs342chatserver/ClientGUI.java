package cs342chatserver;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/*----------------------------------------------------------------------------
 * Network Chat
 *
 * Class: CS 342 Software Design
 *
 * Created by Alex Schlake
 * April 2014
 ----------------------------------------------------------------------------*/
/*
 * Sets up the client GUI.
 *
 */

public class ClientGUI extends JFrame implements ActionListener {

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
    private Client c;
    private String username;

    public ClientGUI(){

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
        add(top, BorderLayout.PAGE_START);
        add(chatArea, BorderLayout.CENTER);
        add(bottom, BorderLayout.PAGE_END);

        username = setUsername();

        chatMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if( c != null){
                    c.sendMessage("chat", chatMessage.getText());
                    chatArea.append("\n" + username + ": " + chatMessage.getText());
                    chatMessage.setText("");
                }else{
                    chatArea.append("\nNot connected to server.");
                }
            }
        });
    }

    public String getUsername(){ return username; }

    private String setUsername() {
        return JOptionPane.showInputDialog(
                this,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == connection){
            if( listenToggle == false){
                chatArea.append("\nConnecting...");
                try{
                    c = new Client(serverAddressInput.getText(), Integer.parseInt(serverPortInput.getText()), username);
                }catch(IOException f){
                    f.printStackTrace();
                }
                c.start();
                connection.setText("Disconnect");
                chatArea.append("\nConnected to " + c.getServerAddress() + ":" + c.getServerPort());
                listenToggle = true;
            }else{
                chatArea.append("\nDisconnecting...");
                c.stop();
                connection.setText("Connect");
                listenToggle = false;
            }
        }
    }

}

