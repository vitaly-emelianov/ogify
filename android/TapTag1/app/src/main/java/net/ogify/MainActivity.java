package net.ogify;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import net.ogify.custom.CustomActivity;
import net.ogify.fragments.helpers.DrawerItem;
import net.ogify.fragments.Tasks;
import net.ogify.fragments.helpers.LeftNavAdapter;
import net.ogify.fragments.MyProfile;
import net.ogify.fragments.CreatedByMe;

public class MainActivity extends CustomActivity {
    private DrawerLayout drawerLayout;
    private ListView drawerLeft;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();
        setupContainer();
    }

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                setActionBarTitle();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.menu1);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawers();

        setupLeftNavDrawer();
    }

    private void setupLeftNavDrawer() {
        drawerLeft = (ListView) findViewById(R.id.left_drawer);

        drawerLeft.setAdapter(new LeftNavAdapter(this, getDummyLeftNavItems()));
        drawerLeft.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                drawerLayout.closeDrawers();
                launchFragment(pos);
            }
        });
    }

    private ArrayList<DrawerItem> getDummyLeftNavItems() {
        ArrayList<DrawerItem> al = new ArrayList<DrawerItem>();
        al.add(new DrawerItem("My Profile", null, R.drawable.ic_nav4));
        al.add(new DrawerItem("Tasks", null, R.drawable.ic_nav1));
        al.add(new DrawerItem("Created By Me", null, R.drawable.ic_nav2));
        return al;
    }

    private void launchFragment(int pos) {
        Fragment f = null;
        String title = null;
        if (pos == 0) {
            title = "My Profile";
            f = new MyProfile();
        } else if (pos == 1) {
            title = "Tasks";
            f = new Tasks();
        } else if (pos == 2) {
            title = "Created By Me";
            f = new CreatedByMe();
        }

        if (f != null) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, f).addToBackStack(title)
                    .commit();
        }
    }

    private void setupContainer() {
        getSupportFragmentManager().addOnBackStackChangedListener(
                new OnBackStackChangedListener() {

                    @Override
                    public void onBackStackChanged() {
                        setActionBarTitle();
                    }
                });
        launchFragment(0);
    }

    private void setActionBarTitle() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            return;
        String title = getSupportFragmentManager().getBackStackEntryAt(
                getSupportFragmentManager().getBackStackEntryCount() - 1)
                .getName();
        getActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            } else
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
