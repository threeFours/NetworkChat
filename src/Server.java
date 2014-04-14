import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Alexander on 4/13/14.
 */
public class Server extends JFrame implements ActionListener {

    private JPanel panel;
    protected static JButton listening;
    protected static ServerSocket serverSocket = null;
    protected static boolean serverContinue = true;
    protected static boolean listenToggle = false;
    private ServerThread server;

    public Server(){

        panel = new JPanel(new GridLayout());
        listening = new JButton("Start");
        listening.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        panel.add(listening, BorderLayout.LINE_START);

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
