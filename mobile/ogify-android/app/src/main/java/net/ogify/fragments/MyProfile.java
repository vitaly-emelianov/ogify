package net.ogify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ogify.R;
import net.ogify.custom.CustomFragment;
import net.ogify.fragments.helpers.TaskList;
import net.ogify.fragments.helpers.enums.OrderNamespace;

public class MyProfile extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, null);
        setHasOptionsMenu(true);

        setTouchNClick(v.findViewById(R.id.p1));
        setTouchNClick(v.findViewById(R.id.p2));
        setTouchNClick(v.findViewById(R.id.p3));

        Fragment taskList = new TaskList();

        Bundle args = new Bundle();
        args.putInt("namespace", OrderNamespace.ASSIGNED_TO_ME.ordinal());
        taskList.setArguments(args);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.assignToMeList, taskList).commit();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_assign_new, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.action_assign:
                Fragment f = new Tasks();
                String title = "Tasks";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, f).addToBackStack(title)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private void setupViewComponents(View v) {
        ListView grid = (ListView) v.findViewById(R.id.list);
        grid.setAdapter(new StoreAdapter());
    }

    private class StoreAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            if (v == null)
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.orders_list_item, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText("Store " + (pos + 1));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText((pos + 1) * 100 + " likes, " + (pos + 1) * 10
                    + " products");

            return v;
        }
    }
}
