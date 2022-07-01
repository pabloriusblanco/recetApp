package com.example.recetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recetapp.fragments.home.MainFragment;
import com.example.recetapp.fragments.home.RecipeSearchFragment;
import com.example.recetapp.welcome.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MaterialButton button_open_drawer;

    //Drawer
    DrawerLayout drawerLayout;
    View headerView;
    NavigationView navigationView;

    //Logo Image
    ImageView logoImage;

    // Search
    SearchView searchView;

    // Informacion de Usuario
    TextView userNameInMenu;
    TextView emailInMenu;

    //Tags para busqueda
    List<String> tags = new ArrayList<>();

    //Firebase
    FirebaseAuth mAuth;
    String userUid;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            userUid = user.getUid();
        }


        // Drawer Instance
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,null,R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //Boton para abrir el drawer
        button_open_drawer = findViewById(R.id.open_drawer_button);
        button_open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                else{
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        // Instancia de NavigationView
        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set header
        headerView = navigationView.getHeaderView(0);

        //Instancia de Menu
        userNameInMenu = headerView.findViewById(R.id.menu_userName_title);
        emailInMenu = headerView.findViewById(R.id.menu_userMail_subtitle);

        setUserNameInMenu();
        emailInMenu.setText(userEmail);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);



       searchView = findViewById(R.id.SearchView_home);
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Cuando se submitea, llama al fragment de buscar recetas random con el parametro que se haya insertado
                return searchByTags(query);
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    tags.clear();
                }
                return false;
            }
        });

    }

    private boolean searchByTags(String query){
        tags.clear();
        tags.add(query);
        Fragment fragment = RecipeSearchFragment.newInstance();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("tags_argument", (ArrayList<String>) tags);

        //Menu Buscar Checked
        MenuItem menuItem = navigationView.getMenu().getItem(1);
        menuItem.setChecked(true);

        //Close keyboard
        hideKeyboard();

        // Set de argumentos en el fragment
        fragment.setArguments(arguments);
        changeFragment(fragment);
        return true;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .replace(R.id.home_content, fragment)
                .commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.nav_item_home:
                fragment = MainFragment.newInstance();
                break;
            case R.id.nav_item_recipes:
                fragment = RecipeSearchFragment.newInstance();
                break;
            case R.id.nav_item_logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                logout();
                return true;
        }

        changeFragment(fragment);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        mAuth.signOut();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setUserNameInMenu(){
        // Hay un error en firebase https://github.com/firebase/FirebaseUI-Android/issues/409
        // que no permite obtener el nombre si el login es exclusivamente por email/password
        // asi que es necesario obtenerlo directamente de la base despues de que se tiene la referencia del uid del usuario

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        // De toda la informacion, si coincide con el nombre lo guarda.
                        String key = ds.getKey();
                        String value = ds.getValue().toString();
                        if (ds.getKey().equals("name")) {
                            userNameInMenu.setText(value);
                        }
                    }
                }
            }
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error","Error: " + error.getMessage());
            }
        });
    }
}