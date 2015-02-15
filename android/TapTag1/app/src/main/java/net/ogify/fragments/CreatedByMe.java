package net.ogify.fragments;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ogify.NewOrderActivity;
import net.ogify.OrderInfoActivity;
import net.ogify.R;
import net.ogify.custom.CustomFragment;
import net.ogify.fragments.helpers.TaskList;
import net.ogify.fragments.helpers.enums.OrderNamespace;

public class CreatedByMe extends CustomFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.linear_layout, null);
        setHasOptionsMenu(true);

        Fragment taskList = new TaskList();

        Bundle args = new Bundle();
        args.putInt("namespace", OrderNamespace.MY.ordinal());
        taskList.setArguments(args);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.linear_layout, taskList).commit();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_new_order, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
