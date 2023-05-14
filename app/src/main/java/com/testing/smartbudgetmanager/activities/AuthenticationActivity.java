package com.testing.smartbudgetmanager.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.testing.smartbudgetmanager.MainActivity;
import com.testing.smartbudgetmanager.R;
import com.testing.smartbudgetmanager.models.Users;

public class AuthenticationActivity extends AppCompatActivity {
    private Button signInBtn;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initialization();
        clickListeners();
    }

    private void initialization(){
        signInBtn = findViewById(R.id.signInBtn);
        auth = FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(AuthenticationActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.your_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void clickListeners(){

    }
    int RC_SIGN_IN = 40;
    private void signIn(){
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuth(account.getIdToken());
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }

            }catch (Exception e){

            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential creditional = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(creditional)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(AuthenticationActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}