package com.example.busarrivaltime.ui.main;

import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.busarrivaltime.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class ResultFragment extends Fragment implements RouteFragment.OnSendSMSListener {

//    public static final String ARG_INDEX = "index";

//    private static final String INIT_TEXT = "Click on search button";

//    private static final String KEY_SAVED_TEXT = "saved_text";
    private static final String KEY_SEARCH_ENABLED = "search_enabled";

//    public static final String SEPARATOR = "\n---------------\n\n";
//    private static final int SAVED_TEXT_MAX = 10;  // only save last 10 messages

    private MainViewModel mViewModel;

    private TextView mMessage;


    private BroadcastReceiver mReceiver;

    private int mCurrentIndex = -1;

    // Because we use "replace" instead of "add" to navigate fragments, all views are re-created
    // when navigating back to this fragment.  As a result, we can't directly modify mSearchButton's
    // visibility through listener, we need to store the value.
    private int mVisibility = View.VISIBLE;

    private FloatingActionButton mSearchButton;

    public static ResultFragment newInstance() {
        ResultFragment f = new ResultFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_INDEX, index);
//        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //---get the SMS message passed in---
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                String str = "";
                if (bundle != null)
                {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
//                        str += "SMS from " + msgs[i].getOriginatingAddress();
//                        str += " :";
                        str += msgs[i].getMessageBody().toString();
//                        str += "n";
                    }
                    //---display the new SMS message---
//                    Toast.makeText(context, str, Toast.LENGTH_LONG).show();

//                    int index = getArguments().getInt(ARG_INDEX);
                    Route route = mViewModel.getRoute(mCurrentIndex);

                    str = route.mStop + " [" + route.mBus + "] " + route.mDescription +"\n\n" + str;

                    String text = mMessage.getText().toString();

                    if (!MainViewModel.INIT_TEXT.equals(text) && !TextUtils.isEmpty(text)) {
                       str = text + MainViewModel.SEPARATOR + str;
                    }

                    mMessage.setText(str);

                    // reset current index, re-enable search
                    mCurrentIndex = -1;
                    mVisibility = View.VISIBLE;

                    // update UI
                    mSearchButton.setVisibility(mVisibility);

                    // scroll to bottom
                    mScroll.post(new Runnable() {
                        @Override
                        public void run() {
                            mScroll.fullScroll(View.FOCUS_DOWN);
                        }
                    });


//            if (!"".equals(str)) {
//                abortBroadcast();
//            }
                }
            }
        };


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

        //---register the receiver---
        getContext().registerReceiver(mReceiver, intentFilter);
    }

    LinearLayout mContainter;
    ScrollView mScroll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_fragment, container, false);
        mMessage = view.findViewById(R.id.message);

        mScroll = view.findViewById(R.id.scroll);
        mContainter = view.findViewById(R.id.message_container);

        mSearchButton = view.findViewById(R.id.search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteFragment f =  RouteFragment.newInstance();
                f.registerOnSendSMSListener(ResultFragment.this);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, f)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        // TODO: Use the ViewModel

        // load data
        mViewModel.load(savedInstanceState);

//        mMessage.setText(MainViewModel.INIT_TEXT);
        mMessage.setText(mViewModel.getText());

        if (savedInstanceState != null) {
            // Rotate or restore from low mem process kill
            // restore search button
            mVisibility = savedInstanceState.getInt(KEY_SEARCH_ENABLED);
//            mSearchButton.setVisibility(savedInstanceState.getInt(KEY_SEARCH_ENABLED));
        }

        mSearchButton.setVisibility(mVisibility);

        // scroll to bottom
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.fullScroll(View.FOCUS_DOWN);
            }
        });


//        if (savedInstanceState == null) {
//            // init or return from other fragment
//
//            String text = mMessage.getText().toString();
//            if (TextUtils.isEmpty(text)) {
//                // init
//                mMessage.setText(INIT_TEXT);
//            }
//        } else {
//            // Rotate or restore from low mem process kill
//
//            String savedText = savedInstanceState.getString(KEY_SAVED_TEXT);
////            StringBuilder sb = new StringBuilder();
////            String[] texts = savedText.split(SEPARATOR);
////            for (int i = 0; i < texts.length; i++) {
////                sb.append(texts[i]);
////                if (i < texts.length-1) {
////                    sb.append(SEPARATOR);
////                }
////            }
//
//            mMessage.setText(savedText);
//
//            mSearchButton.setVisibility(savedInstanceState.getInt(KEY_SEARCH_ENABLED));
//        }

//        mScroll.invalidate();
//        mScroll.requestLayout();





//        mText = getActivity().findViewById(R.id.message);
//        mText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String phoneNo = "33333";
//                String message = "52786 133";
//                if (phoneNo.length()>0 && message.length()>0)
//                    sendSMS(phoneNo, message);
//                else
//                    Toast.makeText(getContext(),
//                            "Please enter both phone number and message.",
//                            Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        String text = mMessage.getText().toString();
//        String[] texts = text.split(SEPARATOR);
//        int length = texts.length;
//        int beginIndex = 0;
//        if (length > SAVED_TEXT_MAX) {
//            beginIndex = length - SAVED_TEXT_MAX;
//        }
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = beginIndex; i < length; i++) {
//            sb.append(texts[i]);
//            if (i < length-1) {
//                sb.append(SEPARATOR);
//            }
//        }

//        outState.putString(KEY_SAVED_TEXT, sb.toString());

        outState.putInt(KEY_SEARCH_ENABLED, mSearchButton.getVisibility());
    }

    //---sends an SMS message to another device---
//    private void sendSMS(String phoneNumber, String message)
//    {
//        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
//                new Intent(getContext(), MainFragment.class), 0);
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, pi, null);
//    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.setText(mMessage.getText().toString());
        mViewModel.save();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mReceiver);
    }

    @Override
    public void onSend(int index) {
        mCurrentIndex = index;
        mVisibility = View.GONE;
//        mSearchButton.setVisibility(View.GONE);
    }

//    public interface OnReceiveFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction();
//    }



}
