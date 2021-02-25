package Controller;

import Model.User;
import View.LoginView;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthenticationController {

    private Map<String,User> users;
    private User currentUser;
    private MainController mainController;
    private LoginView loginView;
    private MessageDigest hasher;

    private void setUsersData(){
        users = Serializer.deserializeUsers();
        User.setCount(new AtomicInteger(users.size()));
        //users = new HashMap<String,User>();
    }

    public AuthenticationController(MainController mainController){
        setUsersData();
        this.mainController = mainController;
        currentUser = null;
        this.loginView = new LoginView(this);
        loginView.setVisible(true);
        try {
            hasher = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private String getMd5FromString(String string){
        hasher.update(string.getBytes());
        byte[] md5 = hasher.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< md5.length ;i++)
        {
            sb.append(Integer.toString((md5[i] & 0xff) + 0x100, 16).substring(1));
        }
        String md5String = sb.toString();
        return md5String;
    }

    public boolean verifyLogin(String name, String password){
        User user = users.get(name);
        if(user != null){
            hasher.update(password.getBytes());
            byte[] md5 = hasher.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< md5.length ;i++)
            {
                sb.append(Integer.toString((md5[i] & 0xff) + 0x100, 16).substring(1));
            }
            String md5Password = sb.toString();
            if(user.getPassword().equals(md5Password)){
                mainController.switchView(user);
                return true;
            }
        }
        //authentication failed
        String text = loginView.getTopLabelText();
        if(text.contains("wrong")){
            return false;
        }
        else{
            loginView.setTopLabelText(text + " - wrong username/password");
        }
        return false;
    }

    public boolean addUser(User user){
        user.setPassword(getMd5FromString(user.getPassword()));
        if(users.containsKey(user.getUsername())){
            return false;
        }else{
            users.put(user.getUsername(),user);
            return true;
        }
    }

    public void deleteUser(User user){
        users.remove(user.getUsername(),user);
    }

    public void updateUser(String name, User user){
        users.remove(name);
        users.put(name, user);
    }

    public void endView(){
        loginView.setVisible(false);
    }

    public Map<String,User> getUsers(){
        return this.users;
    }

    private void setCurrentUser(User user){
        this.currentUser = user;
    }

    public User getUserByID(int id){
        for(User user : users.values()){
            if(user.getID() == id){
                return user;
            }
        }
        return null;
    }

    public User getCurrentUser(){
        return this.currentUser;
    }
}
