package com.ogify.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogify.R;
import com.ogify.custom.CustomFragment;

public class Tasks extends CustomFragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employee_tabs, null);
        setHasOptionsMenu(true);

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.pager);

        mTabHost.addTab(mTabHost.newTabSpec("my").setIndicator("For Me"),
                TaskList.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("friends").setIndicator("By Friends"),
                TaskList.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("all").setIndicator("All"),
                TaskList.class, null);

        return mTabHost;
    }
}