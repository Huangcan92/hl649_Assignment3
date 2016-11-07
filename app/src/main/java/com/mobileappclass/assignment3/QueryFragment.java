package com.mobileappclass.assignment3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment {
    private Firebase mFireRef;

    private EditText tv_id;
    private Spinner spinner;
    private ListView listView;
    private Button button;

    private BaseAdapter adapter;
    private List<Info> items;


    public QueryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query, container, false);

        tv_id = (EditText) view.findViewById(R.id.tv_id);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        listView = (ListView) view.findViewById(R.id.listView);
        button = (Button) view.findViewById(R.id.button);
        items = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        mFireRef = MainActivity.mFireRef;

        mFireRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("log", "QueryFragment: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Inflate the layout for this fragment
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = tv_id.getText().toString();
                final String order = spinner.getSelectedItem().toString();
                Log.e("log", id + " " + order);
                mFireRef.child("Students").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            items.clear();
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                String date = dataSnapshot2.child("date").getValue().toString();
                                String y = dataSnapshot2.child("y").getValue().toString();
                                String x = dataSnapshot2.child("x").getValue().toString();
                                String netid = dataSnapshot2.child("netid").getValue().toString();
                                Info info = new Info(date, y, x, netid);
                                Log.e("log", info.toString());
                                if (!netid.equals(id)) {
                                    continue;
                                }
                                items.add(info);
                            }
                            if (!order.equals("Ascending")) {
                                Collections.reverse(items);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });
        return view;
    }

}
