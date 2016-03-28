package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends ListActivity {

    // TODO implement LoginActivity - parsing JSON, reading/writing to database
    private GlobalHandler globalHandler;
    private EditText username;
    private EditText password;

    // to handle cases when there is no network available
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo  = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        this.globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        this.username = (EditText) findViewById(R.id.username);
        this.password = (EditText) findViewById(R.id.password);
    }

    @OnClick(R.id.loginButton)
    public void onLogin(){
        if (isNetworkAvailable()) {
            // hard code 17 in for debugging purposes
            if (!this.username.toString().equals("") && !this.password.toString().equals("")){
                globalHandler.mfmRequestHandler.login(this.username.toString(), this.password.toString(), "17");
                globalHandler.refreshStudentsAndGroups();

                Intent intent = new Intent(this, MainScreenActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Please enter a valid username and password", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Network Is Unavailable!", Toast.LENGTH_LONG).show();
        }
    }

}
