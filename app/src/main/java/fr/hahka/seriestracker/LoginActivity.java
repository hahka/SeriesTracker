package fr.hahka.seriestracker;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.UserInterface;

import static fr.hahka.seriestracker.utilitaires.Miscellaneous.md5;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    final private static int NO_USER_FOUND = 4002;
    final private static int INVALID_PASSWORD = 4003;
    private static String TAG = LoginActivity.class.getSimpleName();
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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
     * Tentative de connexion à BetaSeries
     * Affiche les erreurs quand elle arrivent
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reinitialise les erreurs
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Récupère les valeurs entrées pour la connexion
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

            UserInterface.showProgress(false, mLoginFormView, mProgressView);
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            UserInterface.showProgress(true, mLoginFormView, mProgressView);
            mAuthTask = new UserLoginTask(login, password);


            mAuthTask.execute((Void) null);

        }
    }

    private boolean isLoginValid(String login) {
        return login.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mLoginView.setAdapter(adapter);
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
        int error = -1;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = md5(password);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            userId = null;
            token = null;

            //Log.d(TAG,"début connection");

            HttpPost httppost = new HttpPost("https://api.betaseries.com/members/auth");
            List<NameValuePair> postParameters = new ArrayList<>();
            //On crée la liste qui contiendra tous nos paramètres
            //Et on y rajoute nos paramètres
            postParameters.add(new BasicNameValuePair("login", mEmail));
            postParameters.add(new BasicNameValuePair("password", mPassword));
            postParameters.add(new BasicNameValuePair("key", Config.API_KEY));


            // Parsing json
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(entity);
                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = httpclient.execute(httppost);
                InputStream is = response.getEntity().getContent();


                JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

                reader.beginObject();
                while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
                    String value = reader.nextName();
                    switch (value) {
                        case "user":
                            reader.beginObject();
                            while (reader.hasNext()) {
                                value = reader.nextName();
                                //Log.d(TAG, value);
                                if (value.equals("id")) {

                                    userId = String.valueOf(reader.nextInt());
                                    //Log.d(TAG, String.valueOf(userId));

                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                            break;
                        case "token":
                            token = reader.nextString();
                            //Log.d(TAG, token);
                            break;
                        case "errors":
                            reader.beginArray();
                            while (reader.hasNext()) {
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
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }

                }

                reader.endObject();
                reader.close();

                is.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


            return (userId != null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                // Connexion réussie. Renvoie les objets user et planningList à DrawerActivity

                Intent returnIntent = new Intent();
                returnIntent.putExtra(Config.USER_ID, userId);
                returnIntent.putExtra(Config.TOKEN, token);
                //returnIntent.putExtra(Config.USER, user);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {

                UserInterface.showProgress(false, mLoginFormView, mProgressView);
                switch (error) {
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
            UserInterface.showProgress(false, mLoginFormView, mProgressView);
        }

    }
}



