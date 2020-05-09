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

//    private static final int STATE_INIT = 0;
//    private static final int STATE_NAV_BACK = 1;
//    private static final int STATE_ROTATE = 2;
//    private static final int STATE_LOW_MEM = 3;

    public static final String INIT_TEXT = "Click on search button";
    public static final String SEPARATOR = "\n---------------\n\n";
    private static final int SAVED_TEXT_MAX = 10;  // only save last 10 messages

    private static final String PREF_NAME = "bus_routes";
    private static final String PREF_ROUTE_SIZE = "size";
//    private static final String PREF_KEY = "routes";
    private static final String PREF_SEPARATOR = ":";
    private static final String PREF_TEXT = "text";

    private boolean mIsInitialized;
//    private Context mContext;

    private ArrayList<Route> mRoutes = new ArrayList<>();
    private String mText;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
//    private RouteRecyclerViewAdapter mAdapter;


//    public MainViewModel() {
//        super();
//    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

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

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    // make sure data is loaded before using
    public void load(Bundle saveInstanceState) {
        // load for init or restoring process killed by low memory

        // 4 possible situations: init, restore from low mem process kill, rotate, navigate back
        // to this fragment from another.  Only init and restore from low mem process kill need to
        // rebuild ViewModel.
        if (!mIsInitialized) {
            String data = null;
            if (saveInstanceState != null) {
                // restore from low mem process kill, get data from saveInstanceState
                data = "";
            }

            loadFromPref(data);
        }


//        int state = -1;
//        if (saveInstanceState == null) {
//            if (!mIsInitialized) {
//                state = STATE_INIT;
//            } else {
//                state = STATE_NAV_BACK;
//            }
//        } else {
//            if (!mIsInitialized) {
//                state = STATE_LOW_MEM;
//            } else {
//                state = STATE_ROTATE;
//            }
//        }
//
//        switch (state) {
//            case STATE_INIT:
//                loadFromPref(null);
//                break;
//            case STATE_NAV_BACK:
//                // already loaded, do nothing
//                break;
//            case STATE_ROTATE:
//                // already loaded, do nothing
//                break;
//            case STATE_LOW_MEM:
//                String data = "";
//                if (saveInstanceState != null) {
//                    // get data from saveInstanceState
//                }
//                loadFromPref(data);
//                break;
//            default:
//                break;
//        }
    }

    private void loadFromPref(String data) {
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

        mText = sharedPref.getString(PREF_TEXT, INIT_TEXT);

        mIsInitialized = true;
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

        String[] texts = mText.split(SEPARATOR);
        int length = texts.length;
        int beginIndex = 0;
        if (length > SAVED_TEXT_MAX) {
            beginIndex = length - SAVED_TEXT_MAX;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = beginIndex; i < length; i++) {
            sb.append(texts[i]);
            if (i < length-1) {
                sb.append(SEPARATOR);
            }
        }

        editor.putString(PREF_TEXT, sb.toString());

        editor.commit();
    }

}
