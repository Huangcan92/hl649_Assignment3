package com.mobileappclass.assignment3;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment {
    private Firebase mFireRef;

    private TextView tv_state;
    private TextView tv_type;
    private ListView listView;
    private Button button;

    private BaseAdapter adapter;
    private List<Info> items;

    public ServerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        tv_state = (TextView) view.findViewById(R.id.tv_state);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        listView = (ListView) view.findViewById(R.id.listView);
        button = (Button) view.findViewById(R.id.button);
        items = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        mFireRef = new Firebase("https://assignment-3-7a0b6.firebaseio.com");
        mFireRef.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        try {
                            String date = dataSnapshot2.child("date").getValue().toString();
                            String y = dataSnapshot2.child("y").getValue().toString();
                            String x = dataSnapshot2.child("x").getValue().toString();
                            String netid = dataSnapshot2.child("netid").getValue().toString();

                            Info info = new Info(date, y, x, netid);
                            Log.e("log", info.toString());
                            items.add(info);
                            if (items.size() == 200) {
                                adapter.notifyDataSetChanged();
                                tv_state.setText("Connected");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                tv_state.setText("Connected");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        view.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFireRef.child("Students").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        items.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                String date = dataSnapshot2.child("date").getValue().toString();
                                String y = dataSnapshot2.child("y").getValue().toString();
                                String x = dataSnapshot2.child("x").getValue().toString();
                                String netid = dataSnapshot2.child("netid").getValue().toString();
                                Info info = new Info(date, y, x, netid);
                                Log.e("log", info.toString());
                                items.add(info);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        tv_state.setText("Connected");
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Info> infoList = ((MainActivity) getActivity()).localFragment.sync();
                for (int i = 0;i<items.size();i++) {
                    if ("hl649".equals(items.get(i).getNetid())) {
                        infoList.add(items.get(i));
                    }
                }
                Firebase students = MainActivity.mFireRef.child("Students");
                Map<String, Map<String, String>> datas = new LinkedHashMap<>();

                for (int i = 0; i < infoList.size(); i++) {
                    Info info = infoList.get(i);
                    Map<String, String> infos = new HashMap<>();
                    infos.put("date", info.getTime().substring(0,14));
                    infos.put("y", info.getY());
                    infos.put("x", info.getX());
                    infos.put("netid", "hl649");
                    datas.put(info.getTime().replace("/", "-"), infos);
                }
//                Map<String, Map<String, Map<String, String>>> updates = new HashMap<>();
//                updates.put("hl649", datas);
                students.child("hl649").setValue(datas);
            }
        });

        initReceiver();
        return view;
    }

    BroadcastReceiver br = new MyReceiver();

    private void initReceiver() {
        IntentFilter intent = new IntentFilter();
        intent.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intent.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(br, intent);
    }

    class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            List<Info> infoList = ((MainActivity) getActivity()).localFragment.sync();
            Firebase students = MainActivity.mFireRef.child("Students");

            Map<String, Map<String, String>> datas = new LinkedHashMap<>();

            for (int i = 0; i < infoList.size(); i++) {
                Info info = infoList.get(i);
                Log.e("log",info.toString());
                Map<String, String> infos = new HashMap<>();
                infos.put("date", info.getTime().substring(0,14));
                infos.put("y", info.getY());
                infos.put("x", info.getX());
//                infos.put("y", "10.000000");
//                infos.put("x", "10.000000");
                infos.put("netid", "hl649");
                datas.put(info.getTime().replace("/", "-"), infos);
            }
//            Map<String, Map<String, Map<String, String>>> updates = new HashMap<>();
//            updates.put("hl649", datas);
            students.child("hl649").setValue(datas);
            return null;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(br);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    WifiManager wifiManager = (WifiManager) context
                            .getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo.getBSSID() != null) {
                        String ssid = wifiInfo.getSSID();
                        tv_type.setText(ssid.replace("\"", ""));
                        Log.e("log", "NETWORK_STATE_CHANGED_ACTION " + ssid);
                    }
                } else {
                    tv_state.setText("Unconnected");
                }
            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                    WifiManager wifiManager = (WifiManager) context
                            .getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = wifiManager.getConnectionInfo();
                    if (info.getBSSID() != null) {
                        String ssid = info.getSSID();
                        tv_type.setText(ssid.replace("\"", ""));
                        Log.e("log", "WIFI_STATE_CHANGED_ACTION " + ssid);
                        if (ssid != null && !"".equals(ssid)) {
                            new MyAsyncTask().execute();
                        }
                    }
                }
            }
        }
    }
}
