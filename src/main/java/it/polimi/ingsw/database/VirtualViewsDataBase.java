package it.polimi.ingsw.database;

import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

public class VirtualViewsDataBase {
    ArrayList<VirtualView> virtualViews;
    private static VirtualViewsDataBase instance;

    private VirtualViewsDataBase() {
        virtualViews = new ArrayList<>();
    }

    public static synchronized VirtualViewsDataBase getVirtualViewsDataBase() {
        if (instance == null) {
            instance = new VirtualViewsDataBase();
        }
        return instance;
    }

    public VirtualView getVirtualView(String username){
        for(VirtualView virtualView : virtualViews){
            if(virtualView.getUsername().equals(username)){
                return virtualView;
            }
        }
        return null;
    }

    public void removeVirtualView(String username){
        VirtualView view = null;
        for(VirtualView virtualView : virtualViews){
            if(virtualView.getUsername().equals(username)){
                view = virtualView;
            }
        }
        virtualViews.remove(view);
    }

    public void addVirtualView(VirtualView virtualView){
        if(!contains(virtualView.getUsername())) {
            virtualViews.add(virtualView);
        }
    }

    public boolean contains(String username){
        for(VirtualView virtualView : virtualViews){
            if(virtualView.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public void status(){
        //TODO eliminate
        System.out.println("VIRTUAL VIEW DATABASE:\n Status:\n - Number of views: "+virtualViews.size());
        System.out.println(" - VirtualViews's players:");
        for(VirtualView view : virtualViews){
            System.out.println("   +"+view.getUsername());
        }
    }
}