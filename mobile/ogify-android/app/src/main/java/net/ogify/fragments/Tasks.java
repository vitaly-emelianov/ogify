package net.ogify.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ogify.R;
import net.ogify.custom.CustomFragment;
import net.ogify.fragments.helpers.enums.OrderNamespace;
import net.ogify.fragments.helpers.TaskList;

public class Tasks extends CustomFragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pager, null);
        setHasOptionsMenu(true);

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.pager);

        Bundle bundlePrivate = new Bundle();
        bundlePrivate.putInt("namespace", OrderNamespace.PRIVATE.ordinal());
        mTabHost.addTab(mTabHost.newTabSpec("my").setIndicator("For Me"),
                TaskList.class, bundlePrivate);

        Bundle bundleFriends = new Bundle();
        bundleFriends.putInt("namespace", OrderNamespace.FRIENDS.ordinal());
        mTabHost.addTab(mTabHost.newTabSpec("friends").setIndicator("By Friends"),
                TaskList.class, bundleFriends);

        Bundle bundleFriendsOfFriends = new Bundle();
        bundleFriendsOfFriends.putInt("namespace", OrderNamespace.FRIENDS_OF_FRIENDS.ordinal());
        mTabHost.addTab(mTabHost.newTabSpec("friends-of-friends").setIndicator("By Friends of Friends"),
                TaskList.class, bundleFriendsOfFriends);

        Bundle bundleAll = new Bundle();
        bundleAll.putInt("namespace", OrderNamespace.ALL.ordinal());
        mTabHost.addTab(mTabHost.newTabSpec("all").setIndicator("All"),
                TaskList.class, bundleAll);

        return mTabHost;
    }
}