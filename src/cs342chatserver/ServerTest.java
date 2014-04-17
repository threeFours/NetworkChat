package cs342chatserver;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 */

public class ServerTest {

    public static void main(String[] args){
        ServerTest st = new ServerTest("127.0.0.1", "10007");
    }

    public ServerTest(String addr, String port){
        try {
            Socket out = new Socket(InetAddress.getByName(addr), Integer.parseInt(port));
            Thread t = new Thread(new ReadLoop(out));
            t.start();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out.getOutputStream()),true);
            pw.print(new UserMessage("joe", 0).toString());
            pw.flush();

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
                    if(br.ready()) {
                        System.out.print((char) br.read());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
