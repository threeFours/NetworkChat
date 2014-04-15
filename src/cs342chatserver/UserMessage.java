package cs342chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/** 
 * 
 * Handles the strings that are sent to the server to log that user on.
 * There is an optional list in the string that allow for the user to immediately
 * log into a list of rooms.
 * 
 * Like the other message classes, the strings are in a format 
 * in which the reader knows how long the items and lists being sent are.
 *
 * @author Joe
 */

public class UserMessage {
	private String name;
	private int hash;
	private String[] rooms;
	
	/**
	 * Constructor that takes a user name string.
	 * @param name
	 */
	
	public UserMessage(String name, int hash){
		this.name = name;
		this.hash = hash;
		this.rooms = null;
	}
	
	/**
	 * Constructor that takes a user name string and a list of room names.
	 * @param name
	 * @param rooms
	 */
	
	public UserMessage(String name, int hash, String[] rooms){
		this.name = name;
		this.hash = hash;
		this.rooms = rooms;
	}
	
	/**
	 * Returns the name.
	 * @return
	 */
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns the hash code of the user.
	 * @return
	 */
	
	public int getUserHash(){
		return this.hash;
	}
	
	/**
	 * Returns an array of room name strings.
	 * @return
	 */
	
	public String[] getRooms(){
		return this.rooms;
	}
	
	/**
	 * Returns an instance of UserMessage that takes the information
	 * contained in the formatted string entered as a parameter.
	 * @param text
	 * @return
	 */
	
	public static UserMessage parse(String text){
		int index = 0;
		String runningstring = new String();
		int jump;
		
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		jump = Integer.parseInt(runningstring);
		String n = text.substring(++index, index+jump);
		index += jump;
		
		runningstring = new String();
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		jump = Integer.parseInt(runningstring);
		int h = Integer.parseInt(text.substring(++index, index+jump));
		index += jump;
		
		ArrayList<String> r = new ArrayList<String>();
		while(index < text.length()){
			runningstring = new String();
			while(text.charAt(index) != ':'){
				runningstring = runningstring.concat(Character.toString(text.charAt(index)));
				index++;
			}
			jump = Integer.parseInt(runningstring);
			r.add(text.substring(++index,index+jump));
			index += jump;
		}
		
		return new UserMessage(n,h,r.toArray(new String[r.size()]));
	}
	
	/**
	 * Returns a formatted string containing the data enclosed in the class.
	 * @param
	 * @return
	 */
	
	public String toString(){
		String out = new String("4:user");
		out.concat(Integer.toString(this.name.length()) + ":" + this.name + Integer.toString(Integer.toString(this.hash).length()) + Integer.toString(this.hash) + this.rooms.length + "(");
		for(String roomname:this.rooms){
			out.concat(Integer.toString(roomname.length()) + ":" + roomname);
		}
		out.concat(")");
		return out;
	}
	
	/**
	 * Returns a new UserMessage instance but generates that instance from a 
	 * BufferedReader.
	 * @param br
	 * @return
	 * @throws IOException
	 */
	
	public static UserMessage fromStream(BufferedReader br) throws IOException{
		String runningstring = new String();
		int temp;
		int jump;
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
		String sh = new String(runningstring);
		int h = Integer.parseInt(sh);
		
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
		String[] r = new String[jump];
		br.read();
		for(int i = 0;i<r.length;i++){
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
			String room = new String();
			while(room.length() < jump){
				temp = br.read();
				if(temp != -1){
					room.concat(Character.toString((char) temp));
				}
			}
			r[i] = room;
		}
		br.read();
		
		return new UserMessage(n,h,r);
	}
}
