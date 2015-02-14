package com.ogify.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ScrollView;

import com.ogify.R;
import com.ogify.custom.CustomFragment;

public class Profile extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, null);

        setTouchNClick(v.findViewById(R.id.p1));
        setTouchNClick(v.findViewById(R.id.p2));
        setTouchNClick(v.findViewById(R.id.p3));

        ScrollView mainLayout = (ScrollView) getActivity().findViewById(R.id.scrollView2);
        LayoutInflater inflaterCool = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflaterCool.inflate(R.layout.offers_list, mainLayout, true);

        return v;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.p3)
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new MainFragment())
                    .addToBackStack("Elements").commit();
    }
}
