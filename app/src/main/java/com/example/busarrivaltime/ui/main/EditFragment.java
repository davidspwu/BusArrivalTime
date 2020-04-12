package com.example.busarrivaltime.ui.main;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.busarrivaltime.R;

public class EditFragment extends Fragment {

    public static final String ARG_INDEX = "index";
//    public static final String ARG_IS_EDIT = "is_edit";

    private MainViewModel mViewModel;
    private int mIndex;

    private EditText mPhone;
    private EditText mBus;
    private EditText mStop;
    private EditText mDescription;
    private Button mDone;
    private Button mDelete;

    public static EditFragment newInstance(int index) {
        EditFragment f = new EditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        f.setArguments(args);
        return f;
    }

//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_fragment, container, false);
        mPhone = v.findViewById(R.id.phone);
        mBus = v.findViewById(R.id.bus);
        mStop = v.findViewById(R.id.stop);
        mDescription = v.findViewById(R.id.description);
        mDone = v.findViewById(R.id.done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhone.getText().toString();
                String bus = mBus.getText().toString();
                String stop = mStop.getText().toString();
                String description = mDescription.getText().toString();

                if (mIndex >= 0) {
                    // save change to ViewModel
                    Route r = mViewModel.getRoute(mIndex);
                    r.mPhone = phone;
                    r.mBus = bus;
                    r.mStop = stop;
                    r.mDescription = description;
                } else {
                    // add new route
                    Route r = new Route(phone, bus, stop, description);
                    mViewModel.addRoute(r);
                }

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mDelete = v.findViewById(R.id.delete);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.removeRoute(mIndex);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        // TODO: Use the ViewModel

        mIndex = getArguments().getInt(ARG_INDEX);
        if (mIndex >= 0) {
            Route r = mViewModel.getRoute(mIndex);
            mPhone.setText(r.mPhone);
            mBus.setText(r.mBus);
            mStop.setText(r.mStop);
            mDescription.setText(r.mDescription);
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.GONE);
        }
    }

}
