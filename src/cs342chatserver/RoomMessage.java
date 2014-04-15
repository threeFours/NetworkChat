package cs342chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains a message to be sent to clients and received by the server to
 * update clients on how many users are in a particular room.
 * 
 * Also used when a user wants to create a new room.
 * @author Joe
 *
 */

public class RoomMessage {
	private String name;
	private String sender;
	private int hash;
	private String action;
	private String[] users;
	
	/**
	 * Creates a new instance of a RoomMessage with a room name and an 
	 * empty list of users in that room. (Meant to be sent by clients.)
	 * @param name
	 * @param sender
	 * @param hash
	 * @param action
	 */
	
	public RoomMessage(String name, String sender, int hash, String action){
		this.name = name;
		this.sender = sender;
		this.hash = hash;
		this.action = action;
		this.users = null;
	}
	
	/**
	 * Creates a new instance of RoomMessage with a room name and a
	 * list of users in that room. (Meant to be sent by the server 
	 * to update the users on who is in a particular room.
	 * @param name
	 * @param sender
	 * @param hash
	 * @param action
	 * @param users
	 */
	
	public RoomMessage(String name, String sender, int hash, String action, String[] users){
		this.name = name;
		this.sender = sender;
		this.hash = hash;
		this.action = action;
		this.users = users;
	}
	
	/**
	 * Returns the name of the room.
	 * @return
	 */
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns a list of users in the room.
	 * @return
	 */
	
	public String getSender(){
		return this.sender;
	}
	
	/**
	 * Returns the sender's hash.
	 * @return
	 */
	
	public int getHash(){
		return this.hash;
	}
	
	/**
	 * Returns the string describing the action the 
	 * client wants to take in regard to the specified room.
	 * @return
	 */
	
	public String getAction(){
		return this.action;
	}
	
	/**
	 * Returns a list of users 
	 * @return
	 */
	
	public String[] getUsers(){
		return this.users;
	}
	
	/**
	 * Generates an instance of RoomMessage by parsing a formatted string.
	 * @param text
	 * @return
	 */
	
	public static RoomMessage parse(String text){
		int index = 0;
		String runningstring = new String();
		
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		int jump = Integer.parseInt(runningstring);
		String n = text.substring(++index,index+jump);
		index += jump;
		
		runningstring = new String();
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		jump = Integer.parseInt(runningstring);
		String s = text.substring(++index,index+jump);
		index += jump;
		
		runningstring = new String();
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		jump = Integer.parseInt(runningstring);
		int h = Integer.parseInt(text.substring(++index,index+jump));
		index += jump;
		
		runningstring = new String();
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		jump = Integer.parseInt(runningstring);
		String a = text.substring(++index,index+jump);
		index += jump;
		
		ArrayList<String> u = new ArrayList<String>();
		while(index < text.length()){
			runningstring = new String();
			while(text.charAt(index) != ':'){
				runningstring = runningstring.concat(Character.toString(text.charAt(index)));
				index++;
			}
			jump = Integer.parseInt(runningstring);
			u.add(text.substring(++index,index+jump));
			index += jump;
		}
		
		return new RoomMessage(n,s,h,a,u.toArray(new String[u.size()]));
	}
	
	/**
	 * Returns a formatted string representation of the class to be sent and received over sockets.
	 */
	
	public String toString(){
		String s = new String("4:room" +
				Integer.toString(this.name.length()) + ":" + this.name +
				Integer.toString(this.sender.length()) + ":" + this.sender +
				Integer.toString(Integer.toString(this.hash).length()) + ":" + Integer.toString(this.hash) +
				Integer.toString(this.action.length()) + ":" + this.action +
				Integer.toString(users.length) +"(");
		
		for(String u : users){
			s.concat(Integer.toString(u.length()) + ":" + u);
		}
		
		s.concat(")");
		return s;
	}
	
	/**
	 * Generates an instance of RoomMessage by reading in a string from a BufferedReader.
	 * @param br
	 * @return
	 * @throws IOException
	 */
	
	public static RoomMessage fromStream(BufferedReader br) throws IOException{
		String runningstring = new String();
		int temp;
		int jump;
		//Reads in room name
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring.concat(Character.toString(c));
				} else {
					jump = Integer.parseInt(runningstring);
					break;
				}
			} else {
				continue;
			}
		}
		runningstring = new String();
		while(runningstring.length() < jump){
			temp = br.read();
			if(temp != -1){
				runningstring.concat(Character.toString((char) temp));
			}
		}
		String n = new String(runningstring);
		
		//Reads in sender.
		runningstring = new String();
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring.concat(Character.toString(c));
				} else {
					jump = Integer.parseInt(runningstring);
					break;
				}
			} else {
				continue;
			}
		}
		runningstring = new String();
		while(runningstring.length() < jump){
			temp = br.read();
			if(temp != -1){
				runningstring.concat(Character.toString((char) temp));
			}
		}
		String s = new String(runningstring);
		
		//Reads in hash code for user.
		runningstring = new String();
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring.concat(Character.toString(c));
				} else {
					jump = Integer.parseInt(runningstring);
					break;
				}
			} else {
				continue;
			}
		}
		runningstring = new String();
		while(runningstring.length() < jump){
			temp = br.read();
			if(temp != -1){
				runningstring.concat(Character.toString((char) temp));
			}
		}
		int h = Integer.parseInt(runningstring);
		
		//Reads in the string representing the action the message will take.
		runningstring = new String();
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring.concat(Character.toString(c));
				} else {
					jump = Integer.parseInt(runningstring);
					break;
				}
			} else {
				continue;
			}
		}
		runningstring = new String();
		while(runningstring.length() < jump){
			temp = br.read();
			if(temp != -1){
				runningstring.concat(Character.toString((char) temp));
			}
		}
		String a = new String(runningstring);
		
		//Reads in the users.
		runningstring = new String();
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring.concat(Character.toString(c));
				} else {
					jump = Integer.parseInt(runningstring);
					break;
				}
			} else {
				continue;
			}
		}
		String[] u = new String[jump];
		br.read();
		for(int i = 0;i<u.length;i++){
			runningstring = new String();
			while(true){
				temp = br.read();
				if(temp != -1){
					char c = (char) temp;
					if(c != ':'){
						runningstring.concat(Character.toString(c));
					} else {
						jump = Integer.parseInt(runningstring);
						break;
					}
				} else {
					continue;
				}
			}
			String user = new String();
			while(user.length() < jump){
				temp = br.read();
				if(temp != -1){
					user.concat(Character.toString((char) temp));
				}
			}
			u[i] = user;
		}
		br.read();
		
		return new RoomMessage(n,s,h,a,u);
	}
}
