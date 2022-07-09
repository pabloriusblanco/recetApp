package com.example.recetapp.fragments.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recetapp.Adapters.FavoriteRecipeAdapter;
import com.example.recetapp.Listeners.FavoriteRecipeResponseListener;
import com.example.recetapp.Listeners.LoadingDialog;
import com.example.recetapp.Listeners.RandomRecipeResponseListener;
import com.example.recetapp.Listeners.RecipeDetailsClickListener;
import com.example.recetapp.Listeners.RemoveFavoriteClickListener;
import com.example.recetapp.R;
import com.example.recetapp.RequestManager;
import com.example.recetapp.models.FavoriteApiRes;
import com.example.recetapp.models.RandomApiRes;
import com.example.recetapp.models.Recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeFavorites extends Fragment {

    //Dialog
    LoadingDialog dialog;

    RequestManager manager;
    FavoriteRecipeAdapter favoriteRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    String ids = "";

    public RecipeFavorites() {}

    public static RecipeFavorites newInstance() {
        RecipeFavorites fragment = new RecipeFavorites();
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
        View v = inflater.inflate(R.layout.fragment_recipe_favorites, container, false);
        recyclerView = v.findViewById(R.id.recycler_favoritesRecipes);
        // Los recycler View necesitan un adapter cuando la informacion es asincronica, si no tira un error.
        // Aca estoy llamando a un constructor vacio para que no tire error que despues va a ser reemplazado.
        setTempAdapteronReciclerView();

        // Instancia de dialogo de progreso
        dialog = new LoadingDialog(getContext());
        dialog.ShowDialog("Cargando favoritos...");


        // Obtener la lista de favoritos
        ids = getFavoritesIds();

        if (ids.length() > 0) {
            manager = new RequestManager(getContext());
            manager.getFavoriteRecipes(favoriteRecipeResponseListener, ids);
        }
        else{
            Log.d("No favorites", "No Favorites");
        }

        return v;
    }

    private String getFavoritesIds(){
        String response = "";

        SharedPreferences pref = getContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Set<String> favoritesList = new HashSet<String>(pref.getStringSet("favorites",new HashSet<String>()));

        if (favoritesList != null && favoritesList.size() > 0) {
            for (String favorite : favoritesList) {
                if (response.length() == 0) response += favorite;
                else response += ',' + favorite;
            }
        }

        return response;
    }

    public final FavoriteRecipeResponseListener favoriteRecipeResponseListener = new FavoriteRecipeResponseListener() {
        @Override
        public void didFetch(List<Recipe> response, String message) {
            dialog.HideDialog();
            favoriteRecipeAdapter = new FavoriteRecipeAdapter(getContext(), response, recipeDetailsClickListener, removeFavoriteClickListener);
            recyclerView.setAdapter(favoriteRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            dialog.HideDialog();
            Log.d("Error Favorites List", message);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    };

    private final RecipeDetailsClickListener recipeDetailsClickListener = new RecipeDetailsClickListener() {
        @Override
        public void onRecipeClick(String id) {
            Fragment fragment = RecipeDetailsFragment.newInstance();
            Bundle arguments = new Bundle();
            arguments.putString("id_recipe", id);
            fragment.setArguments(arguments);
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit)
                    .addToBackStack(null)
                    .replace(R.id.home_content, fragment)
                    .commit();
        }
    };

    private final RemoveFavoriteClickListener removeFavoriteClickListener = new RemoveFavoriteClickListener() {
        @Override
        public void onButtonRemoveClick(String id) {
            // Obtener el objeto de preferencias
            SharedPreferences pref = getContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);

            // Crear un editor
            SharedPreferences.Editor editor = pref.edit();
            // Obtener la lista de strings de preferencias
            Set<String> favoritesList = new HashSet<String>(pref.getStringSet("favorites",new HashSet<String>()));
            // Remueve de favoritos
            favoritesList.remove(String.valueOf(id));

            // Aplica la nueva lista
            editor.putStringSet("favorites", favoritesList);
            editor.apply();

            Toast.makeText(getContext(), "Se quitó de favoritos", Toast.LENGTH_LONG).show();
        }
    };

    private void setTempAdapteronReciclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }
}