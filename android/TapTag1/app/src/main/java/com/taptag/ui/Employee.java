package com.taptag.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.taptag.R;
import com.taptag.custom.CustomActivity;
import com.taptag.custom.CustomFragment;

public class Employee extends CustomFragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employee_tabs, null);

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.pager);

        mTabHost.addTab(mTabHost.newTabSpec("TabA").setIndicator("TabA"),
                Profile.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("TabB").setIndicator("TabB"),
                Employer.class, null);

        return mTabHost;
    }
}
