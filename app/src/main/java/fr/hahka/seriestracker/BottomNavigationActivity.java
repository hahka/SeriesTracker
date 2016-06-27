package fr.hahka.seriestracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import fr.hahka.seriestracker.episodes.planning.PlanningFragment;
import fr.hahka.seriestracker.shows.ShowsFragment;
import fr.hahka.seriestracker.user.UserFragment;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.ScrollableFragmentWithBottomBar;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by thibautvirolle on 23/06/2016.
 */
public class BottomNavigationActivity extends AppCompatActivity {

    private static final String TAG = BottomNavigationActivity.class.getSimpleName();

    private static String userId = "";
    private static String token = "";
    private BottomNavigationBar bottomNavigationBar;

    private Fragment fragment = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.bottom_navigation_activity);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Mon Profile"))
                .addItem(new BottomNavigationItem(R.drawable.ic_planning, "Mon Planning"))
                .addItem(new BottomNavigationItem(R.drawable.ic_shows, "Mes Séries"))
                .addItem(new BottomNavigationItem(R.drawable.ic_logout, "Déconnexion"))
                .initialise();


        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                bottomNavigationItemAction(position);
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
                //bottomNavigationItemAction(position);
                if(position == 1 || position == 2) {
                    ((ScrollableFragmentWithBottomBar)fragment).smoothScrollToPosition(0);
                }
            }
        });

        if (savedInstanceState == null) {
            if (userId != null && !userId.equals("")) {
                // on first time display view for first nav item
                displayView(0);
            } else {
                Intent loginIntent = new Intent(BottomNavigationActivity.this, LoginActivity.class);
                startActivityForResult(loginIntent, Config.AUTH_REQUEST_CODE);
            }

        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    private void logout() {
        Intent restart = new Intent(BottomNavigationActivity.this,BottomNavigationActivity.class);
        userId = null;

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        //realm.clear(SimpleShow.class);
        realm.commitTransaction();

        startActivity(restart);
        finish();
    }


    private void bottomNavigationItemAction(int position) {

        if(position == 3)
            logout();
        else
            displayView(position);

    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // met à jour le contenu en remplaçant le fragment

        Bundle bundle;
        switch (position) {
            case 0:
                fragment = new UserFragment();
                bundle = new Bundle();
                bundle.putString(Config.TOKEN, token);
                bundle.putString(Config.USER_ID, userId);
                fragment.setArguments(bundle);

                break;
            case 1:
                fragment = new PlanningFragment();
                bundle = new Bundle();
                bundle.putString(Config.TOKEN, token);
                bundle.putString(Config.USER_ID, userId);
                fragment.setArguments(bundle);

                break;
            case 2:
                fragment = new ShowsFragment();
                bundle = new Bundle();
                bundle.putString(Config.TOKEN, token);
                bundle.putString(Config.USER_ID, userId);
                fragment.setArguments(bundle);

                break;

            default:
                break;
        }

        if (fragment != null) {

            FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.setTransition(FragmentTransaction.
                    TRANSIT_FRAGMENT_FADE);


            ft.replace(R.id.frame_container, fragment).commit();


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.AUTH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Récupère les données de l'intent
            userId = data.getStringExtra(Config.USER_ID);
            token = data.getStringExtra(Config.TOKEN);

            displayView(0);

        } else {
            finish();
        }

    }
}
