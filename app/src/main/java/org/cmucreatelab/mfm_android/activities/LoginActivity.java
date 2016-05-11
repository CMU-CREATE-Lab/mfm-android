package org.cmucreatelab.mfm_android.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.annotation.TargetApi;
import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private GlobalHandler globalHandler;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String username;
    private String password;


    // If the user successfully belonged to a school, then this will be called.
    public void requestListSchoolsSuccess(ArrayList<School> schools) {
        // TODO do we just select the first school in the list?
        globalHandler = GlobalHandler.getInstance(getApplicationContext());
        globalHandler.mfmRequestHandler.login(this, username, password, schools.get(0).getId().toString());
    }


    // If the entire loging process was successful then this should be called.
    public void loginSuccess() {
        GlobalHandler.getInstance(getApplicationContext()).refreshStudentsAndGroups();
        Intent intent = new Intent(this, ViewStudentsAndGroupsActivity.class);
        startActivity(intent);
        showProgress(false);
        finish();
    }


    // If at any point the login process failed, call this method.
    public void loginFailure() {
        // TODO Have more possible failures instead of just username or password being invalid.
        showProgress(false);
        Toast.makeText(this.getApplicationContext(), R.string.error_invalid_credentials, Toast.LENGTH_LONG).show();
    }


    // Activity methods and listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // skip the login process if the kiosk is still logged in.
        this.globalHandler = GlobalHandler.getInstance(getApplicationContext());
        if (this.globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            showProgress(true);
            School school  = this.globalHandler.mfmLoginHandler.getSchool();
            String id = this.globalHandler.mfmLoginHandler.getKioskUid();
            this.globalHandler.mfmLoginHandler.login(school, id);
            this.loginSuccess();
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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


    @OnClick(R.id.sign_in_button)
    public void attemptLogin() {
        Log.i(Constants.LOG_TAG, "Attempting to login.");

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        username = mUsernameView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Shows a progress bar and starts the background task of logging in.
            showProgress(true);
            GlobalHandler.getInstance(getApplicationContext()).mfmRequestHandler.requestListSchools(this, username, password);
        }
    }

}

