package com.example.busarrivaltime.ui.main;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class MainViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private static final String PREF_NAME = "bus_routes";
    private static final String PREF_ROUTE_SIZE = "size";
//    private static final String PREF_KEY = "routes";
    private static final String PREF_SEPARATOR = ":";

    private boolean mIsInitialized;
//    private Context mContext;

    private ArrayList<Route> mRoutes = new ArrayList<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
//    private RouteRecyclerViewAdapter mAdapter;


//    public MainViewModel() {
//        super();
//    }

    public boolean addRoute(Route route) {

        for (int i = 0; i < mRoutes.size(); i++) {
            Route r = mRoutes.get(i);
            if (r.mBus.equals(route.mBus) && r.mStop.equals(route.mStop)) {
                Toast.makeText(getApplication(), "Route already exists", Toast.LENGTH_LONG).show();
                return false;
            }
        }

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

    public void init() {
        if (!mIsInitialized) {

            SharedPreferences sharedPref = getApplication().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            int size = sharedPref.getInt(PREF_ROUTE_SIZE, 0);
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    String val = sharedPref.getString(Integer.toString(i), "");
                    String[] route = val.split(PREF_SEPARATOR);
                    Route r = new Route(route[0], route[1], route[2], route[3]);
                    mRoutes.add(r);
                }
            }

            mIsInitialized = true;
        }
    }


    public void save() {
        SharedPreferences sharedPref = getApplication().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();  // clean all first

        int size = mRoutes.size();
        if (size > 0) {
            editor.putInt(PREF_ROUTE_SIZE, size);
            for (int i = 0; i < mRoutes.size(); i++) {
                Route r = mRoutes.get(i);
                String key = Integer.toString(i);
                String val = r.mPhone + PREF_SEPARATOR + r.mBus + PREF_SEPARATOR + r.mStop + PREF_SEPARATOR + r.mDescription;
                editor.putString(key, val);
            }
        }

        editor.commit();
    }

}
