package com.example.recetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailForgotInput;
    Button recoverForgotButton;

    // Firebase
    FirebaseAuth mAuth;

    // Dialog
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        // Instancia de dialogo de progreso
        dialog = new ProgressDialog(this);

        // Instancia de los inputs y elementos
        emailForgotInput = findViewById(R.id.email_forgot_input);
        recoverForgotButton = findViewById(R.id.button_action_forgotPassword);

        // Instanciar Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    public void onButtonRecoverPassword(View view){
        String email_value = emailForgotInput.getText().toString().trim();

        // Validacion de la informacion que ingresa el usuario
        if (!Patterns.EMAIL_ADDRESS.matcher(email_value).matches()){
            emailForgotInput.setError("Formato inválido de email");
            emailForgotInput.requestFocus();
        }

        dialog.show();

        mAuth.sendPasswordResetEmail(email_value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.hide();
                    Toast.makeText(ForgotPasswordActivity.this, "En unos momentos recibirá un email en la casilla de correo", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
                else{
                    dialog.hide();
                    Toast.makeText(ForgotPasswordActivity.this, "Hubo un error. Por favor revisar los datos ingresados e intente nuevamente", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}