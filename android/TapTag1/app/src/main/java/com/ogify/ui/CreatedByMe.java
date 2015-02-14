package com.ogify.ui;

import android.content.Intent;
import android.os.Bundle;
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

import com.ogify.NewOrderActivity;
import com.ogify.OrderInfoActivity;
import com.ogify.R;
import com.ogify.custom.CustomFragment;

public class CreatedByMe extends CustomFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.offers_list, null);
        setHasOptionsMenu(true);
        setupViewComponents(v);
        return v;
    }

    private void setupViewComponents(View v) {
        ListView grid = (ListView) v.findViewById(R.id.list);
        grid.setAdapter(new StoreAdapter());
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_new_order, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        R.layout.offers_list_item, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText("Store " + (pos + 1));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText((pos + 1) * 100 + " likes, " + (pos + 1) * 10
                    + " products");

            return v;
        }
    }
}
