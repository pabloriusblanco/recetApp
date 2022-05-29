package com.example.recetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recetapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText nameInput;
    EditText emailInput;
    EditText passInput;
    Button signUpActionButton;

    ProgressDialog dialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //        Instancia de dialogo de progreso
        dialog = new ProgressDialog(this);

        //        Instancia de los inputs
        nameInput = (EditText)findViewById(R.id.name_register_input);
        emailInput = (EditText)findViewById(R.id.email_register_input);
        passInput = (EditText)findViewById(R.id.password_register_input);
        signUpActionButton = (Button)findViewById(R.id.button_action_register);

        mAuth = FirebaseAuth.getInstance();

    }

    public void onButtonLoginClicked(View view){
        Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void signUpButtonClicked(View v){

        String valueNameInput = nameInput.getText().toString().trim();
        String valueEmailInput = emailInput.getText().toString().trim();
        String valuePassInput = passInput.getText().toString().trim();

        if (valueNameInput.isEmpty()){
            nameInput.setError("Por favor ingresar un Nombre");
            nameInput.requestFocus();
        }

        if (valueEmailInput.isEmpty()){
            emailInput.setError("Por favor ingresar un Email");
            emailInput.requestFocus();
        }
        if (valuePassInput.isEmpty() || valuePassInput.length() < 6){
            passInput.setError("Por favor ingresar un Password de al menos 6 caracteres");
            passInput.requestFocus();
        }

        dialog.show();

        mAuth.createUserWithEmailAndPassword(valueEmailInput, valuePassInput)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            // Instacia de modelo User
                            User user = new User(valueNameInput, valueEmailInput, valuePassInput);

                            // Obtenemos la instancia del firebase para pasar los datos que queremos guardar
                            FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.hide();
                                        Toast.makeText(SignUpActivity.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        dialog.hide();
                                        Toast.makeText(SignUpActivity.this, "Ha sucedido un error", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else {
                            dialog.hide();
                            Toast.makeText(SignUpActivity.this, "Ha sucedido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}