package com.example.veggieportz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class sign_in extends AppCompatActivity {


    private ProgressBar ProBar;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private EditText Email,Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);





        mAuth = FirebaseAuth.getInstance();
        ProBar = findViewById(R.id.progressBar);
        Email = findViewById(R.id.email);
        Pass = findViewById(R.id.pass);
        Button SIB = findViewById(R.id.SignIn);
        Button Dsu = findViewById(R.id.Dsup);

        findViewById(R.id.GsIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GSignIn();
            }
        });
        Dsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(sign_in.this,sign_up.class);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        });

        SIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String pass = Pass.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(sign_in.this, MainActivity.class);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                                finish();
                            } else {
                                Toast.makeText(sign_in.this, "Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else
                    Toast.makeText(sign_in.this,"Please Fill Credentials",Toast.LENGTH_SHORT).show();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void GSignIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(sign_in.this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END onactivityresult]

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
        ProBar.setVisibility(View.VISIBLE);
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ProBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent home  = new Intent(sign_in.this,MainActivity.class);
                            startActivity(home);
                            overridePendingTransition(0,0);
                            finish();
                            Toast.makeText(sign_in.this, "SignIn Success", Toast.LENGTH_SHORT).show();
                        } else {


                            Toast.makeText(sign_in.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END auth_with_google]

}