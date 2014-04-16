package cs342chatserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*----------------------------------------------------------------------------
 * Network Chat
 *
 * Class: CS 342 Software Design
 *
 * Created by Alex Schlake
 * April 2014
 ----------------------------------------------------------------------------*/
/*
 * Sets up and handles the server GUI.
 *
 */
public class ChatServerGUI extends JFrame implements ActionListener {

    private JPanel panel;
    private JPanel bottom;
    private JButton listening;
    private JLabel serverAddress;
    private JLabel serverPort;
    private boolean listenToggle;
    private InetAddress address;
    private ChatServer c;

    public ChatServerGUI(){

        try{
            address = InetAddress.getLocalHost();
        }catch(UnknownHostException f){
            System.out.println("Unknown host.");
        }
        listenToggle = false;
        panel = new JPanel(new GridLayout());
        bottom = new JPanel(new GridLayout());
        serverAddress = new JLabel("Address: " + address.getHostAddress());
        serverPort =  new JLabel("Port: ");
        listening = new JButton("Connect");
        listening.addActionListener(this);
        add(panel, BorderLayout.PAGE_START);
        add(bottom, BorderLayout.CENTER);
        panel.add(listening, BorderLayout.LINE_START);
        bottom.add(serverAddress, BorderLayout.LINE_START);
        bottom.add(serverPort, BorderLayout.LINE_END);

    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource() == listening){
            if(listenToggle == false){
                try{
                c = new ChatServer(10007);
                }catch(IOException f){
                    System.out.println("Error creating new chat server.");
                }
                c.start();
                serverPort.setText("Port: " + Integer.toString(c.getPort()));
                listening.setText("Disconnect");
                listenToggle = true;
            }else{
                serverPort.setText("Port: ");
                listening.setText("Connect");
                c.stop();
                listenToggle = false;
            }
        }
    }
}