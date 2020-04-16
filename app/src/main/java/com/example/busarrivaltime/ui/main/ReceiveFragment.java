package com.example.busarrivaltime.ui.main;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busarrivaltime.R;

public class ReceiveFragment extends Fragment {

    private static final String KEY_SAVED_TEXT = "saved_text";

    private MainViewModel mViewModel;

    private TextView mMessage;
    private BroadcastReceiver mReceiver;

    public static ReceiveFragment newInstance() {
        return new ReceiveFragment();
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

                    mMessage.setText(str);

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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receive_fragment, container, false);
        mMessage = view.findViewById(R.id.message);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        // TODO: Use the ViewModel

        if (savedInstanceState != null) {
            // Rotate or restore from low mem process kill
            String text = savedInstanceState.getString(KEY_SAVED_TEXT);
            mMessage.setText(text);
        }

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
        outState.putString(KEY_SAVED_TEXT, mMessage.getText().toString());
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
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mReceiver);
    }

    public interface OnReceiveFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }
}
