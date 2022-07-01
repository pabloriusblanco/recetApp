package com.example.recetapp.fragments.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recetapp.Listeners.LoadingDialog;
import com.example.recetapp.MainActivity;
import com.example.recetapp.R;
import com.example.recetapp.welcome.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {

    EditText emailText, passwordText;
    TextView forgotPassTextView;
    Button loginButton;

    LoadingDialog dialog;

    FirebaseAuth mAuth;


    public SignInFragment() {}


    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Instancia de dialogo de progreso
        dialog = new LoadingDialog(getContext());

        // Instancia de los inputs y elementos
        emailText = v.findViewById(R.id.email_login_input);
        passwordText = v.findViewById(R.id.password_login_input);
        forgotPassTextView = v.findViewById(R.id.forgot_pass_link);
        loginButton = v.findViewById(R.id.button_action_Login);

        // Instanciar Firebase
        mAuth = FirebaseAuth.getInstance();

        //Listener de click en buttonLogin
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateExistingUser();
            }
        });

        // En el caso de que no se haya cerrado sesion te logea automaticamente
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return v;
    }

    private void validateExistingUser(){
        String email_value = emailText.getText().toString().trim();
        String password_value = passwordText.getText().toString().trim();

        if (validInputs(email_value, password_value)) {
            dialog.ShowDialog("Ingresando...");
            mAuth.signInWithEmailAndPassword(email_value, password_value).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dialog.HideDialog();
                        startActivity(intent);
                    } else {
                        dialog.HideDialog();
                        Toast.makeText(getContext(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        emailText.requestFocus();
                    }
                }
            });
        }
    }

    private boolean validInputs(String email_input, String password_input){
        // Validacion de la informacion que ingresa el usuario
        if (!Patterns.EMAIL_ADDRESS.matcher(email_input).matches()){
            emailText.setError("Formato inválido de email");
            emailText.requestFocus();
            return false;
        }
        if (password_input.length() < 6){
            passwordText.setError("La contraseña debe tener al menos 6 caracteres");
            passwordText.requestFocus();
            return false;
        }
        return true;
    }

}