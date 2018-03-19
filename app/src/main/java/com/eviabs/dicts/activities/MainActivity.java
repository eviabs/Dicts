package com.eviabs.dicts.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.Session;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener {

    private String TAG = MainActivity.class.getSimpleName();

    protected final Context context = this;

    protected Snackbar snackbar = null;

    protected static Session session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // Set session
        if (session == null) {
            session = new Session(context);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu is hidden, uncomment if needed.
//        getMenuInflater().inflate(R.menu.menu_base, menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem mSearchmenuItem = menu.findItem(R.id.menu_toolbarsearch);
        SearchView searchView = (SearchView) mSearchmenuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_title));
        searchView.setOnQueryTextListener(this);

        Log.d(TAG, "onCreateOptionsMenu: mSearchmenuItem->" + mSearchmenuItem.getActionView());
        return true;
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Show as Snackbar with OnClickListener binding
     * Dismisses older Snackbars.
     *
     * @param text           The text to display
     * @param length         the length
     * @param buttonTxt      The text on button
     * @param buttonListener The on OnClickListener
     */
    protected void showSnack(String text, int length, String buttonTxt, View.OnClickListener buttonListener) {
        dismissSnack();
        View parentLayout = findViewById(android.R.id.content);
        snackbar = Snackbar.make(parentLayout, text, length)
                .setAction(buttonTxt, buttonListener)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackbar.show();
    }

    /**
     * Show as Snackbar without OnClickListener binding
     * Dismisses older Snackbars.
     *
     * @param text      The text to display
     * @param length    the length
     * @param buttonTxt The text on button
     */
    protected void showSnack(String text, int length, String buttonTxt) {
        dismissSnack();
        View parentLayout = findViewById(android.R.id.content);
        snackbar = Snackbar.make(parentLayout, text, length)
                .setAction(buttonTxt, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Do nothing
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackbar.show();
    }

    /**
     * Dismiss the active Snackbar
     */
    protected void dismissSnack() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }


    /**
     * Sets the layout Width/Height of the "activity container"
     * Default is WRAP_CONTENT
     *
     * @return
     */
    protected int getLayoutWidthHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Shows a long Toast
     *
     * @param msg the Toast's message
     */
    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Get a list of the required permissions needed by the app (as in the manifest)
     *
     * @return an array of permissions
     */
    protected String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }

    /**
     * Get a list of the required permissions that are still needed by the app (as in the manifest).
     * Those permissions where not given or were taken away during runtime.
     *
     * @return an array of permissions
     */
    @NonNull
    protected String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext(); ) {
            String permission = i.next();

            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(MainActivity.class.getSimpleName(), "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                Log.d(MainActivity.class.getSimpleName(), "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    /**
     * The default share functionality
     */
    protected void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_app_body));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
    }

    /**
     * The default send functionality
     */
    protected void send() {
        share();
    }

    /**
     * The default refresh functionality
     */
    protected void refresh() {
        // Do nothing
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            this.showToast("home");

        } else if (id == R.id.nav_logout) {
            this.showToast("logout");

        } else if (id == R.id.nav_manage) {
            this.showToast("manage");

        } else if (id == R.id.nav_share) {
            share();

        } else if (id == R.id.nav_send) {
            send();

        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: query->"+query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange: newText->" + newText);
        showToast(newText);

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}