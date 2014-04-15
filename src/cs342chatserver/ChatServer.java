package cs342chatserver;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer{
	private static ExecutorService pool = Executors.newCachedThreadPool();
	
	private ArrayList<ChatRoom> roomlist;
	private ArrayList<User> userlist;
	private final int port;
	private ServerSocket sock;
    private Thread t;
	
	public ChatServer(int port) throws IOException{
		this.port = port;
		this.sock = null;
		this.roomlist = new ArrayList<ChatRoom>();
		this.userlist = new ArrayList<User>();
	}
	
	public void start(){
		try {
			this.sock = new ServerSocket(port);

			while(true){
				Socket client = this.sock.accept();
				pool.execute(new LogOnHandler(client,this));
			}
		} catch (IOException e) {
			System.out.println("ERROR: Cannot listen on port " + Integer.toString(this.port));
			e.printStackTrace();
		}
	}
	
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

	public static void main(String[] args) throws IOException{

        JFrame window = new ChatServerGUI();
        window.setTitle("Server");
        window.setVisible(true);
        window.setSize(300, 100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
				for(User u : serv.getUsers()){
					if(u.getNick() == m.getSender() && u.hashCode() == m.getUserHash()){
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
			for(ChatRoom cr : serv.getRooms()){
				if(cr.getRoomName() == m.getRoom()){
					System.out.println("(" + m.getRoom() + ") " + m.getSender() +":" + m.getBody());
					cr.broadcastMessage(m);
					return;
				}
			}
			System.out.println("Error: Invalid room to send to.");
		}
		
		private void handleRoom(RoomMessage m){
			for(User u : serv.getUsers()){
				if(u.getNick() == m.getSender() && u.hashCode() == m.getHash()){
					if(m.getAction() == "join"){
						for(ChatRoom cr : serv.getRooms()){
							if(cr.getRoomName() == m.getName()){
								if(cr.isPrivate() == false){
									if(cr.containsUser(u) == false){
										System.out.println("User " + m.getSender() + " joined room " + m.getName());
										cr.addUser(u);
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
					} else if(m.getAction() == "exit"){
						for(ChatRoom cr : serv.getRooms()){
							if(cr.getRoomName() == m.getName()){
								if(cr.containsUser(u)){
									System.out.println("User " + m.getSender() + " exited room " + m.getName());
									cr.removeUser(u);
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
		
		private void handleUser(UserMessage m){
			//TODO GUI
			//TODO Implement user handler.
			//TODO Implement message broadcaster.
		}
		
		@Override
		public void run() {
			try {
				BufferedReader r = new BufferedReader(new InputStreamReader(req.getInputStream()));
				while(true){
					int jump = 0;
					String runningstring = new String();
					while(true){
						int i = r.read();
						if(i != -1){
							char c = (char) i;
							if(c != ':'){
								runningstring.concat(Character.toString(c));
							} else {
								jump = Integer.parseInt(runningstring);
								break;
							}
						}else{
							continue;
						}
					}
					runningstring = new String();
					while(runningstring.length() < jump){
						int i = r.read();
						if(i != -1){
							runningstring.concat(Character.toString((char) i));
						}
					}
					switch(runningstring){
						case "chat":
							this.handleChat(ChatMessage.fromStream(r));
						case "room":
							this.handleRoom(RoomMessage.fromStream(r));
						case "user":
							this.handleUser(UserMessage.fromStream(r));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}