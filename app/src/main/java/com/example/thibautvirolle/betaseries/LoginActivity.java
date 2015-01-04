package com.example.thibautvirolle.betaseries;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thibautvirolle.betaseries.episodes.Episode;
import com.example.thibautvirolle.betaseries.utilitaires.Config;
import com.example.thibautvirolle.betaseries.utilitaires.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    private static String TAG = LoginActivity.class.getSimpleName();

    final private static int NO_USER_FOUND = 4002;
    final private static int INVALID_PASSWORD = 4003;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        /*if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }*/

        /*// Check for a valid email address.
        // Useless car login !!!
        if (TextUtils.isEmpty(email)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mLoginView.setError(getString(R.string.error_invalid_email));
            focusView = mLoginView;
            cancel = true;
        }*/

        /*if(TextUtils.isEmpty(email)){
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(email)){
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }*/


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            showProgress(false);
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            mAuthTask = new UserLoginTask("thibaut.virolle@gmail.com", "23tivi03");
            mAuthTask.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isLoginValid(String login) {
        return login.length() > 1;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mLoginView.setAdapter(adapter);
    }

    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(),0,s.length());
            String hash = new BigInteger(1, digest.digest()).toString(16);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;


        String userId = null;
        String token = null;
        ArrayList<Episode> planningList;
        int error = -1;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = md5(password);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            userId = null;
            token = null;

            HttpPost httppost = new HttpPost("https://api.betaseries.com/members/auth");
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            //On crée la liste qui contiendra tous nos paramètres
            //Et on y rajoute nos paramètres
            postParameters.add(new BasicNameValuePair("login", mEmail));
            postParameters.add(new BasicNameValuePair("password", mPassword));
            postParameters.add(new BasicNameValuePair("key", Config.API_KEY));
            String login = null;
            String s;

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(entity);
                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response=httpclient.execute(httppost);
                InputStream is = response.getEntity().getContent();


                JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

                try {
                    reader.beginObject();
                    while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
                        String value = reader.nextName();
                        if (value.equals("user")) {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                value = reader.nextName();
                                if (value.equals("id")) {

                                    userId = String.valueOf(reader.nextInt());

                                } else if (value.equals("login")) {
                                    login = reader.nextString();
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        }
                        else if (value.equals("token")) {
                            token = reader.nextString();
                        }
                        else if (value.equals("errors")) {
                            reader.beginArray();
                            while(reader.hasNext())
                            {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    value = reader.nextName();
                                    if (value.equals("code")) {
                                        error = reader.nextInt();
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                            }

                            reader.endArray();
                        } else
                            reader.skipValue();

                    }

                    reader.endObject();
                } finally {
                    reader.close();
                }

                is.close();



            } catch (Exception e) {
                e.printStackTrace();
            }









            HttpGet httpget = new HttpGet("https://api.betaseries.com/planning/member?id="+userId+"&key="+ Config.API_KEY);
            Log.d(TAG,"3.1");

            try {
                HttpClient httpclient = new DefaultHttpClient();
                Log.d(TAG,"3.2");

                HttpResponse response=httpclient.execute(httpget);
                Log.d(TAG,"3.3");
                InputStream is = response.getEntity().getContent();

                Log.d(TAG,"4");

                planningList = JsonParser.readShowEpisodesJsonStream(is);

                Log.d(TAG,"5");

                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return (userId != null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(true);

            if (success) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Config.USER_ID, userId);
                returnIntent.putExtra(Config.TOKEN, token);
                returnIntent.putParcelableArrayListExtra(Config.PLANNING_LIST,planningList);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                showProgress(false);
                switch(error)
                {
                    case NO_USER_FOUND:
                        mLoginView.setError(getString(R.string.error_no_user_found));
                        mLoginView.requestFocus();
                        break;

                    case INVALID_PASSWORD:
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                        break;

                    default:
                        break;

                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private InputStream downloadData(String requestUrl) throws IOException, DownloadException {

            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
            URL url = new URL(requestUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
            urlConnection.setRequestMethod("GET");

            int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                return inputStream;
            } else {
                Log.d(TAG,String.valueOf(statusCode));
                throw new DownloadException("Failed to fetch data!!");
            }
        }


        public class DownloadException extends Exception {

            public DownloadException(String message) {
                super(message);
            }

            public DownloadException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    }
}



