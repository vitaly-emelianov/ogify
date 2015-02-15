package net.ogify;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.ogify.custom.CustomActivity;
import net.ogify.fragments.helpers.enums.OrderNamespace;


public class OrderInfoActivity extends CustomActivity {
    OrderNamespace namespace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("namespace");
            namespace = OrderNamespace.values()[value];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!OrderNamespace.MY.equals(namespace) && !OrderNamespace.ASSIGNED_TO_ME.equals(namespace)) {
            getMenuInflater().inflate(R.menu.menu_assign_to_me, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
