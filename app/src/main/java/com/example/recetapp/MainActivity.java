package com.example.recetapp;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.recetapp.Adapters.RandomRecipeAdapter;
import com.example.recetapp.Listeners.RandomRecipeResponseListener;
import com.example.recetapp.Listeners.RecipeDetailsClickListener;
import com.example.recetapp.databinding.ActivityMainBinding;
import com.example.recetapp.models.RandomApiRes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerBaseActivity {

    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    List<String> tags = new ArrayList<>();


    //Binding
    ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
//        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);

        dialog.show();
        manager = new RequestManager(MainActivity.this);
        manager.getRandomRecipes(randomRecipeResponseListener, tags);
    }

    public final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomApiRes response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_randomRecipe);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
            recyclerView.setNestedScrollingEnabled(false);

            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes, recipeDetailsClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeDetailsClickListener recipeDetailsClickListener = new RecipeDetailsClickListener() {
        @Override
        public void onRecipeClick(String id) {
            startActivity(new Intent(MainActivity.this, RecipeDetailsActivity.class).putExtra("id", id));
        }
    };
}