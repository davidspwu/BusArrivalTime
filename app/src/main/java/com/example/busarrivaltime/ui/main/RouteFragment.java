package com.example.busarrivaltime.ui.main;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.busarrivaltime.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

///**
// * A fragment representing a list of Items.
// * <p/>
// * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
// * interface.
// */
public class RouteFragment extends Fragment implements RouteRecyclerViewAdapter.OnListInteractionListener {

    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
//    private int mColumnCount = 1;
//    private OnListFragmentInteractionListener mListener;




    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";

    private static final String KEY_IS_LOADING = "is_loading";

    private MainViewModel mViewModel;
    private RecyclerView mListView;
//    private RouteRecyclerViewAdapter mAdapter;

    private FloatingActionButton mAddButton;

    private BroadcastReceiver mReceiver;

    private long mClickTime;
//    private int mClickIndex = -1;

    private AlertDialog mLoadingDialog;
//    private boolean mIsLoading;

    private ArrayList<OnSendSMSListener> mOnSendSMSListeners = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RouteFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RouteFragment newInstance(/*int columnCount*/) {
        RouteFragment fragment = new RouteFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        mLoadingDialog.dismiss();
                        returnToResultFragment();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getContext(), "Generic failure",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getContext(), "No service",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getContext(), "Null PDU",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getContext(), "Radio off",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };


        //---when the SMS has been sent---
        getContext().registerReceiver(mReceiver, new IntentFilter(SENT));

        //---when the SMS has been delivered---
//        getContext().registerReceiver(new BroadcastReceiver(){
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode())
//                {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getContext(), "SMS delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getContext(), "SMS not delivered",
//                                Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(DELIVERED));




//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }

//        Context context = getContext();
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.loading_dialog, null);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(v)
                .setCancelable(false);
        mLoadingDialog = builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_list, container, false);

        mAddButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, EditFragment.newInstance(-1))
                        .addToBackStack(null)
                        .commit();
            }
        });


        // Set the adapter
//        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mListView = (RecyclerView) view.findViewById(R.id.list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mListView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                    layoutManager.getOrientation());
            mListView.addItemDecoration(dividerItemDecoration);



            // re-ordering functionality
            ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    Collections.swap(mViewModel.getRoutes(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    // no-op
                }

                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                            ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
                }
            });
            touchHelper.attachToRecyclerView(mListView);



//        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        // TODO: Use the ViewModel

        // load data
        mViewModel.load(savedInstanceState);

        if (savedInstanceState == null) {
            // init or navigate back from another fragment
            // as list is created each time, do nothing
        } else {
            // rotate or recover from low memory process kill, restore transient UI

            boolean isLoading = savedInstanceState.getBoolean(KEY_IS_LOADING);
            if (isLoading) {
                mLoadingDialog.show();
            }
        }

        mListView.setAdapter(new RouteRecyclerViewAdapter(mViewModel.getRoutes(), this));
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.save();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mListener = null;
        getContext().unregisterReceiver(mReceiver);
        mLoadingDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_IS_LOADING, mLoadingDialog.isShowing());
    }

    @Override
    public void onListInteraction(int index, Route route) {

        // prevent quick double click
        long time = System.currentTimeMillis();
        if (time - mClickTime < 1000) {
            return;
        }
        mClickTime = time;




//        builder.setMessage("Sending SMS");
//                .setCancelable(false);
//                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // FIRE ZE MISSILES!
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });

        // show loading dialog
        mLoadingDialog.show();

//        // save route index
//        mClickIndex = index;

        String phone = route.mPhone;
        String message = route.mStop + " " + route.mBus;
        sendSMS(index, phone, message);
    }

    @Override
    public void onEditButtonInteraction(int index) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, EditFragment.newInstance(index))
                .addToBackStack(null)
                .commit();
    }

//    private void launchReceiveFragment() {
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, ResultFragment.newInstance(mClickIndex))
//                .addToBackStack(null)
//                .commit();
//    }

    private void returnToResultFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    //---sends an SMS message to another device---
    private void sendSMS(int index, String phoneNumber, String message)
    {
        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(getContext(), 0,
                new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

        // notify listener
        for (int i = 0; i < mOnSendSMSListeners.size(); i++) {
            mOnSendSMSListeners.get(i).onSend(index);
        }
    }

    public void registerOnSendSMSListener(OnSendSMSListener listener) {
        mOnSendSMSListeners.add(listener);
    }

    public interface OnSendSMSListener {
        public void onSend(int index);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        OnSendSMSListener listener = (OnSendSMSListener)getParentFragment();
//        registerOnSendSMSListener(listener);
//    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    @Override
//    public void onListFragmentInteraction(Route route) {
////        Toast.makeText(MainActivity.this, item.toString(), Toast.LENGTH_LONG).show();
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, ReceiveFragment.newInstance())
//                .addToBackStack(null)
//                .commit();
//    }
//
//    @Override
//    public void onEditButtonInteraction(int index) {
//       getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, EditFragment.newInstance(index))
//                .addToBackStack(null)
//                .commit();
//    }


//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction(Route route);
//        void onEditButtonInteraction(int index);
//    }
}
