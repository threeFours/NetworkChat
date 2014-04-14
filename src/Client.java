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
    protected static TextArea chatArea;
    protected static TextField serverAddress;
    protected static TextField serverPort;
    protected static TextField chatMessage;
    protected static String username;
    protected static boolean listenToggle;
    protected ClientThread client;
    protected InputThread input;
    protected static Socket socket = null;
    protected static PrintWriter out = null;
    protected static BufferedReader in = null;
    protected static BufferedReader stdIn = null;

    public Client() throws IOException{

        listenToggle = false;
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        top = new JPanel(new GridLayout());
        bottom = new JPanel(new GridLayout());
        connection = new JButton("Start");
        sendMessage = new JButton("Send");
        connection.addActionListener(this);
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatMessage = new TextField(40);
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
                client = new ClientThread();
            }else{
                System.out.println("Disconnecting...");
                chatArea.append("\nDisconnecting...");
                try{
                    out.close();
                    in.close();
                    stdIn.close();
                    socket.close();
                }catch(IOException f){
                    System.out.println("Unable to close streams and connection.");
                }
                connection.setText("Start");
                listenToggle = false;
            }
        }
        if(e.getSource() == sendMessage){

        }
    }
}
