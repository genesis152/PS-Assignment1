package Model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements Serializable {
    public static enum Type {POSTMAN,COORDINATOR,ADMINISTRATOR};
    private static AtomicInteger count = new AtomicInteger(0);
    private final int ID;
    private String username;
    private String name;
    private String password;
    private Type type;


    public User(){
        this.ID = count.incrementAndGet();
    }

    public User(String username, String name, String password, Type type){
        this.username = username;
        this.ID = count.incrementAndGet();
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public int getID(){
        return this.ID;
    }

    public Type getType(){
        return this.type;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUserame(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public static void setCount(AtomicInteger count1){
        count=count1;
    }
}
