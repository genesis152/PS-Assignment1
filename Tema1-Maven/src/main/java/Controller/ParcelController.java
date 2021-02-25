package Controller;

import Model.Parcel;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParcelController {
    private MainController mainController;
    private Map<Integer, Parcel> parcels;
    private GraphController graphController;

    public ParcelController(MainController mainController){
        setParcels();
        this.mainController = mainController;
    }

    private void setParcels(){
        //load from serialised file
        parcels = Serializer.deserializeParcels();
        Parcel.setCount(new AtomicInteger(parcels.size()));
       // parcels = new HashMap<Integer,Parcel>();
        this.graphController = new GraphController(parcels);
    }

    public void addParcel(Parcel parcel){
        parcels.put(parcel.getID(),parcel);
        graphController.addParcelToCompleteGraph(parcel);
    }

    public void updateParcel(Parcel parcel){
        parcels.remove(parcel.getID());
        parcels.put(parcel.getID(),parcel);
        graphController.updateParcelInGraph(parcel);
    }

    public void printParcelGraph(){
        graphController.printGraph();
    }

    public Object getGraphLayout(){
        return graphController.saveGraphAsLayout();
    }

    public void removeParcel(Parcel parcel){
        parcels.remove(parcel.getID(),parcel);
        graphController.removeParcelFromGraph(parcel);
    }

    public Parcel getParcelByID(int parcelID){
        return parcels.get(parcelID);
    }

    public List<Parcel> getParcelsByPostmanID(int postmanID){
        List<Parcel> parcelsList = new ArrayList<Parcel>();
        for(Parcel parcel : parcels.values()){
            if(parcel.getAssignedPostmanID() == postmanID){
                parcelsList.add(parcel);
            }
        }
        return parcelsList;
    }

    public Map<Integer,Parcel> getParcels(){
        return this.parcels;
    }

}
