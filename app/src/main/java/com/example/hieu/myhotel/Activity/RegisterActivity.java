package com.example.hieu.myhotel.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hieu.myhotel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUser;
    EditText edtPass;

    Button btnRegister;
    private Context mContext;
    private Dialog dialog;

    private FirebaseAuth mAuth;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=(Toolbar)findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Find My Hotel");
        toolbar.setTitleTextColor(Color.WHITE);
        mapping();
        mAuth = FirebaseAuth.getInstance();
    }

    private void mapping() {
        edtUser = (EditText) findViewById(R.id.edtUsername);
        edtPass = (EditText) findViewById(R.id.edtPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(Register_Click);
    }

    private View.OnClickListener Register_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String email = edtUser.getText().toString();
                String password = edtPass.getText().toString();
                register(email,password);
            }catch(Exception ex){
                showDialog();
            }
        }
    };

    private void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void showDialog() {
        dialog = new Dialog(RegisterActivity.this);
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
}
