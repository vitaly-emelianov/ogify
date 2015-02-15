package net.ogify.fragments.helpers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ogify.OrderInfoActivity;
import net.ogify.R;
import net.ogify.custom.CustomFragment;
import net.ogify.fragments.helpers.enums.OrderNamespace;

public class TaskList extends CustomFragment {
    OrderNamespace namespace = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.orders_list, null);
        setupViewComponents(v);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int index = bundle.getInt("namespace");
            namespace = OrderNamespace.values()[index];
        }

        return v;
    }

    private void setupViewComponents(View v) {
        ListView grid = (ListView) v.findViewById(R.id.list);
        grid.setAdapter(new StoreAdapter());
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                intent.putExtra("namespace", namespace.ordinal());
                startActivity(intent);
            }
        });
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
            lbl.setText(namespace.getLabel() + " " + (pos + 1));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText((pos + 1) * 100 + " likes, " + (pos + 1) * 10
                    + " products");

            return v;
        }
    }
}
