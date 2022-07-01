package com.example.recetapp.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recetapp.Adapters.RandomRecipeAdapter;
import com.example.recetapp.Listeners.LoadingDialog;
import com.example.recetapp.Listeners.RandomRecipeResponseListener;
import com.example.recetapp.Listeners.RecipeDetailsClickListener;
import com.example.recetapp.R;
//import com.example.recetapp.RecipeDetailsActivity;
import com.example.recetapp.RequestManager;
import com.example.recetapp.models.RandomApiRes;

import java.util.ArrayList;
import java.util.List;

public class RecipeSearchFragment extends Fragment {

    //Dialog
    LoadingDialog dialog;

    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    List<String> tags = new ArrayList<>();

    // Elementos no estaticos
    ImageView searchLogo;
    ImageView notFoundLogo;
    ImageView gradient_searchRecipe;
    TextView searchRecipesTitle;
    TextView searchRecipesSubtitle;

    public RecipeSearchFragment() {}

    public static RecipeSearchFragment newInstance() {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_search, container, false);
        recyclerView = v.findViewById(R.id.recycler_searchRecipe);
        // Los recycler View necesitan un adapter cuando la informacion es asincronica, si no tira un error.
        // Aca estoy llamando a un constructor vacio para que no tire error que despues va a ser reemplazado.
        setTempAdapteronReciclerView();

        // Instancia de dialogo de progreso
        dialog = new LoadingDialog(getContext());


        // Instancia de elementos a cambiar
        searchLogo = v.findViewById(R.id.recipesSearch_SearchLogo);
        notFoundLogo = v.findViewById(R.id.recipesSearch_NotFoundLogo);
        gradient_searchRecipe = v.findViewById(R.id.gradient_searchRecipe);
        searchRecipesTitle = v.findViewById(R.id.recipesSearch_Title);
        searchRecipesSubtitle = v.findViewById(R.id.recipesSearch_subtitle);

        //Si viene con busqueda de main fragment
        if (getArguments().getStringArrayList("tags_argument") != null){
            tags = getArguments().getStringArrayList("tags_argument");
            toggleHiddenElements();
            searchRecipesTitle.setText("Busqueda: " + TextUtils.join(", ", tags));
        }

        manager = new RequestManager(getContext());

        if (tags.size() > 0){
            dialog.ShowDialog("Buscando platos...");
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
        }

        return v;
    }

    private void toggleHiddenElements(){
        recyclerView.setVisibility(View.VISIBLE);
        searchLogo.setVisibility(View.GONE);
        notFoundLogo.setVisibility(View.GONE);
        gradient_searchRecipe.setVisibility(View.VISIBLE);
//        searchRecipesTitle.setVisibility(isVisible() ? View.GONE : View.VISIBLE);
        searchRecipesSubtitle.setVisibility(View.GONE);
    }

    private void notFoundRecipes(){
        searchLogo.setVisibility(View.GONE);
        gradient_searchRecipe.setVisibility(View.GONE);
        searchRecipesSubtitle.setVisibility(View.VISIBLE);
        notFoundLogo.setVisibility(View.VISIBLE);
    }

    public final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomApiRes response, String message) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            recyclerView.setNestedScrollingEnabled(false);
            if (response.recipes.size() < 1) {
                dialog.HideDialog();
                notFoundRecipes();
                searchRecipesTitle.setText("Oops, no se encontraron platos\n" + "con los criterios de búsqueda ingresados: " + TextUtils.join(", ", tags));
                searchRecipesSubtitle.setText("Cambie la búsqueda e intente nuevamente en unos momentos");
                Toast.makeText(getContext(), "No se encontraron recetas", Toast.LENGTH_SHORT).show();
            }
            else{
                dialog.HideDialog();
                randomRecipeAdapter = new RandomRecipeAdapter(getContext(), response.recipes, recipeDetailsClickListener);
                recyclerView.setAdapter(randomRecipeAdapter);
            }
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
                    .addToBackStack(null)
                    .setCustomAnimations(R.anim.enter, R.anim.exit)
                    .replace(R.id.home_content, fragment)
                    .commit();
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