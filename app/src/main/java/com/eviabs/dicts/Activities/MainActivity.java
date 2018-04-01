package com.eviabs.dicts.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
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
import android.widget.ScrollView;
import android.widget.Toast;

import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.ApiClients.AutocompleteClient;
import com.eviabs.dicts.Fragments.SearchResultsFragment;
import com.eviabs.dicts.Fragments.SettingsFragment;
import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.LocalPreferences;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName() + "@asw!";

    private final String FRAGMENTS_SET_UP = "fragments_set_up";

    private final SearchResultsFragment searchResultsFragment = new SearchResultsFragment();

    private final SettingsFragment settingsFragment = new SettingsFragment();

    private enum FragmentType {
        Search,
        Settings
    };

    private boolean fragmentsSetUp = false;

    private FragmentType currentShownFragment = FragmentType.Search;

    private MaterialSearchView searchView = null;

    private final Context context = this;

    private Snackbar snackbar = null;

    private LocalPreferences localPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "enters onCreate");
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


        // Set localPreferences
        if (localPreferences == null) {
            localPreferences = new LocalPreferences(context);
        }

        setupFragments();
        showFragment(FragmentType.Search);

//        Log.d(TAG, "exits onCreate");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // first close drawer, then close search bar, then go back to search fragment, and lastly
        // go use default behavior

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentShownFragment == FragmentType.Settings) {
            showFragment(FragmentType.Search);
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
                    String cleanedBodyStr = StringEscapeUtils.unescapeJava(body.replaceAll(AutocompleteClient.BAD_CHARS, ""));
                    String[] arrSuggestions = cleanedBodyStr.split(AutocompleteClient.DELIMITER);

                    // remove the first (and duplicate) result
                    if (arrSuggestions.length > 0) {
                        arrSuggestions = Arrays.copyOfRange(arrSuggestions, 1, arrSuggestions.length);
                    }

                    // force max num of suggestions
                    if (arrSuggestions.length > getLocalPreferences().getNumOfSuggestions()) {
                        arrSuggestions = Arrays.copyOfRange(arrSuggestions, 0, getLocalPreferences().getNumOfSuggestions());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            showFragment(FragmentType.Search);

        } else if (id == R.id.nav_manage) {
            showFragment(FragmentType.Settings);

        } else if (id == R.id.nav_share) {
            share();

        } else if (id == R.id.nav_send) {
            send();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Displays the desired fragment, and hides the other one.
     *
     * @param fragmentType the fragment to show
     */
    public void showFragment(FragmentType fragmentType) {
        currentShownFragment = fragmentType;
        Fragment fragmentToShow = fragmentType == FragmentType.Search ? searchResultsFragment : settingsFragment;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragmentToShow);

        fragmentTransaction.commit();
    }

    /**
     * Adds the 2 fragments to the fragments list.
     * If the fragments are already added, we remove them and add them again.
     *
     */
        public void setupFragments() {
//        Log.d(TAG, "enters setupFragments");
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Log.d(TAG, "fragments: " + fragmentManager.getFragments().size());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container, searchResultsFragment);
        fragmentTransaction.commit();
    }

    /**
     * Perform a search operation.
     * Called from the searching fragment.
     * The searching fragment will be shown, and any other fragment will be hidden.
     *
     * @param query the search term
     */
    private void search(String query) {
        // Notify user if no internet connection is available
        notifyIfOffline();

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

    /**
     * Crated a nice Snakbar the notifies the user that no internet connection is available.
     */
    public void notifyIfOffline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(cm !=null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()))
        {
            showSnack(getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE, getResources().getString(R.string.dismiss));
        }
    }

    /**
     * Retrieve the LocalPreferences object.
     * @return the LocalPreferences object.
     */
    public LocalPreferences getLocalPreferences() {
        return localPreferences;
    }
}