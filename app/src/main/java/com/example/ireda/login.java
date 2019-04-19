package com.example.ireda;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class login extends AppCompatActivity {
    private static final String TAG = null;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Button _login, _sign;
    EditText _log_email, _log_password;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        openHelper = new DatabaseHelper(this);
        db=openHelper.getReadableDatabase();

        _login = (Button) findViewById(R.id.login);
        _log_email = (EditText) findViewById(R.id.log_email);
        _log_password = (EditText) findViewById(R.id.log_password);


        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {

                    String Lemail = _log_email.getText().toString();
                    String Lpass = _log_password.getText().toString();
                    cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_3 + "=? AND " + DatabaseHelper.COL_4 + "=?", new String[]{Lemail, Lpass});
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            cursor.moveToNext();
                            Toast.makeText(getApplicationContext(), "Logged in Successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Logged in Failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            public boolean validate(){
                if(_log_email.getText().toString().trim().length() <=0){
                    Toast.makeText(login.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return true;
                }else if (_log_password.getText().toString().trim().length() <=0){
                    Toast.makeText(login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign:
                        signIn();
                        break;
                    // ...
                }
            }
        });

    }


    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }


    private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }



    private void updateUI(GoogleSignInAccount account) {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Toast.makeText(this, "Name of the user :" + personName+ "user id is :" + personId, Toast.LENGTH_SHORT).show();
            Intent inte = new Intent(login.this, MapsActivity.class);
            startActivity(inte);
        }

    }


}
