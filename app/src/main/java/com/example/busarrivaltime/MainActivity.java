package com.example.busarrivaltime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.busarrivaltime.ui.main.EditFragment;
import com.example.busarrivaltime.ui.main.ReceiveFragment;
import com.example.busarrivaltime.ui.main.Route;
import com.example.busarrivaltime.ui.main.RouteFragment;
import com.example.busarrivaltime.ui.main.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements RouteFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RouteFragment.newInstance())
                    .commitNow();
        }

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        getSupportFragmentManager().popBackStack();
//    }

    @Override
    public void onListFragmentInteraction(Route route) {
//        Toast.makeText(MainActivity.this, item.toString(), Toast.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ReceiveFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEditButtonInteraction(int index) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, EditFragment.newInstance(index))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, EditFragment.newInstance(-1))
                    .addToBackStack(null)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
