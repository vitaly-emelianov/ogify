package net.ogify.fragments.helpers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.ogify.R;

public class LeftNavAdapter extends BaseAdapter {
    private ArrayList<DrawerItem> items;
    private Context context;

    public LeftNavAdapter(Context context, ArrayList<DrawerItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DrawerItem getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.left_nav_item, null);
        TextView lbl = (TextView) convertView;
        lbl.setText(getItem(position).getTitle());
        lbl.setCompoundDrawablesWithIntrinsicBounds(getItem(position)
                .getImage(), 0, 0, 0);
        return lbl;
    }

}
