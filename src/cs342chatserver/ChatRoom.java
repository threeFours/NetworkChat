package cs342chatserver;

import java.util.ArrayList;

/**
 * Contains a room name and a list of users for that chat room. Used to 
 * maintain the data of a particular room.
 * 
 * Also contains an option to prevent new users from joining, effectively creating
 * a private room.
 * @author Joe
 *
 */

public class ChatRoom {
	
	private String roomname;
	private ArrayList<User> users;
	private boolean priv;
	
	/**
	 * 
	 * @param roomname
	 * @param port
	 */
	
	public ChatRoom(String roomname, int port){
		this.roomname = roomname;
		this.users = new ArrayList<User>();
		this.priv = false;
	}
	
	public void broadcastMessage(ChatMessage m){
		//TODO
	}
	
	public String getRoomName(){
		return this.roomname;
	}
	
	public ArrayList<User> getUsers(){
		return this.users;
	}
	
	public boolean isPrivate(){
		return this.priv;
	}
	
	public void setPrivacy(boolean priv){
		this.priv = priv;
	}
	
	public void addUser(User u){
		if(priv == false){
			this.users.add(u);
		}
	}
	
	public void removeUser(User u){
		this.users.remove(u);
	}
	
	public boolean containsUser(User u){
		return this.users.contains(u);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roomname == null) ? 0 : roomname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatRoom other = (ChatRoom) obj;
		if (roomname == null) {
			if (other.roomname != null)
				return false;
		} else if (!roomname.equals(other.roomname))
			return false;
		return true;
	}
	
}