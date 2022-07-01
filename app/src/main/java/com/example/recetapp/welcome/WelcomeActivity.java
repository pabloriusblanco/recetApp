package com.example.recetapp.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.example.recetapp.R;
import com.example.recetapp.fragments.welcome.ForgotPassFragment;
import com.example.recetapp.fragments.welcome.SignInFragment;
import com.example.recetapp.fragments.welcome.SignUpFragment;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    // Botones
    MaterialButton signInButton;
    MaterialButton signUpButton;

    // Fragments (login, registro y contraseña olvidada)

    Fragment fragmentSignIn;
    Fragment fragmentSignUp;
    Fragment fragmentForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Instancia de botones
        signInButton = findViewById(R.id.signIn_button);
        signUpButton = findViewById(R.id.signUp_button);

        fragmentSignIn = SignInFragment.newInstance();
        fragmentSignUp = SignUpFragment.newInstance();
        fragmentForgotPass = ForgotPassFragment.newInstance();

        Fragment fragment = fragmentSignIn;
        changeFragment(fragment);
    }

    public void changeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .replace(R.id.welcome_content, fragment)
                .commit();
    }

    public void onButtonRegisterClicked(View view){
        // Si el fragmento no es visible
        if (!fragmentSignUp.isVisible()) {
            styleButton(signInButton, "empty");
            styleButton(signUpButton, "full");
            changeFragment(fragmentSignUp);
        }
    }

    public void onButtonLoginClicked(View view){
        // Si el fragmento no es visible
        if (!fragmentSignIn.isVisible()) {
            styleButton(signInButton, "full");
            styleButton(signUpButton, "empty");
            changeFragment(fragmentSignIn);
        }
    }

    public void onButtonForgotPasswordClicked(View view){
        // Si el fragmento no es visible
        if (!fragmentForgotPass.isVisible()) {
            styleButton(signInButton, "empty");
            styleButton(signUpButton, "empty");
            changeFragment(fragmentForgotPass);
        }
    }

    private void styleButton(MaterialButton button, String style) {
        final Typeface poppins_regular = ResourcesCompat.getFont(this, R.font.poppins_regular);
        final Typeface poppins_semibold = ResourcesCompat.getFont(this, R.font.poppins_semibold);

        if (style == "empty") {
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F4F5FA")));
            button.setTypeface(poppins_regular);
            button.setTextColor(Color.parseColor("#12121F"));
        }
        if (style == "full") {
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFBD30")));
            button.setTypeface(poppins_semibold);
            button.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }



}