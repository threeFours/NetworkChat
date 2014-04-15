package cs342chatserver;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

/**
 * Contains a username and an IP address. Both are used to prevent 
 * different users to use the same name.
 * @author Joe
 *
 */

public class User {
	private String nick;
	private Socket address;
	private Date d;
	
	/**
	 * Takes a name and socket type and returns an instance of User.
	 * @param nick
	 * @param address
	 */
	
	public User(String nick, Socket address){
		this.nick = nick;
		this.address = address;
		this.d = new Date();
	}
	
	/**
	 * Returns the username of the user.
	 * @return
	 */
	
	public String getNick(){
		return this.nick;
	}
	
	/**
	 * Returns the socket that the user is using.
	 * @return
	 */
	
	public Socket getSocket(){
		return this.address;
	}
	
	/**
	 * Returns the enclosing class for the IP address.
	 * @return
	 */
	
	public InetAddress getAddress(){
		return this.address.getInetAddress();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		result = prime * result + ((nick == null) ? 0 : nick.hashCode());
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
		User other = (User) obj;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		if (nick == null) {
			if (other.nick != null)
				return false;
		} else if (!nick.equals(other.nick))
			return false;
		return true;
	}
	
	
	
}
