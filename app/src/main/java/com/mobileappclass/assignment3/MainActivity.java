package com.mobileappclass.assignment3;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    LocalFragment localFragment;
    public static Firebase mFireRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("log","create");
        Firebase.setAndroidContext(this);
        mFireRef = new Firebase("https://assignment-3-7a0b6.firebaseio.com");
        localFragment = new LocalFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, localFragment).commit();
        if (isLand()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_other, new ServerFragment()).commit();
        }
        Intent intent = new Intent();
        intent.setClass(this, LocalService.class);
        startService(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("log","onDestroy");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isLand()) {
            menu.add(Menu.NONE, 0, 0, "Offline");
            menu.add(Menu.NONE, 1, 1, "Online");
            menu.add(Menu.NONE, 2, 2, "Query");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, localFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ServerFragment()).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new QueryFragment()).commit();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isLand() {
        Configuration configuration = getResources().getConfiguration();
        int orientation = configuration.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(this,LocalService.class);
        stopService(intent);
        System.exit(0);
    }
}
