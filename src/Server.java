import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

/**
 * Created by Alexander on 4/13/14.
 */
public class Server extends JFrame implements ActionListener {

    private JPanel panel;
    private JPanel bottom;
    protected static JButton listening;
    protected static JLabel serverAddress;
    protected static JLabel serverPort;
    protected static ServerSocket serverSocket = null;
    protected static boolean serverContinue = true;
    protected static boolean listenToggle = false;
    private ServerThread server;
    private InetAddress address;

    public Server(){

        try{
        address = InetAddress.getLocalHost();
        }catch(UnknownHostException f){
            System.out.println("Unknown host.");
        }
        panel = new JPanel(new GridLayout());
        bottom = new JPanel(new GridLayout());
        serverAddress = new JLabel("Address: " + address.getHostAddress());
        serverPort =  new JLabel("Port: ");
        listening = new JButton("Start");
        listening.addActionListener(this);
        add(panel, BorderLayout.PAGE_START);
        add(bottom, BorderLayout.CENTER);
        panel.add(listening, BorderLayout.LINE_START);
        bottom.add(serverAddress, BorderLayout.LINE_START);
        bottom.add(serverPort, BorderLayout.LINE_END);

    }

    public static void main(String[] args) throws IOException{
        JFrame window = new Server();
        window.setTitle("Server");
        window.setVisible(true);
        window.setSize(300, 100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == listening){
            server = new ServerThread();
        }
    }
}
