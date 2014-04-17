package cs342chatserver;


import java.io.BufferedReader;
import java.io.IOException;

public class ListMessage {

    private String[] users;
    private String[] rooms;

    public ListMessage(String[] users, String[] rooms){
        this.users = users;
        this.rooms = rooms;
    }

    public String[] getUsers(){
        return this.users;
    }

    public String[] getRooms(){
        return this.rooms;
    }

    public ListMessage parse(String text){
        String runningstring = new String();
        int index = 0;
        int jump;

        //Gets the size of the list of users.
        while(text.charAt(index) != '('){
            runningstring = runningstring.concat(Character.toString(text.charAt(index)));
            index++;
        }
        jump = Integer.parseInt(runningstring);
        String[] u = new String[jump];

        //Loops through the string parsing out the users.
        for(int i = 0; i < u.length ; i++){
            runningstring = new String();
            while(text.charAt(index) != ':'){
                runningstring = runningstring.concat(Character.toString(text.charAt(index)));
                index++;
            }
            jump = Integer.parseInt(runningstring);
            String us = text.substring(++index, index+jump);
            index += jump;
            u[i] = us;
        }
        index++;

        //Gets the size of the list of rooms.
        while(text.charAt(index) != '('){
            runningstring = runningstring.concat(Character.toString(text.charAt(index)));
            index++;
        }
        jump = Integer.parseInt(runningstring);
        String[] r = new String[jump];

        //Loops through the string parsing out the rooms.
        for(int i = 0; i < r.length ; i++){
            runningstring = new String();
            while(text.charAt(index) != ':'){
                runningstring = runningstring.concat(Character.toString(text.charAt(index)));
                index++;
            }
            jump = Integer.parseInt(runningstring);
            String rs = text.substring(++index, index+jump);
            index += jump;
            r[i] = rs;
        }
        index++;

        return new ListMessage(u,r);

    }

    @Override
    public String toString(){
        String s = new String("4:list");

        s = s.concat(Integer.toString(this.users.length) + "(");

        for(String u : this.users){
            s = s.concat(Integer.toString(u.length()) + ":" + u);
        }

        s = s.concat(")" + Integer.toString(this.rooms.length) + "(");

        for(String r: this.rooms){
            s = s.concat(Integer.toString(r.length()) + ":" + r);
        }

        s = s.concat(")");

        return s;
    }

    public synchronized static ListMessage fromStream(BufferedReader br) throws IOException{
        String runningstring = new String();
        char c = '0';

        while(c != '('){
            c = (char) br.read();
            runningstring = runningstring.concat(Character.toString(c));
        }
        int size = Integer.parseInt(runningstring);

        String[] users = new String[size];

        for(int i = 0; i < users.length; i++){
            runningstring = new String();
            c = '0';
            while(c != ':'){
                c = (char) br.read();
                runningstring = runningstring.concat(Character.toString(c));
            }
            size = Integer.parseInt(runningstring);

            runningstring = new String();
            for(int j = 0; j < size; j++){
                runningstring = runningstring.concat(Character.toString((char) br.read()));
            }

            users[i] = runningstring;
        }
        br.read();

        runningstring = new String();
        c = '0';

        while(c != '('){
            c = (char) br.read();
            runningstring = runningstring.concat(Character.toString(c));
        }
        size = Integer.parseInt(runningstring);

        String[] rooms = new String[size];

        for(int i = 0; i < rooms.length; i++){
            runningstring = new String();
            c = '0';
            while(c != ':'){
                c = (char) br.read();
                runningstring = runningstring.concat(Character.toString(c));
            }
            size = Integer.parseInt(runningstring);

            runningstring = new String();
            for(int j = 0; j < size; j++){
                runningstring = runningstring.concat(Character.toString((char) br.read()));
            }

            rooms[i] = runningstring;
        }
        br.read();

        return new ListMessage(users,rooms);
    }

}
