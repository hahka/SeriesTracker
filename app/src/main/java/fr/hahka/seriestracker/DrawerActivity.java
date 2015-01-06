package fr.hahka.seriestracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

import fr.hahka.seriestracker.episodes.Episode;
import fr.hahka.seriestracker.navdrawer.NavDrawerItem;
import fr.hahka.seriestracker.navdrawer.NavDrawerListAdapter;
import fr.hahka.seriestracker.planning.PlanningFragment;
import fr.hahka.seriestracker.shows.ShowsFragment;
import fr.hahka.seriestracker.user.User;
import fr.hahka.seriestracker.user.UserFragment;
import fr.hahka.seriestracker.utilitaires.Config;

/**
 * Created by thibautvirolle on 03/01/15.
 */
public class DrawerActivity extends Activity {

    private static String userId;
    private static String token;
    private static User user;
    private static ArrayList<Episode> planningList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mTitle = mDrawerTitle = "BetaSeries";

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<>();

        // Ajoute les objets du nav drawer
        // Mon Profil
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Mon Planning
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Mes Séries
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            if (userId != null) {
                // on first time display view for first nav item
                displayView(0);
                getActionBar().setIcon(null);
            } else {
                Intent loginIntent = new Intent(DrawerActivity.this, LoginActivity.class);
                startActivityForResult(loginIntent, Config.AUTH_REQUEST_CODE);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // met à jour le contenu en remplaçant le fragment
        Fragment fragment = null;
        Bundle bundle;
        switch (position) {
            case 0:
                fragment = new UserFragment();
                bundle = new Bundle();
                bundle.putParcelable(Config.USER, user);
                fragment.setArguments(bundle);
                getActionBar().setIcon(R.drawable.ic_profile);
                break;
            case 1:
                fragment = new PlanningFragment();
                bundle = new Bundle();
                bundle.putParcelableArrayList(Config.PLANNING_LIST, planningList);
                fragment.setArguments(bundle);
                getActionBar().setIcon(R.drawable.ic_planning);
                break;
            case 2:
                fragment = new ShowsFragment();
                bundle = new Bundle();
                bundle.putString(Config.TOKEN, token);
                bundle.putParcelable(Config.USER, user);
                fragment.setArguments(bundle);
                getActionBar().setIcon(R.drawable.ic_shows);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // met à jour la vue et le titre, puis ferme le drawer menu
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.AUTH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Récupère les données de l'intent
            userId = data.getStringExtra(Config.USER_ID);
            token = data.getStringExtra(Config.TOKEN);
            user = data.getParcelableExtra(Config.USER);
            planningList = data.getParcelableArrayListExtra(Config.PLANNING_LIST);

            displayView(0);

        } else {
            Toast.makeText(this, "Echec de la connexion !", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(DrawerActivity.this, LoginActivity.class);
            startActivityForResult(loginIntent, Config.AUTH_REQUEST_CODE);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // affiche le nav drawer en cliquant sur l'icon ou le titre
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Slide menu item click listener
     * Affiche le fragment désiré (Mon Profil, Mon Planning, Mes Séries)
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

}
