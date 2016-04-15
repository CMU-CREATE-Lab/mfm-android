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
import org.cmucreatelab.mfm_android.helpers.static_classes.database.DbHelper;

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


    // Class methods
    private void populateStudentAndGroupSuccess() {
        showProgress(false);
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }


    public void populateStudentsSuccess() {
        GlobalHandler.getInstance(getApplicationContext()).refreshGroups(this);
    }


    public void populateGroupsSuccess() {
        this.populateStudentAndGroupSuccess();
    }


    public void requestListSchoolsSuccess(ArrayList<School> schools) {
        // TODO fix so I am not just using the first school
        globalHandler = GlobalHandler.getInstance(getApplicationContext());
        globalHandler.mfmRequestHandler.login(this, username, password, schools.get(0).getId().toString());
    }

    public void loginSuccess() {
        GlobalHandler.getInstance(getApplicationContext()).refreshStudents(this);
    }


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


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            // TODO this is okay for now, but in the future we will want to do a "requestListSchools" then use its first response as our school to log in from
            GlobalHandler.getInstance(getApplicationContext()).mfmRequestHandler.requestListSchools(this, username, password);
        }
    }

}

