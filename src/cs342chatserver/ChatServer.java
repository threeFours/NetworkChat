package cs342chatserver;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */

public class ChatServer implements Runnable{
	private static ExecutorService pool = Executors.newCachedThreadPool();
	
	private ArrayList<ChatRoom> roomlist;
	private ArrayList<User> userlist;
	private final int port;
	private ServerSocket sock;
    private Thread t;

    /**
     *
     * @param port
     * @throws IOException
     */
	
	public ChatServer(int port) throws IOException{
		this.port = port;
		this.sock = null;
		this.roomlist = new ArrayList<ChatRoom>();
		this.userlist = new ArrayList<User>();
        this.t = new Thread(this);
	}

    public void stop(){
        try {
            if(this.sock != null) {
                this.sock.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void start(){
		this.t.start();
	}

    /**
     *
     * @return
     */
	
	public String roomsToString(){
		String s = new String();
		for(ChatRoom room:roomlist){
			s.concat(room.getRoomName() + "\n");
		}
		return s;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public ServerSocket getServerSocket(){
		return this.sock;
	}
	
	public ArrayList<ChatRoom> getRooms(){
		return this.roomlist;
	}
	
	public ArrayList<User> getUsers(){
		return this.userlist;
	}

    public synchronized void broadcastList(){
        String[] ul = new String[userlist.size()];
        int i = 0;
        for(User u : this.userlist){
            ul[i] = u.getNick();
            i++;
        }

        String[] rl = new String[roomlist.size()];
        i = 0;
        for(ChatRoom c : this.roomlist){
            rl[i] = c.getRoomName();
            i++;
        }

        String liststring = new ListMessage(ul,rl).toString();

        for(User u : this.userlist){
            try {
                ListMessage m = new ListMessage(ul,rl);
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(u.getSocket().getOutputStream()))).print(liststring);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	public static void main(String[] args) throws IOException{

        JFrame window = new ChatServerGUI();
        window.setTitle("Server");
        window.setVisible(true);
        window.setSize(300, 100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void run() {
        try {
            this.sock = new ServerSocket(this.port);
            System.out.println("Server is now listening on port " + this.port);
            while(this.sock.isClosed() == false){
                Socket sock = this.sock.accept();

                pool.execute(new LogOnHandler(sock, this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LogOnHandler implements Runnable{

		private Socket req;
		private ChatServer serv;
		
		public LogOnHandler(Socket req,ChatServer serv) throws SocketException{
			this.req = req;
			this.req.setKeepAlive(true);
			this.serv = serv;
		}
		
		private void handleChat(ChatMessage m){
			if(m.getUserHash() != 0){
				boolean correctuser = false;
				for(User u : this.serv.getUsers()){
					if(u.getNick().equals(m.getSender()) && u.hashCode() == m.getUserHash()){
						correctuser = true;
						break;
					}
				}
				if(correctuser == false){
					System.out.println("Error: Invalid sender.");
					return;
				}
			} else {
				System.out.println("Error: Invalid sender.");
				return;
			}
			for(ChatRoom cr : this.serv.getRooms()){
				if(cr.getRoomName().equals(m.getRoom())){
					System.out.println("(" + m.getRoom() + ") " + m.getSender() +":" + m.getBody());
					cr.broadcastMessage(m);
					return;
				}
			}
			System.out.println("Error: Invalid room to send to.");
		}
		
		private void handleRoom(RoomMessage m){
			for(User u : this.serv.getUsers()){
				if(u.getNick().equals(m.getSender()) && u.hashCode() == m.getHash()){
					if(m.getAction().equals("join")){
						for(ChatRoom cr : this.serv.getRooms()){
							if(cr.getRoomName().equals(m.getName())){
								if(cr.isPrivate() == false){
									if(cr.containsUser(u) == false){
										System.out.println("User " + m.getSender() + " joined room " + m.getName());
										cr.addUser(u);
                                        serv.broadcastList();
										return;
									} else {
										System.out.println("Error: User " + m.getSender() + " is already in " + m.getName());
										return;
									}
								} else {
									System.out.println("Error: Requested room is private.");
									return;
								}
							}
						}
						System.out.println("Error: Requested room not found.");
						return;
					} else if(m.getAction().equals("exit")){
						for(ChatRoom cr : this.serv.getRooms()){
							if(cr.getRoomName().equals(m.getName())){
								if(cr.containsUser(u)){
									System.out.println("User " + m.getSender() + " exited room " + m.getName());
									cr.removeUser(u);
                                    this.serv.broadcastList();
									return;
								} else {
									System.out.println("Error: User" + m.getSender() + " was not present in " + m.getName() + " and therefore cannot be removed.");
									return;
								}
							}
						}
						System.out.println("Error: Room not found.");
						return;
					} else {
						System.out.println("Error: Invalid server request action: " + m.getAction());
						return;
					}
				}
			}
			System.out.println("Error: Invalid sender.");
		}
		
		private synchronized void handleUser(UserMessage m){
            for(User u : this.serv.getUsers()){
                if(u.getNick().equals(m.getName()) && u.hashCode() == m.getUserHash()){
                    u.setSocket(this.req);
                    this.serv.broadcastList();
                    System.out.println("User " + m.getName() + " has reconnected.");
                    return;
                } else if(u.getNick().equals(m.getName())){
                    System.out.println("Error: Message hashcode does not match stored hashcode.");
                    return;
                }
            }
            User login = new User(m.getName(), this.req);
            this.serv.getUsers().add(login);
            try {
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.req.getOutputStream()))).print(new UserMessage(m.getName(), userlist.hashCode()).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("User " + m.getName() + " has logged in.");
            this.serv.broadcastList();
		}
		
		@Override
		public void run() {
            System.out.println("Socket connection accepted from address " + this.req.getRemoteSocketAddress().toString());
			try {
				BufferedReader r = new BufferedReader(new InputStreamReader(req.getInputStream()));
                if(r.ready()) {
                    while (true) {
                        int jump = 0;
                        String runningstring = new String();
                        while (true) {
                            int i = r.read();
                            if (i != -1) {
                                char c = (char) i;
                                if (c != ':') {
                                    runningstring = runningstring.concat(Character.toString(c));
                                } else {
                                    jump = Integer.parseInt(runningstring);
                                    break;
                                }
                            } else {
                                return;
                            }
                        }
                        runningstring = new String();
                        while (runningstring.length() < jump) {
                            int i = r.read();
                            if (i != -1) {
                                runningstring = runningstring.concat(Character.toString((char) i));
                            }
                        }

                        if (runningstring.equals("chat")) {
                            this.handleChat(ChatMessage.fromStream(r));
                        } else if (runningstring.equals("room")) {
                            this.handleRoom(RoomMessage.fromStream(r));
                        } else if (runningstring.equals("user")) {
                            this.handleUser(UserMessage.fromStream(r));
                        }

                    }
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}