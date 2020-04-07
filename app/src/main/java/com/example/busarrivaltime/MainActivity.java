package com.example.busarrivaltime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.busarrivaltime.ui.main.MainFragment;
import com.example.busarrivaltime.ui.main.RouteFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RouteFragment.newInstance(1))
                    .commitNow();
        }
    }
}
