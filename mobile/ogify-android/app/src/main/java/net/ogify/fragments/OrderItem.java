package net.ogify.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ogify.OrderInfoActivity;
import net.ogify.R;
import net.ogify.custom.CustomFragment;
import net.ogify.fragments.helpers.enums.OrderNamespace;

public class OrderItem extends CustomFragment {
    OrderNamespace namespace = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_item, null);
        setupViewComponents(v);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int index = bundle.getInt("namespace");
            namespace = OrderNamespace.values()[index];
        }

        return v;
    }

    private void setupViewComponents(final View v) {
        TextView closeButton = (TextView) v.findViewById(R.id.item_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewManager)v.getParent()).removeView(v);
            }
        });
    }

}
