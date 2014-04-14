import java.io.IOException;

/**
 * Created by Alexander on 4/14/14.
 */
public class InputThread extends Thread{

    public void main(String[] args){
        System.out.println("USER " + Client.username + " MESSAGE " + Client.chatMessage.getText());
        Client.out.println("USER " + Client.username + " MESSAGE " + Client.chatMessage.getText());
    }
}