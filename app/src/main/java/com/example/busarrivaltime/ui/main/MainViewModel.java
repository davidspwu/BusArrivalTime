package com.example.busarrivaltime.ui.main;

import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private ArrayList<Route> mRoutes = new ArrayList<>();
//    private RouteRecyclerViewAdapter mAdapter;


    public MainViewModel() {
        super();
    }

    public boolean addRoute(Route route) {
        return mRoutes.add(route);
    }

    public Route getRoute(int index) {
        return mRoutes.get(index);
    }

    public Route removeRoute(int index) {
        return mRoutes.remove(index);
    }

    public ArrayList<Route> getRoutes() {
        return mRoutes;
    }

//    public RouteRecyclerViewAdapter getAdapter() {
//        if (mAdapter == null) {
//            mAdapter = new RouteRecyclerViewAdapter(mRoutes);
//        }
//    }

}
