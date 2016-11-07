package com.mobileappclass.assignment3;


import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragment extends Fragment {

    private ListView listView;

    private BaseAdapter adapter;
    private List<Info> items;
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm:ss aaa",Locale.US);

    private SQLHelper helper;

    public LocalFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        helper = new SQLHelper(getActivity());
        items = new ArrayList<>();
        items.addAll(helper.getInfos());
        adapter = new MyAdapter(getActivity(), items);


        listView.setAdapter(adapter);
        LocalService.mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Info info = new Info(sdf.format(new Date()).toLowerCase(), String.format("%.6f", location.getLatitude()), String.format("%.6f", location.getLongitude()), "");
                items.add(0, info);
                helper.add(info);
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        return view;
    }
    public List<Info> sync() {
        return items;
    }
    public void sync1() {
        Firebase students = MainActivity.mFireRef.child("Students");

        Map<String, Map<String, String>> datas = new LinkedHashMap<>();

        for (int i = 0; i < items.size(); i++) {
            Info info = items.get(i);
            Map<String, String> infos = new HashMap<>();
            infos.put("date", info.getTime());
            infos.put("y", info.getY());
            infos.put("x", info.getX());
            infos.put("netid", info.getNetid());
            datas.put(info.getTime().replace("/", "-"), infos);
        }
        Map<String, Map<String, Map<String, String>>> updates = new HashMap<>();

        updates.put("hl649", datas);
        students.setValue(updates);
    }

}
