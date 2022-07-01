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
import com.example.recetapp.MainActivity;
import com.example.recetapp.R;
import com.example.recetapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    // Inputs
    EditText nameInput;
    EditText emailInput;
    EditText passInput;
    Button signUpActionButton;

    // Dialog
    LoadingDialog dialog;

    // FireBase
    private FirebaseAuth mAuth;

    public SignUpFragment() {}


    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
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
        View v =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Instancia de dialogo de progreso
        dialog = new LoadingDialog(getContext());

        //   Instancia de los inputs
        nameInput = v.findViewById(R.id.name_register_input);
        emailInput = v.findViewById(R.id.email_register_input);
        passInput = v.findViewById(R.id.password_register_input);
        signUpActionButton = v.findViewById(R.id.button_action_register);

        mAuth = FirebaseAuth.getInstance();

        //Listener de click en boton registro
        signUpActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser(view);
            }
        });

        return v;
    }

    public void signUpUser(View v){
        String valueNameInput = nameInput.getText().toString().trim();
        String valueEmailInput = emailInput.getText().toString().trim();
        String valuePassInput = passInput.getText().toString().trim();

        if (validInputs(valueNameInput, valueEmailInput, valuePassInput)) {

            dialog.ShowDialog("Creando usuario...");

            mAuth.createUserWithEmailAndPassword(valueEmailInput, valuePassInput)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Instacia de modelo User
                            User user = new User(valueNameInput, valueEmailInput, valuePassInput);

                            // Obtenemos la instancia del firebase para pasar los datos que queremos guardar
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.HideDialog();
                                        Toast.makeText(getContext(), "Registrado correctamente", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        dialog.HideDialog();
                                        Toast.makeText(getContext(), "Ha sucedido un error", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        } else {
                            dialog.HideDialog();
                            Toast.makeText(getContext(), "Ha sucedido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private boolean validInputs(String name_input, String email_input, String password_input){
        // Validacion de la informacion que ingresa el usuario
        if (name_input.isEmpty()){
            nameInput.setError("Por favor ingresar un Nombre");
            nameInput.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_input).matches() || email_input.isEmpty()){
            emailInput.setError("Por favor ingresar un Email");
            emailInput.requestFocus();
            return false;
        }
        if (password_input.isEmpty() || password_input.length() < 6){
            passInput.setError("Por favor ingresar un Password de al menos 6 caracteres");
            passInput.requestFocus();
            return false;
        }
        return true;
    }
}