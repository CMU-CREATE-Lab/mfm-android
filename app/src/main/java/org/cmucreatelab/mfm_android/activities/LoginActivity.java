package org.cmucreatelab.mfm_android.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseRefreshableActivity {


    // could not pass by using intent so I am doing this for now.
    public static School currentSchool;

    public boolean isStudentsDone;
    public boolean isGroupsDone;
    private GlobalHandler globalHandler;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String username;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        isGroupsDone = false;
        isStudentsDone = false;

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
            this.refreshStudentsAndGroups();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (currentSchool != null) {
            globalHandler.mfmRequestHandler.login(this, username, password, currentSchool.getId().toString());
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (show) {
            audioPlayer.addAudio(R.raw.please_wait);
            audioPlayer.playAudio();
        }


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
        super.onButtonClick(this.getApplicationContext());
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
            GlobalHandler.getInstance(getApplicationContext()).mfmRequestHandler.requestListSchools(this, username, password);
        }
    }


    public void requestListSchoolsSuccess(ArrayList<School> schools) {
        globalHandler = GlobalHandler.getInstance(getApplicationContext());

        if (schools.size() == 1) {
            showProgress(true);
            globalHandler.mfmRequestHandler.login(this, username, password, schools.get(0).getId().toString());
        } else {
            /*showProgress(false);
            Intent intent = new Intent(this, SchoolActivity.class);
            intent.putExtra(Constants.SCHOOL_KEY, schools);
            startActivity(intent);*/

            showProgress(true);
            globalHandler.mfmRequestHandler.login(this, username, password, schools.get(0).getId().toString());
        }
    }

}
