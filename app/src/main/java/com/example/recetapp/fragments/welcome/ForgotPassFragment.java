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
import android.widget.Toast;

import com.example.recetapp.Listeners.LoadingDialog;
import com.example.recetapp.R;
import com.example.recetapp.welcome.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPassFragment extends Fragment {

    EditText emailForgotInput;
    Button recoverForgotButton;

    // Firebase
    FirebaseAuth mAuth;

    // Dialog
    LoadingDialog dialog;

    public ForgotPassFragment() {}


    public static ForgotPassFragment newInstance() {
        ForgotPassFragment fragment = new ForgotPassFragment();
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
        View v = inflater.inflate(R.layout.fragment_forgot_pass, container, false);

        // Instancia de dialogo de progreso
        dialog = new LoadingDialog(getContext());


        // Instancia de los inputs y elementos
        emailForgotInput = v.findViewById(R.id.email_forgot_input);
        recoverForgotButton = v.findViewById(R.id.button_action_forgotPassword);

        // Instanciar Firebase
        mAuth = FirebaseAuth.getInstance();

        recoverForgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword(view);
            }
        });

        return v;
    }

    public void recoverPassword(View view){
        String email_value = emailForgotInput.getText().toString().trim();

        // Validacion de la informacion que ingresa el usuario
        if (!Patterns.EMAIL_ADDRESS.matcher(email_value).matches()){
            emailForgotInput.setError("Formato inválido de email");
            emailForgotInput.requestFocus();
        }

        dialog.ShowDialog("Cargando detalles...");

        mAuth.sendPasswordResetEmail(email_value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.HideDialog();
                    Toast.makeText(getContext(), "En unos momentos recibirá un email en la casilla de correo", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    dialog.HideDialog();
                    Toast.makeText(getContext(), "Hubo un error. Por favor revisar los datos ingresados e intente nuevamente", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}