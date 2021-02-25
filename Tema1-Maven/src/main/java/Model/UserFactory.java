package Model;

public class UserFactory {
    public User getUser(String username, String name, String password, User.Type type){
        if(type == User.Type.ADMINISTRATOR){
            return new Administrator(username, name, password, type);
        } else if(type == User.Type.COORDINATOR){
            return new Coordinator(username, name, password, type);
        } else if(type == User.Type.POSTMAN){
            return new Postman(username, name, password, type);
        } else return null;
    }
}
