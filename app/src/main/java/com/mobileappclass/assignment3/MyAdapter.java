package com.mobileappclass.assignment3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 0 on 2016/11/4.
 */
public class MyAdapter extends BaseAdapter{
    private Context context;
    private List<Info> items;

    public MyAdapter(Context context, List<Info> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Info info = items.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.item, null);
        ((TextView) view.findViewById(R.id.textView1)).setText(info.getTime()+": ");
        ((TextView) view.findViewById(R.id.textView2)).setText(info.getY()+",");
        ((TextView) view.findViewById(R.id.textView3)).setText(info.getX());
        String netid = info.getNetid();
        if (netid != null && !"".equals(netid)) {
            ((TextView) view.findViewById(R.id.textView4)).setText(","+netid);
        }
        return view;
    }
}
