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

	/**
	 * Constructor that takes a user name string.
	 * @param name
	 */
	
	public UserMessage(String name, int hash){
		this.name = name;
		this.hash = hash;
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
		
		return new UserMessage(n,h);
	}
	
	/**
	 * Returns a formatted string containing the data enclosed in the class.
	 * @param
	 * @return
	 */
	
	public String toString(){
		String out = new String("4:user");
		out.concat(Integer.toString(this.name.length()) + ":" + this.name + Integer.toString(Integer.toString(this.hash).length()) + Integer.toString(this.hash));
		return out;
	}
	
	/**
	 * Returns a new UserMessage instance but generates that instance from a 
	 * BufferedReader.
	 * @param br
	 * @return
	 * @throws IOException
	 */
	
	public synchronized static UserMessage fromStream(BufferedReader br) throws IOException{
		String runningstring = new String();
		int temp;
		int jump;

        //
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
		
		return new UserMessage(n,h);
	}
}
