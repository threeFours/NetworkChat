import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Alexander on 4/14/14.
 */
public class ClientThread extends Thread{

    public void main(String[] args){
        ClientThread client = new ClientThread();
    }

    public ClientThread(){
        start();
    }

    public void run(){
        System.out.println("Starting connection...");
        Client.chatArea.append("\nStarting connection...");
        try {
            try{
                Client.socket = new Socket(Client.serverAddress.getText(), Integer.parseInt(Client.serverPort.getText()));
            }catch(NumberFormatException f){
                System.out.println("Error with server port or server address.");
                Client.chatArea.append("\nError with server port or server address.");
            }
            try{
                Client.out = new PrintWriter(Client.socket.getOutputStream(), true);
            }catch(NullPointerException f){
                System.out.println("Error creating output stream.");
                Client.chatArea.append("\nError creating output stream.");
            }
            try{
                Client.in = new BufferedReader(new InputStreamReader(Client.socket.getInputStream()));
            }catch(NullPointerException f){
                System.out.println("Error creating input stream.");
                Client.chatArea.append("\nError creating input stream.");
            }
        } catch (UnknownHostException f) {
            System.err.println("Could not access host: " + Client.serverAddress.getText() + ":" + Client.serverPort.getText());
            Client.chatArea.append("\nCould not access host: " + Client.serverAddress.getText() + ":" + Client.serverPort.getText());
        } catch (IOException f) {
            System.err.println("Couldn't get I/O.");
            Client.chatArea.append("\nCouldn't get I/O.");
        }
        if(Client.socket != null){
            Client.connection.setText("Stop");
            Client.listenToggle = true;
            Client.chatArea.append("\nConnected to " + Client.socket.getLocalAddress());
        }
    }
}