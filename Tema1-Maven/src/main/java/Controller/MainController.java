package Controller;

import Model.*;

import java.awt.*;

public class MainController {
    private AuthenticationController authenticationController;
    private AdministratorController administratorController;
    private ParcelController parcelController;

    public MainController() throws InterruptedException {
        this.authenticationController = new AuthenticationController(this);
        this.parcelController = new ParcelController(this);

//        User user1 = new User("John","JohnDoe","pass", User.Type.POSTMAN);
//        User user2 = new User("Marie","Marie1","word", User.Type.POSTMAN);
//        User user3 = new User("Cicada","Cicada","coord", User.Type.COORDINATOR);
//        User user4 = new User("admin","admin","root", User.Type.ADMINISTRATOR);
//        authenticationController.addUser(user1);
//        authenticationController.addUser(user2);
//        authenticationController.addUser(user3);
//        authenticationController.addUser(user4);
//        Serializer.serializeUsers(authenticationController.getUsers());
//
//        Parcel parcel2 = new Parcel.ParcelBuilder()
//                                .address("Constanta")
//                                .coordinates(new Point(6,3))
//                                .assignedPostmanID(1)
//                                .build();
//        Parcel parcel3 = new Parcel.ParcelBuilder()
//                                .address("Oradea")
//                                .coordinates(new Point(3,5))
//                                .assignedPostmanID(1)
//                                .build();
//        Parcel parcel4 = new Parcel.ParcelBuilder()
//                                .address("Timisoara")
//                                .coordinates(new Point(4,4))
//                                .assignedPostmanID(1)
//                                .build();
//        Parcel parcel1 = new Parcel.ParcelBuilder()
//                                .address("Cluj")
//                                .coordinates(new Point(10,10))
//                                .assignedPostmanID(1)
//                                .build();
//        Parcel parcel5 = new Parcel.ParcelBuilder()
//                                .address("Deva")
//                                .coordinates(new Point(0,5))
//                                .assignedPostmanID(1)
//                                .build();
//
//        parcelController.addParcel(parcel1);
//        parcelController.addParcel(parcel2);
//        parcelController.addParcel(parcel3);
//        parcelController.addParcel(parcel4);
//        parcelController.addParcel(parcel5);

       // authenticationController.verifyLogin("Cicada", "coord");
//        Thread.sleep(2000);
//        return;
    }

    public void serialize(){
        System.out.println("Saving data");
        Serializer.serializeUsers(authenticationController.getUsers());
        Serializer.serializeParcels(parcelController.getParcels());
        System.out.println("Finished saving data!");
        parcelController.printParcelGraph();
    }

    public Object getGraphLayout(){
        return parcelController.getGraphLayout();
    }

    public void switchView(User user){
        if(user.getType()== User.Type.ADMINISTRATOR){
            authenticationController.endView();
            administratorController = new AdministratorController(this,authenticationController);
        }
        if(user.getType() == User.Type.POSTMAN){
            authenticationController.endView();
            PostmanViewController postmanViewController = new PostmanViewController(this, parcelController, user);
        }
        if(user.getType() == User.Type.COORDINATOR){
            authenticationController.endView();
            CoordinatorViewController coordinatorViewController = new CoordinatorViewController(this, parcelController, authenticationController, user);
        }
    }
}
