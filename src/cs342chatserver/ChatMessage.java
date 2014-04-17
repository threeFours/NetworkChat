package cs342chatserver;

import java.io.BufferedReader;
import java.io.IOException;

/** 
 * 
 * Handles the strings that are sent to the server to handle chat messages.
 * The server receives these messages and then passes them along.
 * 
 * Like the other message classes, the strings are in a format 
 * in which the reader knows how long the items and lists being sent are.
 *
 * @author Joe
 */

public class ChatMessage {
	
	private String sender;
	private int hash;
	private String room;
	private String body;
	
	/**
	 * Constructs a new instance of a ChatMessage that contains a string representations
	 * of the sender's name, the sender's identifying hash code, the room's name, and the body of text in the message.
	 * @param sender
	 * @param room
	 * @param body
	 */
	
	public ChatMessage(String sender, int hash, String room, String body){
		this.sender = sender;
		this.hash = hash;
		this.room = room;
		this.body = body;
	}
	
	/**
	 * Returns the name of the sender.
	 * @return
	 */
	
	public String getSender(){
		return this.sender;
	}
	
	/**
	 * Returns the hash code of the sender.
	 * @return
	 */
	
	public int getUserHash(){
		return this.hash;
	}
	
	/**
	 * Returns the name of the room.
	 * @return
	 */
	
	public String getRoom(){
		return this.room;
	}
	
	/**
	 * Returns the body of text for the message.
	 * @return
	 */
	
	public String getBody(){
		return this.body;
	}
	
	/**
	 * Returns an instance of ChatMessage that takes the information
	 * contained in the formatted string entered as a parameter.
	 * @param text
	 * @return
	 */
	
	public static ChatMessage parse(String text){
		int index = 0;
		String runningstring = new String();
		
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		int jump = Integer.parseInt(runningstring);
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
		String r = text.substring(++index,index+jump);
		index += jump;
		
		runningstring = new String();
		while(text.charAt(index) != ':'){
			runningstring = runningstring.concat(Character.toString(text.charAt(index)));
			index++;
		}
		jump = Integer.parseInt(runningstring);
		String b = text.substring(++index,index+jump);
		index += jump;
		
		return new ChatMessage(s,h,r,b);
	}
	
	/**
	 * Contains a formatted representation of the class to be sent and 
	 * received by clients.
	 */
	
	public String toString(){
		return 
				"4:type" +
				Integer.toString(this.sender.length()) + ":" + this.sender + 
				Integer.toString(Integer.toString(this.hash).length()) + ":" + Integer.toString(this.hash) +
				Integer.toString(this.room.length()) + ":" + this.room + 
				Integer.toString(this.body.length()) + ":" + this.body;
	}
	
	/**
	 * Creates a ChatMessage instance by reading in a string from a BufferedReader.
	 * @param br
	 * @return
	 * @throws IOException
	 */
	
	public synchronized static ChatMessage fromStream(BufferedReader br) throws IOException{
		String runningstring = new String();
		int temp;
		int jump;

        while(true){
            if(br.ready()){
                break;
            }
        }

        while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring = runningstring.concat(Character.toString(c));
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
				runningstring = runningstring.concat(Character.toString((char) temp));
			}
		}
		String s = new String(runningstring);
		
		runningstring = new String();
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring = runningstring.concat(Character.toString(c));
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
				runningstring = runningstring.concat(Character.toString((char) temp));
			}
		}
		int h = Integer.parseInt(runningstring);
		
		runningstring = new String();
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring = runningstring.concat(Character.toString(c));
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
				runningstring = runningstring.concat(Character.toString((char) temp));
			}
		}
		String r = new String(runningstring);
		
		runningstring = new String();
		while(true){
			temp = br.read();
			if(temp != -1){
				char c = (char) temp;
				if(c != ':'){
					runningstring = runningstring.concat(Character.toString(c));
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
				runningstring = runningstring.concat(Character.toString((char) temp));
			}
		}
		String b = new String(runningstring);
		return new ChatMessage(s,h,r,b);
	}
}
