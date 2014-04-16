package cs342chatserver;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 */

public class ServerTest {

    public void main(String[] args){
        try {
            Socket out = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
            Thread t = new Thread(new ReadLoop(out));
            t.start();
            PrintWriter pw = new PrintWriter(out.getOutputStream());
            pw.write(new UserMessage("joe",0).toString());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ReadLoop implements Runnable{

        private Socket sock;

        public ReadLoop(Socket sock){
            this.sock = sock;
        }

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
                while(sock.isClosed() == false){
                    System.out.print(br.read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
