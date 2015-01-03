package com.example.thibautvirolle.betaseries;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thibautvirolle.betaseries.shows.ShowsActivity;


public class MainActivity extends Activity{


    public static final int AUTH_REQUEST_CODE = 0;

    public static final String USER_ID = "user_id";
    public static final String TOKEN = "token";

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);

        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(myIntent, AUTH_REQUEST_CODE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AUTH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Access data from the completed intent
            String id = data.getStringExtra(USER_ID);
            String token = data.getStringExtra(TOKEN);
            //Toast.makeText(MainActivity.this, id, Toast.LENGTH_LONG).show();

            Intent userShowsIntent = new Intent(this,ShowsActivity.class);
            userShowsIntent.putExtra("user_id",id);
            userShowsIntent.putExtra("token",token);

            startActivity(userShowsIntent);


        }
        else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
