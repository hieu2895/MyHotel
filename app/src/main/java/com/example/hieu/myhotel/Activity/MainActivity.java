package com.example.hieu.myhotel.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hieu.myhotel.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText edtUser;
    private EditText edtPass;

    private String TAG = "MainActivity";

    private Button btnLogin;
    private Button btnRegister;
    private Dialog dialog;
    private Button btnSkip;
    private LoginButton btn_LoginFB;

    //private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        toolbar.setTitle("Find My Hotel");
        toolbar.setTitleTextColor(Color.WHITE);
        mapping();
        getUserAuth();
        loginWithFB();
    }
    private void getUserAuth(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    String id= user.getUid();
                    Uri uri = user.getPhotoUrl();

                    Log.d(TAG,"onAuthStateChanged: signed_in: " +id+"     "+user);


                    Bundle bundle = new Bundle();
                    bundle.putString("id",id);
                    bundle.putString("name", name);
                    bundle.putString("email", email);
                    bundle.putString("uri", String.valueOf(uri));


                    intent.putExtra("Packet", bundle);
                    startActivity(intent);

                }else{
                    Log.d(TAG,"onAuthStateChanged: signed_out" );
                }
            }
        };
    }

    private void loginWithFB(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        btn_LoginFB.setReadPermissions("email", "public_profile");
        btn_LoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
        getUserAuth();
    }

    private void mapping() {
        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPass = (EditText) findViewById(R.id.edtPass);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegisterMain);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        btn_LoginFB = (LoginButton) findViewById(R.id.login_buttonFB);

        btnLogin.setOnClickListener(Login_Click);
        btnRegister.setOnClickListener(Register_Click);
        btnSkip.setOnClickListener(Skip_Click);
    }
    public View.OnClickListener Login_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String email = edtUser.getText().toString();
                String password = edtPass.getText().toString();
                login(email,password);
            }catch(Exception ex){
                showDialog();
            }
        }


    };

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(MainActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
        getUserAuth();
    }

    private void showDialog() {
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Messeger");
        dialog.setContentView(R.layout.dialogerror);

        Button btnDialog = (Button) dialog.findViewById(R.id.btnDialog);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public View.OnClickListener Register_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent register = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(register);
        }
    };

    public View.OnClickListener Skip_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent skip = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(skip);
        }
    };

    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Login is Successful ",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        getUserAuth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
