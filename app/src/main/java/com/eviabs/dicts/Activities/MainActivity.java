package com.eviabs.dicts.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.ApiClients.AutocompleteClient;
import com.eviabs.dicts.Fragments.SearchResultsFragment;
import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.Session;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private String TAG = MainActivity.class.getSimpleName();

    private enum FragmentType {
        Search,
        Settings
    };
    private FragmentType currentShownFragment = FragmentType.Search;
    final protected SearchResultsFragment searchResultsFragment = new SearchResultsFragment();

    private MaterialSearchView searchView = null;

    private final Context context = this;

    private Snackbar snackbar = null;

    private static Session session = null;

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


        searchView = (MaterialSearchView) findViewById(R.id.search_view);


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
        //TODO: FIX backpress when only 1 item in backstack
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        final MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.search_view);
        //TODO: Voice icon isn't showing up, that's a bug in MaterialSearchView. Consider replace it.
        searchView.setVoiceSearch(true);

        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);

                // return true if you don't want dismiss the search bar
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setSuggestions(newText);

                // return true if you don't want dismiss the search bar
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic

            }
        });

        showFragment(FragmentType.Search);
        return true;
    }

    private void setSuggestions(String term) {
        // don't search the web if string is too short
        if (term != null && term.length() > 1) {

            // clear old suggestions
            searchView.setSuggestions(new String[] {});

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(AutocompleteClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            AutocompleteClient client = builder.build().create(AutocompleteClient.class);
            Call<ResponseBody> call = client.getSuggestions(term, AutocompleteClient.CLIENT_JSON, AutocompleteClient.LANGUAGE);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        searchView.setSuggestions(responseToArrayOfSuggestions(response.body().string()));
                    } catch (IOException ex) {
                        // do nothing
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }

                private String[] responseToArrayOfSuggestions(String body) throws IOException {
                    // clean the response and split to array of strings
                    String cleanedBodyStr = body.replaceAll(AutocompleteClient.BAD_CHARS, "");
                    String[] arrSuggestions = cleanedBodyStr.split(AutocompleteClient.DELIMITER);

                    // remove the first (and duplicate) result
                    if (arrSuggestions.length > 0) {
                        arrSuggestions = Arrays.copyOfRange(arrSuggestions, 1, arrSuggestions.length);
                    }

                    // force max num of suggestions
                    if (arrSuggestions.length > ApiConsts.NUM_OF_SUGGESTIONS) {
                        arrSuggestions = Arrays.copyOfRange(arrSuggestions, 0, ApiConsts.NUM_OF_SUGGESTIONS);
                    }

                    // sort and return
                    Arrays.sort(arrSuggestions);

                    return arrSuggestions;
                }
            });
        }
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
    public void showSnack(String text, int length, String buttonTxt, View.OnClickListener buttonListener) {
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
    public void showSnack(String text, int length, String buttonTxt) {
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
    public void dismissSnack() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }


    /**
     * Shows a long Toast
     *
     * @param msg the Toast's message
     */
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Get a list of the required permissions needed by the app (as in the manifest)
     *
     * @return an array of permissions
     */
    public String[] getRequiredPermissions() {
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
    public String[] requiredPermissionsStillNeeded() {

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
    public void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_app_body));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
    }

    /**
     * The default send functionality
     */
    public void send() {
        share();
    }

    /**
     * The default refresh functionality
     */
    public void refresh() {
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

    public void showFragment(FragmentType fragmentType) {
        //TODO: add settings fragment
        Fragment fragment = fragmentType == FragmentType.Search ? searchResultsFragment : searchResultsFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    private void search(String query) {
        // Make sure that the search result fragment is shown
        switch (currentShownFragment){
            case Search:
                break;
            case Settings:
                showFragment(FragmentType.Search);
            default:
                break;
        }

        // Perform the search
        searchResultsFragment.search(query);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}