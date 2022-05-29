package com.example.recetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.regex.Pattern;

public class WelcomeActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    TextView forgotPassTextView;
    Button loginButton;

    ProgressDialog dialog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Instancia de dialogo de progreso
        dialog = new ProgressDialog(this);

        // Instancia de los inputs y elementos
        emailText = findViewById(R.id.email_login_input);
        passwordText = findViewById(R.id.password_login_input);
        forgotPassTextView = findViewById(R.id.forgot_pass_link);
        loginButton = findViewById(R.id.button_action_Login);

        // Instanciar Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    public void onButtonRegisterClicked(View view){
        Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onButtonForgotPasswordClicked(View view){
        Intent intent = new Intent(WelcomeActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void onButtonSignInClicked(View view){
        String email_value = emailText.getText().toString().trim();
        String password_value = passwordText.getText().toString().trim();

        // Validacion de la informacion que ingresa el usuario
        if (!Patterns.EMAIL_ADDRESS.matcher(email_value).matches()){
            emailText.setError("Formato inválido de email");
            emailText.requestFocus();
        }
        if (password_value.length() < 6){
            passwordText.setError("La contraseña debe tener al menos 6 caracteres");
            passwordText.requestFocus();
        }

        dialog.show();

        mAuth.signInWithEmailAndPassword(email_value, password_value).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(WelcomeActivity.this,"Usuario verificado",Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
                else {
                    dialog.hide();
                    Toast.makeText(WelcomeActivity.this,"Nombre de usuario o contraseña incorrectos",Toast.LENGTH_LONG).show();
                    emailText.requestFocus();
                }
            }
        });
    }

}